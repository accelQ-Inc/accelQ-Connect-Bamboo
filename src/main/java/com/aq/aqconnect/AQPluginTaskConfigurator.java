package com.aq.aqconnect;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.TaskConfiguratorHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.List;
import java.util.Arrays;

/**
 * Created by Vinay on 6/19/2018.
 */

@Scanned
public class AQPluginTaskConfigurator extends AbstractTaskConfigurator {

    public static final String APP_URL = "appURL";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PROJECT_NAME = "projectName";
    public static final String JOB_PID = "jobPid";
    public static final String RUN_PARAM_STR = "runParamStr";

    private final TaskConfiguratorHelper taskConfiguratorHelper;

    @Autowired
    public AQPluginTaskConfigurator(@ComponentImport final TaskConfiguratorHelper taskConfiguratorHelper) {
        this.taskConfiguratorHelper = taskConfiguratorHelper;
    }

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(config, params, getFieldsToCopy());
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, getFieldsToCopy());
    }

    protected List<String> getFieldsToCopy() {
        return Arrays.asList(new String[]{APP_URL, USERNAME, PASSWORD, PROJECT_NAME, JOB_PID, RUN_PARAM_STR});
    }

}
