package com.netcracker.jira.custom.fields.types.automation.rest;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleActor;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.security.roles.RoleActor;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.netcracker.jira.custom.fields.types.component.CFTypesService;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import com.netcracker.jira.custom.fields.types.model.ProjectRoleModel;
import com.netcracker.jira.custom.fields.types.type.ProjectRoleGroupPickerCFType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Path("/automation-rule-helper")
public class CustomFieldTypeResources {
    private final CFTypesService cfTypesService;
    private final ProjectRoleManager projectRoleManager;
    @ComponentImport private final ProjectManager projectManager;

    @Autowired
    public CustomFieldTypeResources(CFTypesService cfTypesService, ProjectRoleManager projectRoleManager, ProjectManager projectManager) {
        this.cfTypesService = cfTypesService;
        this.projectRoleManager = projectRoleManager;
        this.projectManager = projectManager;
    }

    @GET
    @Path("cf-detail/{customFieldId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomField(@PathParam("customFieldId") String customFieldID) {

        CustomFieldData cfData = cfTypesService.getCustomFieldById(customFieldID);
        return Response.ok(new Gson().toJson(cfData), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("cf-all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomFields(@QueryParam("query") String query) {
        List<CustomFieldData> projectRoleGroupPickerFields = cfTypesService.getCustomFieldsByType(ProjectRoleGroupPickerCFType.class);

        List<CustomFieldData> selectedCustomFieldDataList = Lists.newArrayList();

        for (CustomFieldData cf : projectRoleGroupPickerFields) {
            if (StringUtils.containsIgnoreCase(cf.getFieldName(), query)){
                selectedCustomFieldDataList.add(cf);
            }
        }

        return Response.ok(new Gson().toJson(selectedCustomFieldDataList), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("cf-groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomFieldGroups(@FormParam("customFieldId") String customFieldId, @FormParam("projectKey") String projectKey, @FormParam("query") String query) {
        List<String> groups = Lists.newArrayList();
        List<String> filterGroup=new ArrayList<>();
        if (!customFieldId.equals("undefined")) {
            ProjectManager projectManager = ComponentAccessor.getProjectManager();
            CustomFieldData cfData = cfTypesService.getCustomFieldById(customFieldId);
            List<ProjectRoleModel> roles = cfData.getRoles();
            if (roles != null) {
                for (ProjectRoleModel temp : roles) {
                    ProjectRole projectRole = projectRoleManager.getProjectRole(temp.getId());
                    if (projectRole == null) continue;

                    for (String tempGroup : projectRoleManager.getProjectRoleActors(projectRole, projectManager.getProjectObjByKey(projectKey))
                            .getRoleActorsByType(ProjectRoleActor.GROUP_ROLE_ACTOR_TYPE)
                            .stream()
                            .map(RoleActor::getParameter)
                            .collect(Collectors.toList())) {
                        if (!groups.contains(tempGroup) && !excludeGroup(tempGroup,cfData.getRegExpFilter())) {
                            groups.add(tempGroup);
                        }
                    }
                }
            }


        filterGroup = groups.stream().filter(line -> line.toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());

        }

        return Response.ok(new Gson().toJson(filterGroup), MediaType.APPLICATION_JSON).build();
    }

    private boolean excludeGroup(String group, String filter){
        if (StringUtils.isBlank(filter)) return false;
        Pattern pattern = Pattern.compile(filter);
        Matcher matcher = pattern.matcher(group);
        return matcher.matches();
    }
}
