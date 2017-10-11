package com.quest.foglight.sample.rest.client;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PushDataRestClient extends SecurityRestClient {

	public static void main(String[] args) throws Exception {
		PushDataRestClient client = new PushDataRestClient();
		String topologyObjectId = null;
		String ruleId = null;
		
		TopologyRestClient topologyClient = new TopologyRestClient();
		topologyClient.login();
		JsonArray hosts = topologyClient.queryAllTopologyShells("Host");
		if (hosts.size() > 0) {
			JsonObject shell = hosts.get(0).getAsJsonObject();
			topologyObjectId = shell.get("uniqueId").getAsString();
		}
		topologyClient.logout();
		
		RuleRestClient ruleClient = new RuleRestClient();
		ruleClient.login();
		JsonArray rules = ruleClient.queryAllRules();
		if (rules.size() > 0) {
			JsonObject shell = rules.get(0).getAsJsonObject();
			ruleId = shell.get("id").getAsString();
		}
		ruleClient.logout();
		
		client.login();
		/*
		 * test push alarm
		 */
		client.pushAlarm(topologyObjectId, ruleId);
		
		/*
		 * test push data
		 */
		client.pushData();
		client.logout();
	}
	
	public void pushAlarm(String topologyObjectId, String ruleId) throws Exception {
		
		if (topologyObjectId == null) {
			log("Invalid parameter: Topology object id cannot be empty!");
		}
		
		WebTarget target = client.target(context.getBaseURL()).path(
				"alarm/pushAlarm");
		//{"alarmMessage":"This is a test alarm message from postman", "severity":"2", "topologyObjectId":"94e27d92-08a5-4c28-9a90-8856a7689105"}
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("alarmMessage", "This is a test alarm message from client");
		requestBody.addProperty("severity", 2);
		requestBody.addProperty("topologyObjectId", topologyObjectId);
		if (ruleId != null) {
			requestBody.addProperty("ruleId", ruleId);
		}

		log("pushAlarm request body: " + requestBody.toString());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.header("Access-Token", context.getAccessToken())
				.post(Entity.json(requestBody.toString()));

		String responseText = response.readEntity(String.class);
		log("pushAlarm result: " + responseText);

		if (response.getStatus() != 200) {
			throw new Exception("pushAlarm failed: " + response.getStatus());
		}
	}
	
	/*
	 * Before call the pushData, you need to add the following topology type to Foglight
	 * 
		<!DOCTYPE types SYSTEM "../dtd/topology-types.dtd">
		<types>
		    <type name='PushDataTest1' extends='TopologyObject'>
				<property name='name' type='String' is-identity='true'/>
				<property name='testMetric' type='Metric' is-containment='true' unit-name='count' />
				<property name='testObservations' type='TestObservation1' is-containment='true' unit-name='count' />
			</type>
			
			<type name='TestObservation1' extends='ComplexObservation'>
				<property name='current' type='TestObservationValue1' is-many='false' is-containment='true' />
				<property name='latest' type='TestObservationValue1' is-many='false' is-containment='true' />
				<property name='history' type='TestObservationValue1' is-many='true' is-containment='true' />
			</type>
			
			<type name='TestObservationValue1' extends='ObservedValue'>
				<property name='value' type='TestObject1' is-containment='true'/>
			</type>
			
			<type name="TestObject1" extends="DataObject">
				<property name='testName' type='String' />
				<property name='createDate' type='Date' />
				<property name='createTime' type='Double' />
			</type>
		</types>
	 */
	public void pushData() throws Exception {
		WebTarget target = client.target(context.getBaseURL()).path(
				"topology/pushData");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -5);
		PushDataTest pushDataTest = new PushDataTest(
				Arrays.asList(
						new PushDataTestData(
								"PushDataTest1",
								new PushDataTestProperties(
										"TestData1",
										90.0,
										new PushDataTestObservation(
												"testObservation1",
												new Date().getTime(),
												new Date().getTime())))),
				cal.getTime().getTime(),
				new Date().getTime());
		Gson gson = new Gson();
		
		log("pushData request body: " + gson.toJson(pushDataTest, PushDataTest.class));
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.header("Access-Token", context.getAccessToken())
				.post(Entity.json(gson.toJson(pushDataTest)));
		
		String responseText = response.readEntity(String.class);
		log("pushData result: " + responseText);

		if (response.getStatus() != 200) {
			throw new Exception("pushData failed: " + response.getStatus());
		}
	}
	
	class PushDataTest {
		private List<PushDataTestData> data;
		private long startTime;
		private long endTime;
		public PushDataTest (List<PushDataTestData> data, long startTime, long endTime) {
			this.data = data;
			this.startTime = startTime;
			this.endTime = endTime;
		}
		public List<PushDataTestData> getData() {
			return data;
		}
		public void setData(List<PushDataTestData> data) {
			this.data = data;
		}
		public long getStartTime() {
			return startTime;
		}
		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}
		public long getEndTime() {
			return endTime;
		}
		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}
	}
	
	class PushDataTestData {
		private String typeName;
		private PushDataTestProperties properties;
		public PushDataTestData (String typeName, PushDataTestProperties properties) {
			this.typeName = typeName;
			this.properties = properties;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public PushDataTestProperties getProperties() {
			return properties;
		}
		public void setProperties(PushDataTestProperties properties) {
			this.properties = properties;
		}
	}
	
	class PushDataTestProperties {
		private String name;
		private double testMetric;
		private PushDataTestObservation testObservations;
		public PushDataTestProperties(String name, double testMetric, PushDataTestObservation testObservations) {
			this.name = name;
			this.testMetric = testMetric;
			this.testObservations = testObservations;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getTestMetric() {
			return testMetric;
		}
		public void setTestMetric(double testMetric) {
			this.testMetric = testMetric;
		}
		public PushDataTestObservation getTestObservations() {
			return testObservations;
		}
		public void setTestObservations(PushDataTestObservation testObservations) {
			this.testObservations = testObservations;
		}
	}
	
	class PushDataTestObservation {
		private String testName;
		private long createDate;
		private long createTime;
		public PushDataTestObservation(String testName, long createDate, long createTime) {
			this.testName = testName;
			this.createDate = createDate;
			this.createTime = createTime;
		}
		public String getTestName() {
			return testName;
		}
		public void setTestName(String testName) {
			this.testName = testName;
		}
		public long getCreateDate() {
			return createDate;
		}
		public void setCreateDate(long createDate) {
			this.createDate = createDate;
		}
		public long getCreateTime() {
			return createTime;
		}
		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}
	}
}
