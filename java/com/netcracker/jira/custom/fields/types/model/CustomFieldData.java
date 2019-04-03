package com.netcracker.jira.custom.fields.types.model;

import com.google.common.collect.Lists;

import java.util.List;

public class CustomFieldData {

    private String fieldId;
    private String fieldName;
    private List<ProjectRoleModel> roles;
    private boolean isAllProjects;
    private List<String> projects;
    private String defaultValue;
    private String regExpFilter;


    public CustomFieldData() {
    }

    public CustomFieldData(String fieldId, String fieldName, List<ProjectRoleModel> roles, boolean isAllProjects,
                           List<String> projects, String defaultValue, String regExpFilter) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.roles = roles;
        this.isAllProjects = isAllProjects;
        this.projects = projects;
        this.defaultValue = defaultValue;
        this.regExpFilter = regExpFilter;
    }

    public CustomFieldData(String fieldId, String fieldName) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.roles = Lists.newArrayList();
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<ProjectRoleModel> getRoles() {
        return roles;
    }

    public void setRoles(List<ProjectRoleModel> roles) {
        this.roles = roles;
    }

    public boolean isAllProjects() {
        return isAllProjects;
    }

    public void setAllProjects(boolean allProjects) {
        isAllProjects = allProjects;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRegExpFilter() {
        return regExpFilter;
    }

    public void setRegExpFilter(String regExpFilter) {
        this.regExpFilter = regExpFilter;
    }
}
