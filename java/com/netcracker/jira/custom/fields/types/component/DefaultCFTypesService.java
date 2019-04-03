package com.netcracker.jira.custom.fields.types.component;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultCFTypesService implements CFTypesService {

    @ComponentImport
    private final CustomFieldManager customFieldManager;

    private final CustomFieldDataManager customFieldDataManager;

    @Autowired
    public DefaultCFTypesService(CustomFieldManager customFieldManager, CustomFieldDataManager customFieldDataManager) {
        this.customFieldManager = customFieldManager;
        this.customFieldDataManager = customFieldDataManager;
    }

    @Override
    public List<CustomFieldData> getCustomFieldsByType(Class<?> cfClass) {
        return customFieldManager.getCustomFieldObjects().stream()
                .filter(cf -> cf.getCustomFieldType().getClass() == cfClass)
                .map(customFieldDataManager::getCustomFieldData)
                .collect(Collectors.toList());
    }

    @Override
    public CustomFieldData getCustomFieldById(String id) {
        CustomField cf = customFieldManager.getCustomFieldObject(id);
        CustomFieldData cfData = customFieldDataManager.getCustomFieldData(cf);
        return cfData;
    }

    @Override
    public void saveCustomFieldData(CustomFieldData cfData) {
        customFieldDataManager.saveCustomFieldData(cfData);
    }
}
