package com.netcracker.jira.custom.fields.types.component;

import com.netcracker.jira.custom.fields.types.model.CustomFieldData;

import java.util.List;

public interface CFTypesService {

    List<CustomFieldData> getCustomFieldsByType(Class<?> cfClass);

    CustomFieldData getCustomFieldById(String id);

    void saveCustomFieldData(CustomFieldData cfData);
}
