package com.netcracker.jira.custom.fields.events.webwork;

import com.atlassian.jira.user.ApplicationUser;
import com.netcracker.jira.custom.fields.events.component.ChangeCFEventConfigurationManager;
import org.springframework.beans.factory.annotation.Autowired;

public class EditChangeCFEventConfiguration extends AbstractChangeCFEventConfiguration {

    @Autowired
    public EditChangeCFEventConfiguration(ChangeCFEventConfigurationManager changeCFEventConfigurationManager) {
        super(changeCFEventConfigurationManager);
    }

    @Override
    public String doExecute() throws Exception {
        ApplicationUser loggedInUser = getLoggedInUser();
        if (loggedInUser == null) return "securitybreach";

        data.put("customFieldId", changeCFEventConfigurationManager.getCFId());
        data.put("eventTypeId", changeCFEventConfigurationManager.getEventTypeId());

        return SUCCESS;
    }
}
