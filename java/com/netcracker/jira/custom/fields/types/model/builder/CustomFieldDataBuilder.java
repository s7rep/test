package com.netcracker.jira.custom.fields.types.model.builder;

import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import com.netcracker.jira.custom.fields.types.model.ProjectRoleModel;

import java.util.List;

public class CustomFieldDataBuilder {

    private String customFieldId;
    private String customFieldName;
    private List<ProjectRoleModel> roles;
    private boolean isAllProjects;
    private List<String> projects;
    private String defaultValue;
    private String regExpFilter;

    public CustomFieldDataBuilder setCustomFieldId(String customFieldId){
        this.customFieldId = customFieldId;
        return this;
    }

    public CustomFieldDataBuilder setCustomFieldName(String customFieldName){
        this.customFieldName = customFieldName;
        return this;
    }

    public CustomFieldDataBuilder setProjectRoles(List<ProjectRoleModel> roles){
        this.roles = roles;
        return this;
    }

    public CustomFieldDataBuilder setIsAllProject(boolean isAllProjects){
        this.isAllProjects = isAllProjects;
        return this;
    }

    public CustomFieldDataBuilder setProjects(List<String> projects){
        this.projects = projects;
        return this;
    }

    public CustomFieldDataBuilder setDefaultValue(String defaultValue){
        this.defaultValue = defaultValue;
        return this;
    }

    public CustomFieldDataBuilder setRegExpFilter(String regExpFilter){
        this.regExpFilter = regExpFilter;
        return this;
    }

    public CustomFieldData build(){
        return new CustomFieldData(customFieldId, customFieldName, roles, isAllProjects, projects, defaultValue, regExpFilter);
    }

    public CustomFieldData build(String cfId, String cfName){
        return new CustomFieldData(cfId, cfName);
    }

    public static CustomFieldDataBuilder builder(){
        return new CustomFieldDataBuilder();
    }
}
