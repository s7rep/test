package com.netcracker.jira.custom.fields.types.webwork;

import com.atlassian.jira.plugin.webresource.JiraWebResourceManager;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.netcracker.jira.custom.fields.types.component.CFTypesService;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import com.netcracker.jira.custom.fields.types.model.ProjectRoleModel;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SaveCFTypesConfiguration extends AbstractCFTypesConfiguration {

    private final CFTypesService cfTypesService;
    private final ProjectRoleManager projectRoleManager;

    private String customFieldId;
    private String projectRoles;
    private String defaultValue;
    private String regExpFilter;

    public SaveCFTypesConfiguration(CFTypesService cfTypesService,
                                    @ComponentImport JiraWebResourceManager jiraWebResourceManager,
                                    @ComponentImport ProjectRoleManager projectRoleManager) {
        super(jiraWebResourceManager);
        this.cfTypesService = cfTypesService;
        this.projectRoleManager = projectRoleManager;
    }

    @Override
    public String doExecute() throws Exception {
        CustomFieldData cfData = cfTypesService.getCustomFieldById(customFieldId);

        if (StringUtils.isNotBlank(projectRoles)){
            List<ProjectRoleModel> roles = Arrays.stream(StringUtils.split(projectRoles, ",")).map(role -> {
                long id = Long.parseLong(role);
                ProjectRole roleObject = projectRoleManager.getProjectRole(id);
                return new ProjectRoleModel(roleObject.getId(), roleObject.getName());
            }).collect(Collectors.toList());
            cfData.setRoles(roles);
        }

        if (defaultValue != null){
            cfData.setDefaultValue(defaultValue);
        }

        if (regExpFilter != null){
            cfData.setRegExpFilter(regExpFilter);
        }

        cfTypesService.saveCustomFieldData(cfData);

        return getRedirect("/secure/admin/ViewCFTypesConfiguration.jspa");
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public void setProjectRoles(String projectRoles) {
        this.projectRoles = projectRoles;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setRegExpFilter(String regExpFilter) {
        this.regExpFilter = regExpFilter;
    }
}
