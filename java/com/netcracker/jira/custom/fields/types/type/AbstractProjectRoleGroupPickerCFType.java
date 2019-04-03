package com.netcracker.jira.custom.fields.types.type;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.converters.MultiGroupConverter;
import com.atlassian.jira.issue.customfields.impl.MultiGroupCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.rest.json.beans.JiraBaseUrls;
import com.atlassian.jira.plugin.webresource.JiraWebResourceManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleActor;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.security.roles.RoleActor;
import com.atlassian.jira.web.FieldVisibilityManager;
import com.google.common.collect.Lists;
import com.netcracker.jira.custom.fields.types.component.CFTypesService;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import com.netcracker.jira.custom.fields.types.model.ProjectRoleModel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractProjectRoleGroupPickerCFType extends MultiGroupCFType {

    private final JiraWebResourceManager jiraWebResourceManager;
    private final CFTypesService cfTypesService;
    private final ProjectRoleManager projectRoleManager;

    public AbstractProjectRoleGroupPickerCFType( CustomFieldValuePersister customFieldValuePersister, GenericConfigManager genericConfigManager,
                                                 MultiGroupConverter multiGroupConverter, PermissionManager permissionManager,
                                                 JiraAuthenticationContext authenticationContext, GroupManager groupManager,
                                                 FieldVisibilityManager fieldVisibilityManager, JiraBaseUrls jiraBaseUrls,
                                                 JiraWebResourceManager jiraWebResourceManager, CFTypesService cfTypesService,
                                                 ProjectRoleManager projectRoleManager) {
        super(customFieldValuePersister, genericConfigManager, multiGroupConverter, permissionManager, authenticationContext, groupManager, fieldVisibilityManager, jiraBaseUrls);
        this.jiraWebResourceManager = jiraWebResourceManager;
        this.cfTypesService = cfTypesService;
        this.projectRoleManager = projectRoleManager;
    }

    protected void fillParametersMap(Issue issue, CustomField field, Map<String, Object> params, boolean addDefaultValueIfSet){
        List<String> groups = Lists.newArrayList();
        CustomFieldData cfData = cfTypesService.getCustomFieldById(field.getId());

        if (issue != null) {
            if (StringUtils.isNotBlank(cfData.getDefaultValue()) && addDefaultValueIfSet){
                groups.add(cfData.getDefaultValue());
                params.put("hasDefaultValue", true);
            } else params.put("hasDefaultValue", false);

            List<ProjectRoleModel> roles = cfData.getRoles();
            if (roles != null){
                for (ProjectRoleModel temp : roles){
                    ProjectRole projectRole = projectRoleManager.getProjectRole(temp.getId());
                    if (projectRole == null) continue;
                    if (issue.getProjectObject() == null) continue;
                    for (String tempGroup : projectRoleManager.getProjectRoleActors(projectRole, issue.getProjectObject())
                            .getRoleActorsByType(ProjectRoleActor.GROUP_ROLE_ACTOR_TYPE)
                            .stream()
                            .map(RoleActor::getParameter)
                            .collect(Collectors.toList())){
                        if (!groups.contains(tempGroup) && !excludeGroup(tempGroup, cfData.getRegExpFilter())){
                            groups.add(tempGroup);
                        }
                    }
                }
            }
        }

        params.put("availableGroups", groups);
        params.put("isMultiple", isMultiple());
    }

    private boolean excludeGroup(String group, String filter){
        if (StringUtils.isBlank(filter)) return false;
        Pattern pattern = Pattern.compile(filter);
        Matcher matcher = pattern.matcher(group);
        return matcher.matches();
    }
}
