package com.aq.aqconnect;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by Vinay on 6/19/2018.
 */
public class AQPluginTaskConfigurator extends AbstractTaskConfigurator {

    public static final String APP_URL = "appURL";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PROJECT_NAME = "projectName";
    public static final String JOB_PID = "jobPid";
    public static final String RUN_PARAM_STR = "runParamStr";


    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(APP_URL, params.getString(APP_URL));
        config.put(USERNAME, params.getString(USERNAME));
        config.put(PASSWORD, params.getString(PASSWORD));
        config.put(PROJECT_NAME, params.getString(PROJECT_NAME));
        config.put(JOB_PID, params.getString(JOB_PID));
        config.put(RUN_PARAM_STR, params.getString(RUN_PARAM_STR));
        return config;
    }

}
