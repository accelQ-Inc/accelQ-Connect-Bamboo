package com.aq.aqconnect;

import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.plugin.web.model.WebPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Map;

/**
 * Created by Vinay on 7/13/2018.
 */
public class AQPluginResultWebPanel implements WebPanel {

    public String getHtml(Map<String, Object> map) {
        Map<String, String> customBuildData = ((ChainResultsSummary) map.get("resultSummary")).getOrderedJobResultSummaries().get(0).getCustomBuildData();
        String resultJson = customBuildData.get(AQPluginConstants.AQ_RESULT_INFO_KEY);
        JSONParser parser = new JSONParser();
        try {
            JSONObject resultJsonObj = (JSONObject) parser.parse(resultJson);
            long passCount = (Long) resultJsonObj.get("pass"),
                 failCount = (Long) resultJsonObj.get("fail"),
                 notRunCount = (Long) resultJsonObj.get("notRun"),
                 totalCount = passCount + failCount + notRunCount;
            String tmpl = "", str = null;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/templates/report.html")));
            while((str = in.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            tmpl = sb.toString();
            String[] splitTmpl = tmpl.split("=====>");
            return splitTmpl[0] + String.format(splitTmpl[1],
                    resultJsonObj.get("status"),
                    totalCount,
                    passCount,
                    failCount,
                    notRunCount,
                    resultJsonObj.get("reportLink"));
        } catch(Exception e ) {

        }
        return "";
    }

    public void writeHtml(Writer writer, Map<String, Object> map) throws IOException {

    }
}
