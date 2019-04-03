package com.netcracker.jira.custom.fields.types.webwork;

import com.atlassian.jira.plugin.webresource.JiraWebResourceManager;
import com.atlassian.jira.web.action.ActionViewDataMappings;
import com.atlassian.jira.web.action.JiraWebActionSupport;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCFTypesConfiguration extends JiraWebActionSupport {
    protected final Map data = new HashMap();
    protected JiraWebResourceManager jiraWebResourceManager;

    public AbstractCFTypesConfiguration(JiraWebResourceManager jiraWebResourceManager) {
        this.jiraWebResourceManager = jiraWebResourceManager;
    }

    public JiraWebResourceManager getJiraWebResourceManager() {
        return jiraWebResourceManager;
    }

    @ActionViewDataMappings({"input", "success", "error"})
    public Map getData() {
        return data;
    }
}
