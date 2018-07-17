package com.aq.aqconnect;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by TJ on 6/18/2018.
 */

public class AQPluginTestResultReportGenerator {

	public void generateReport(String outputPath, String url) throws Exception {
		PrintWriter fileOut = null;
        try {
			File reportsDir = new File(outputPath + "/Reports");
			if(!reportsDir.exists()) {
				reportsDir.mkdir();
			}
			fileOut = new PrintWriter(new File(outputPath + "/Reports/report.html"));
			String content = "<script>" +
							 String.format("window.location.href= '%s';", url) +
							 "</script>";
			fileOut.println(content);
			fileOut.flush();
        } finally {
			fileOut.close();
        }
	}
}
