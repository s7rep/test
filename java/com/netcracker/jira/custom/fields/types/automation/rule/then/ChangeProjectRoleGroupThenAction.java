package com.netcracker.jira.custom.fields.types.automation.rule.then;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.fugue.Either;
import com.atlassian.fugue.Option;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.type.EventDispatchOption;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.pocketknife.api.commons.error.AnError;
import com.atlassian.servicedesk.plugins.automation.api.execution.error.ThenActionError;
import com.atlassian.servicedesk.plugins.automation.api.execution.message.RuleMessage;
import com.atlassian.servicedesk.plugins.automation.api.execution.message.helper.IssueMessageHelper;
import com.netcracker.jira.custom.fields.types.component.CFTypesService;
import com.netcracker.jira.custom.fields.types.model.CustomFieldData;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.fugue.Either.right;

/**
 * Created by vija0516 on 10/3/2017.
 */
public class ChangeProjectRoleGroupThenAction implements com.atlassian.servicedesk.plugins.automation.spi.rulethen.ThenAction {
    static final String CUSTOM_FIELD_KEY = "customField";
    static final String GROUP_NAME = "group";

    private final CFTypesService cfTypesService;

    @ComponentImport
    @Autowired
    private IssueMessageHelper issueMessageHelper;

    public ChangeProjectRoleGroupThenAction(CFTypesService cfTypesService) {
        this.cfTypesService = cfTypesService;
    }


    @Override
    public Either<ThenActionError, RuleMessage> invoke(@Nonnull ThenActionParam thenActionParam) {
        final Either<AnError, Issue> issueEither = issueMessageHelper.getIssue(thenActionParam.getMessage());
        MutableIssue issue = (MutableIssue) issueEither.right().get();


        final Option<String> customFieldOpt = thenActionParam.getConfiguration().getData().getValue(CUSTOM_FIELD_KEY);
        final Option<String> groupOpt = thenActionParam.getConfiguration().getData().getValue(GROUP_NAME);


        ApplicationUser userStartedAutomation = thenActionParam.getUser();
        if (userStartedAutomation == null) {
            userStartedAutomation = issue.getReporterUser();
        }

        CustomFieldData cfData = cfTypesService.getCustomFieldById(customFieldOpt.get());

        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

        CustomField customField = customFieldManager.getCustomFieldObject(cfData.getFieldId());

        String userGroup = groupOpt.get();
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        Group group = groupManager.getGroup(userGroup);

        List<Group> groupList = new ArrayList<>();

        groupList.add(group);

        IssueManager issueManager = ComponentAccessor.getIssueManager();
        issue.setCustomFieldValue(customField, groupList);

        issueManager.updateIssue(userStartedAutomation, issue, EventDispatchOption.DO_NOT_DISPATCH, false);

        return right(thenActionParam.getMessage());
    }
}
