package com.netcracker.jira.custom.fields.types.component;

import com.atlassian.jira.issue.fields.CustomField;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;

public interface CustomFieldDataManager {

    CustomFieldData getCustomFieldData(CustomField cf);

    void saveCustomFieldData(CustomFieldData cfData);

}
