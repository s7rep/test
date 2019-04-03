package com.netcracker.jira.custom.fields.types.component;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.project.Project;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.Gson;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import com.netcracker.jira.custom.fields.types.model.builder.CustomFieldDataBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DefaultCustomFieldDataManager implements CustomFieldDataManager {
    private final static String CF_TYPES_CONFIGURATION_KEY = "com.netcracker.jira.custom-field-types.configuration";

    private PluginSettings pluginSettings;

    @Autowired
    public DefaultCustomFieldDataManager(@ComponentImport PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey(CF_TYPES_CONFIGURATION_KEY);
    }

    private CustomFieldData getDefaultCustomFieldData(CustomField cf){
        CustomFieldDataBuilder builder = CustomFieldDataBuilder.builder();
        builder.setCustomFieldId(cf.getId());
        builder.setCustomFieldName(cf.getName());
        if (cf.isAllProjects()){
            builder.setIsAllProject(true);
        } else {
            builder.setIsAllProject(false);
            builder.setProjects(cf.getAssociatedProjectObjects().stream().map(Project::getKey).collect(Collectors.toList()));
        }

        return builder.build();
    }

    @Override
    public CustomFieldData getCustomFieldData(CustomField cf) {
        String temp = (String)pluginSettings.get(cf.getId());
        if (StringUtils.isBlank(temp)) {
            return getDefaultCustomFieldData(cf);
        }
        Gson gson = new Gson();
        CustomFieldData cfData = gson.fromJson(temp, CustomFieldData.class);
        if (cfData == null) return getDefaultCustomFieldData(cf);
        return cfData;
    }

    @Override
    public void saveCustomFieldData(CustomFieldData cfData) {
        Gson gson = new Gson();
        String data = gson.toJson(cfData, CustomFieldData.class);
        pluginSettings.put(cfData.getFieldId(), data);
    }
}
