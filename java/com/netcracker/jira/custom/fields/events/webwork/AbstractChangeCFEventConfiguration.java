package com.netcracker.jira.custom.fields.events.webwork;

import com.atlassian.jira.web.action.ActionViewDataMappings;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.netcracker.jira.custom.fields.events.component.ChangeCFEventConfigurationManager;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChangeCFEventConfiguration extends JiraWebActionSupport{
    protected final Map data = new HashMap();
    protected ChangeCFEventConfigurationManager changeCFEventConfigurationManager;

    public AbstractChangeCFEventConfiguration(ChangeCFEventConfigurationManager changeCFEventConfigurationManager) {
        this.changeCFEventConfigurationManager = changeCFEventConfigurationManager;
    }

    @ActionViewDataMappings({"input", "success", "error"})
    public Map getData() {
        return data;
    }
}
