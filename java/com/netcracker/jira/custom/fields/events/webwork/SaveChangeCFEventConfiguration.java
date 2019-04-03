package com.netcracker.jira.custom.fields.events.webwork;

import com.netcracker.jira.custom.fields.events.component.ChangeCFEventConfigurationManager;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveChangeCFEventConfiguration extends AbstractChangeCFEventConfiguration {

    private String customFieldId;
    private long eventTypeId;

    @Autowired
    public SaveChangeCFEventConfiguration(ChangeCFEventConfigurationManager changeCFEventConfigurationManager) {
        super(changeCFEventConfigurationManager);
    }

    @Override
    public String doExecute() throws Exception {
        changeCFEventConfigurationManager.setCFId(customFieldId);
        changeCFEventConfigurationManager.setEventTypeId(eventTypeId);

        return getRedirect("/secure/admin/ViewChangeCFEventConfiguration.jspa");
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public void setEventTypeId(long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
}
