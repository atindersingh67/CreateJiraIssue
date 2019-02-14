package jiraBugRaise;

import java.io.IOException;
import java.net.URI;

import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import config.Constants;
import util.PropertyLoader;

public class Test {

	public static void main(String[] args) {
		URI jiraServerUri = URI.create(PropertyLoader.getProperty(Constants.JIRA_URL));
		AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

		AuthenticationHandler auth = new BasicHttpAuthenticationHandler(PropertyLoader.getProperty(Constants.JIRA_USER),
				PropertyLoader.getProperty(Constants.JIRA_PASSWORD));
		JiraRestClient restClient = factory.create(jiraServerUri, auth);
		IssueRestClient issueClient = restClient.getIssueClient();

		try {
			IssueInputBuilder iib = new IssueInputBuilder();
			iib.setProjectKey(PropertyLoader.getProperty(Constants.JIRA_PROJECT_KEY));
			iib.setSummary("Test Summary");
			iib.setIssueTypeId(1l);
			iib.setDescription("Test Description");

			iib.setAssigneeName(PropertyLoader.getProperty(Constants.JIRA_ASSIGNE));
			iib.setDueDate(new DateTime());

			iib.setFieldInput(
					new FieldInput("customfield_28740", ComplexIssueInputFieldValue.with("value", "Testing"))); // Injected
																												// PhaseRequired
			iib.setFieldInput(new FieldInput("customfield_28741", ComplexIssueInputFieldValue.with("value", "UAT"))); // Detected
																														// Phase
			iib.setFieldInput(
					new FieldInput("customfield_10023", ComplexIssueInputFieldValue.with("value", "To be Assigned")));// Defect
																														// Cause
			iib.setFieldInput(new FieldInput("customfield_10015", ComplexIssueInputFieldValue.with("value", "Medium"))); // Severity
			IssueInput issue = iib.build();
			BasicIssue issueObj = issueClient.createIssue(issue).claim();

			System.out.println("Issue " + issueObj.getKey() + " created successfully");
		} finally {
			try {
				restClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
