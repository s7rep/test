package com.netcracker.jira.custom.fields.events.listener;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.event.type.EventTypeManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.changehistory.ChangeHistory;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.history.ChangeItemBean;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netcracker.jira.custom.fields.events.component.ChangeCFEventConfigurationManager;
import org.apache.commons.lang3.StringUtils;
import org.ofbiz.core.entity.GenericValue;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class ChangeCFEventListener implements InitializingBean, DisposableBean {

    private final EventPublisher eventPublisher;
    private final EventTypeManager eventTypeManager;
    private final CustomFieldManager customFieldManager;
    private final ChangeHistoryManager changeHistoryManager;
    private final ChangeCFEventConfigurationManager changeCFEventConfigurationManager;

    @Autowired
    public ChangeCFEventListener(@ComponentImport EventPublisher eventPublisher,
                                 @ComponentImport EventTypeManager eventTypeManager,
                                 @ComponentImport CustomFieldManager customFieldManager,
                                 @ComponentImport ChangeHistoryManager changeHistoryManager,
                                 ChangeCFEventConfigurationManager changeCFEventConfigurationManager) {
        this.eventPublisher = eventPublisher;
        this.eventTypeManager = eventTypeManager;
        this.customFieldManager = customFieldManager;
        this.changeHistoryManager = changeHistoryManager;
        this.changeCFEventConfigurationManager = changeCFEventConfigurationManager;
    }

    @EventListener
    public void onIssueEvent(IssueEvent issueEvent){
        if (Objects.equals(issueEvent.getEventTypeId(), EventType.ISSUE_UPDATED_ID) ||
                Objects.equals(issueEvent.getEventTypeId(), EventType.ISSUE_GENERICEVENT_ID)) {
            String triggerCustomFieldId = changeCFEventConfigurationManager.getCFId();

            GenericValue changeLog = issueEvent.getChangeLog();
            if (changeLog == null) return;
            if (!changeLog.containsKey("id")) return;

            ChangeHistory changeHistory = changeHistoryManager.getChangeHistoryById((Long)changeLog.get("id"));
            if (changeHistory == null) return;

            List<ChangeItemBean> changeItemsBeans = changeHistory.getChangeItemBeans();
            List<CustomField> customFields = Lists.newArrayList();
            for (ChangeItemBean changeItemBean : changeItemsBeans){
                if (StringUtils.equals(ChangeItemBean.CUSTOM_FIELD, changeItemBean.getFieldType())){
                    customFields.addAll(customFieldManager.getCustomFieldObjectsByName(changeItemBean.getField()));
                }
            }

            Optional<CustomField> optional = customFields.stream()
                    .filter(customField -> StringUtils.equals(customField.getId(), triggerCustomFieldId))
                    .findAny();

            long customEventTypeId = changeCFEventConfigurationManager.getEventTypeId();

            if (optional.isPresent() && customEventTypeId != 0L){
                EventType type = eventTypeManager.getEventType(customEventTypeId);
                Map<String, Object> params = Maps.newHashMap(issueEvent.getParams());
                params.put("triggerCustomFieldId", triggerCustomFieldId);
                if (type != null) eventPublisher.publish(new IssueEvent(issueEvent.getIssue(), params,
                        issueEvent.getUser(), type.getId(), true));
            }

            return;
        }
        if (Objects.equals(issueEvent.getEventTypeId(), EventType.ISSUE_CREATED_ID)) {
            String triggerCustomFieldId = changeCFEventConfigurationManager.getCFId();
            CustomField field = customFieldManager.getCustomFieldObject(triggerCustomFieldId);
            if (field == null) return;

            Object cfFieldValue = issueEvent.getIssue().getCustomFieldValue(field);
            if (cfFieldValue == null) return;

            long customEventTypeId = changeCFEventConfigurationManager.getEventTypeId();
            if (customEventTypeId == 0L) return;

            EventType type = eventTypeManager.getEventType(customEventTypeId);
            Map<String, Object> params = Maps.newHashMap(issueEvent.getParams());
            params.put("triggerCustomFieldId", triggerCustomFieldId);
            if (type != null) eventPublisher.publish(new IssueEvent(issueEvent.getIssue(), params,
                    issueEvent.getUser(), type.getId(), true));

            return;
        }
    }

    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }
}
