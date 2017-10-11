package com.quest.foglight.sample.rest.client;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RuleRestClient extends SecurityRestClient {

	public static void main(String[] args) throws Exception {
		RuleRestClient client = new RuleRestClient();
		client.login();
		client.queryAllRules();
		client.logout();
	}
	
	/**
	 * query all rules
	 * @return
	 * @throws Exception
	 */
	public JsonArray queryAllRules() throws Exception {
		WebTarget target = client.target(context.getBaseURL()).path(
				"rule/allRules");

		Invocation.Builder invocationBuilder = target
				.request(MediaType.APPLICATION_JSON_TYPE);
		// set the token from login service response into header
		invocationBuilder.header("Access-Token", context.getAccessToken());
		Response response = invocationBuilder.get();

		String responseText = response.readEntity(String.class);
		log("queryAllRules result: " + responseText);

		if (response.getStatus() != 200) {
			throw new Exception("query failed: " + response.getStatus());
		}

		JsonObject obj = (JsonObject) parser.parse(responseText);

		return obj.getAsJsonArray("data");
	}
}
