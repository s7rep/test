package com.netcracker.jira.custom.fields.types.type;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.converters.MultiGroupConverter;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.rest.json.beans.JiraBaseUrls;
import com.atlassian.jira.plugin.webresource.JiraWebResourceManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.web.FieldVisibilityManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.netcracker.jira.custom.fields.types.component.CFTypesService;

import javax.annotation.Nonnull;
import java.util.Map;

public class ProjectRoleMultiGroupPickerCFType extends AbstractProjectRoleGroupPickerCFType {

    public ProjectRoleMultiGroupPickerCFType(@ComponentImport CustomFieldValuePersister customFieldValuePersister,
                                             @ComponentImport GenericConfigManager genericConfigManager,
                                             @ComponentImport MultiGroupConverter multiGroupConverter,
                                             @ComponentImport PermissionManager permissionManager,
                                             @ComponentImport JiraAuthenticationContext authenticationContext,
                                             @ComponentImport GroupManager groupManager,
                                             @ComponentImport FieldVisibilityManager fieldVisibilityManager,
                                             @ComponentImport JiraBaseUrls jiraBaseUrls,
                                             @ComponentImport JiraWebResourceManager jiraWebResourceManager,
                                             CFTypesService cfTypesService,
                                             @ComponentImport ProjectRoleManager projectRoleManager) {
        super(customFieldValuePersister, genericConfigManager, multiGroupConverter, permissionManager,
                authenticationContext, groupManager, fieldVisibilityManager, jiraBaseUrls, jiraWebResourceManager,
                cfTypesService, projectRoleManager);
    }

    @Nonnull
    @Override
    public Map<String, Object> getVelocityParameters(Issue issue, CustomField field, FieldLayoutItem fieldLayoutItem) {
        Map<String, Object> params = super.getVelocityParameters(issue, field, fieldLayoutItem);
        fillParametersMap(issue, field, params, false);
        return params;
    }

}
