package com.aq.aqconnect;

import com.atlassian.bamboo.build.logger.BuildLogger;

/**
 * Created by Vinay on 6/20/2018.
 */
public class AQPluginTaskConsoleLogger {

    private BuildLogger logger;

    public AQPluginTaskConsoleLogger(BuildLogger logger) {
        this.logger = logger;
    }

    public void print(String msg) {
        this.logger.addBuildLogEntry(msg);
    }

    public void println() {
        print("\n");
    }

    public void println(String msg) {
        print(msg + "\n");
    }
}
