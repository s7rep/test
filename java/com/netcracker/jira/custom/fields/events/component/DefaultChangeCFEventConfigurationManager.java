package com.netcracker.jira.custom.fields.events.component;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultChangeCFEventConfigurationManager implements ChangeCFEventConfigurationManager {
    private final static String CF_EVENTS_CONFIGURATION_KEY = "com.netcracker.jira.custom-field-events.configuration";
    private final static String CUSTOM_FIELD_ID_PARAM_KEY = "custom.field.id";
    private final static String EVENT_TYPE_ID_PARAM_KEY = "event.type.id";

    private PluginSettings pluginSettings;

    @Autowired
    public DefaultChangeCFEventConfigurationManager(@ComponentImport PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey(CF_EVENTS_CONFIGURATION_KEY);
    }

    @Override
    public String getCFId() {
        return (String)pluginSettings.get(CUSTOM_FIELD_ID_PARAM_KEY);
    }

    @Override
    public void setCFId(String cfId) {
        pluginSettings.put(CUSTOM_FIELD_ID_PARAM_KEY, cfId);
    }

    @Override
    public long getEventTypeId() {
        long result = 0L;
        try {
            result = Long.parseLong((String)pluginSettings.get(EVENT_TYPE_ID_PARAM_KEY));
        } catch (Exception e){

        }
        return result;
    }

    @Override
    public void setEventTypeId(long eventTypeId) {
        pluginSettings.put(EVENT_TYPE_ID_PARAM_KEY, Long.toString(eventTypeId));
    }
}
