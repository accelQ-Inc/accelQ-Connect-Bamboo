package com.aq.aqconnect;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.*;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import org.json.simple.JSONObject;

import java.util.Map;

@Scanned
public class AQPluginTask implements CommonTaskType
{
    public static final String APP_URL = "appURL";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PROJECT_NAME = "projectName";
    public static final String JOB_PID = "jobPid";
    public static final String RUN_PARAM_STR = "runParamStr";

    private void log(BuildLogger logger, String msg) {
        logger.addBuildLogEntry(msg);
    }

    private String prepareAQBuildData(String status, long passCount, long failCount, long notRunCount, String reportLink) {
        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("pass", passCount);
        obj.put("fail", failCount);
        obj.put("notRun", notRunCount);
        obj.put("reportLink", reportLink);

        return obj.toJSONString();
    }

    public TaskResult execute(final CommonTaskContext taskContext) throws TaskException
    {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final AQPluginTaskConsoleLogger logger = new AQPluginTaskConsoleLogger((buildLogger));
        final ConfigurationMap conf = taskContext.getConfigurationMap();
        boolean isJobFailed = false;
        Map<String, String> customBuildData = taskContext.getCommonContext().getCurrentResult().getCustomBuildData();

        try {
            logger.println("**************************************");
            logger.println("*** ACCELQ PLUGIN PROCESSING START ***");
            logger.println("**************************************");
            logger.println();

            //login via AQ REST client
            AQPluginRESTClient aqPluginRESTClient = AQPluginRESTClient.getInstance();
            aqPluginRESTClient.setUpBaseURL(conf.get(APP_URL).trim());
            if(aqPluginRESTClient.doLogin(conf.get(USERNAME), conf.get(PASSWORD), conf.get(PROJECT_NAME))) {
                logger.println(AQPluginConstants.LOG_DELIMITER + "Connection Successful");
                logger.println();
                String runParamJsonPayload = getRunParamJsonPayload(conf.get(RUN_PARAM_STR));
                JSONObject realJobObj = aqPluginRESTClient.triggerJob(Integer.parseInt(conf.get(JOB_PID)), runParamJsonPayload);
                if(realJobObj.get("cause") != null) {
                    throw new AQPluginException((String) realJobObj.get("cause"));
                }
                long realJobPid = (Long) realJobObj.get("pid");
                long passCount = 0, failCount = 0, runningCount = 0, totalCount = 0, notRunCount = 0;
                String jobPurpose = (String) realJobObj.get("purpose");
                String jobStatus = "";
                String resultAccessURL = aqPluginRESTClient.getResultExternalAccessURL(Long.toString(realJobPid));
                JSONObject summaryObj;
                int attempt = 0;
                logger.println("Purpose: " + jobPurpose);
                logger.println();
                do {
                    summaryObj = aqPluginRESTClient.getJobSummary(realJobPid);
                    if(summaryObj.get("cause") != null) {
                        throw new AQPluginException((String) summaryObj.get("cause"));
                    }
                    if(summaryObj.get("summary") != null) {
                        summaryObj = (JSONObject) summaryObj.get("summary");
                    }
                    passCount = (Long) summaryObj.get("pass");
                    failCount = (Long) summaryObj.get("fail");
                    notRunCount = (Long) summaryObj.get("notRun");
                    logger.println("Status: " + summaryObj.get("status"));
                    logger.println("Pass: " + passCount);
                    logger.println("Fail: " + failCount);
                    //logger.println("Running: " + runningCount);
                    logger.println("Not Run: " + notRunCount);
                    logger.println();
                    jobStatus = ((String) summaryObj.get("status")).toUpperCase();
                    customBuildData.put(AQPluginConstants.AQ_RESULT_INFO_KEY, prepareAQBuildData(((String) summaryObj.get("status")), passCount, failCount, notRunCount, resultAccessURL));
                    if(jobStatus.equals(AQPluginConstants.TEST_JOB_STATUS.SCHEDULED.getStatus().toUpperCase()))
                        ++attempt;
                    if(attempt == AQPluginConstants.JOB_PICKUP_RETRY_COUNT) {
                        throw new AQPluginException("No agent available to pickup the job");
                    }
                    Thread.sleep(AQPluginConstants.JOB_STATUS_POLL_TIME);
                } while(!jobStatus.equals(AQPluginConstants.TEST_JOB_STATUS.COMPLETED.getStatus().toUpperCase())
                        && !jobStatus.equals(AQPluginConstants.TEST_JOB_STATUS.ABORTED.getStatus().toUpperCase())
                        && !jobStatus.equals(AQPluginConstants.TEST_JOB_STATUS.FAILED.getStatus().toUpperCase()));

                isJobFailed = (failCount > 0) || jobStatus.equals(AQPluginConstants.TEST_JOB_STATUS.ABORTED.getStatus().toUpperCase()) || jobStatus.equals(AQPluginConstants.TEST_JOB_STATUS.FAILED.getStatus().toUpperCase());
                logger.print("Go to ");
                logger.print(resultAccessURL);
                logger.println(" for a detailed report");
                logger.println();

                logger.println("**************************************");
                logger.println("**** ACCELQ PLUGIN PROCESSING END ****");
                logger.println("**************************************");
                logger.println();

                //generate final report with app test result report redirection url
                new AQPluginTestResultReportGenerator().generateReport(taskContext.getRootDirectory().getAbsolutePath(), resultAccessURL);
            } else {
                throw new AQPluginException(AQPluginConstants.LOG_DELIMITER + "Connection Failed");
            }

            if(isJobFailed)
                return TaskResultBuilder.newBuilder(taskContext).failed().build();
            return TaskResultBuilder.newBuilder(taskContext).success().build();

        }
        catch (Exception exception){

            buildLogger.addErrorLogEntry(exception.getMessage());

            return TaskResultBuilder.newBuilder(taskContext).failed().build();

        }

    }

    private String getRunParamJsonPayload(String runParamStr) {
        if(runParamStr == null || runParamStr.trim().length() == 0)
            return null;
        JSONObject json = new JSONObject();
        String[] splitOnAmp = runParamStr.split("&");
        for(String split: splitOnAmp) {
            String[] splitOnEquals = split.split("=");
            if(splitOnEquals.length == 2) {
                String key = splitOnEquals[0].trim(), value = splitOnEquals[1].trim();
                if(!key.equals("") && !value.equals("")) {
                    json.put(key, value);
                }
            }
        }
        return json.toJSONString();
    }
}