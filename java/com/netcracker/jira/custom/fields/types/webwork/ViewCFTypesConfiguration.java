package com.netcracker.jira.custom.fields.types.webwork;

import com.atlassian.jira.plugin.webresource.JiraWebResourceManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.netcracker.jira.custom.fields.types.component.CFTypesService;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import com.netcracker.jira.custom.fields.types.type.ProjectRoleGroupPickerCFType;
import com.netcracker.jira.custom.fields.types.type.ProjectRoleMultiGroupPickerCFType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ViewCFTypesConfiguration extends AbstractCFTypesConfiguration {

    private final CFTypesService cfTypesService;

    @Autowired
    public ViewCFTypesConfiguration(CFTypesService cfTypesService,
                                    @ComponentImport JiraWebResourceManager jiraWebResourceManager) {
        super(jiraWebResourceManager);
        this.cfTypesService = cfTypesService;
    }


    @Override
    public String doExecute() throws Exception {
        ApplicationUser loggedInUser = getLoggedInUser();
        if (loggedInUser == null) return "securitybreach";

        List<CustomFieldData> projectRoleGroupPickerFields = cfTypesService.getCustomFieldsByType(ProjectRoleGroupPickerCFType.class);
        data.put("prgpFields", projectRoleGroupPickerFields);

        List<CustomFieldData> projectRoleMultiGroupPickerFields = cfTypesService.getCustomFieldsByType(ProjectRoleMultiGroupPickerCFType.class);
        data.put("prmgpFields", projectRoleMultiGroupPickerFields);
        return super.doExecute();
    }
}
