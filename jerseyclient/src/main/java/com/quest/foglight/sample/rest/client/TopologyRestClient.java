package com.quest.foglight.sample.rest.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TopologyRestClient extends SecurityRestClient {

	public static void main(String[] args) throws Exception {
		TopologyRestClient client = new TopologyRestClient();
		client.login();
		JsonArray hosts = client.queryAllTopologyShells("Host");
		if (hosts.size() > 0) {
			JsonObject shell = hosts.get(0).getAsJsonObject();
			client.queryHostsByName(shell.get("name").getAsString());
		}
		client.logout();
	}
	
	/**
	 * Query all instances with limited properties ( unique id and the host
	 * name)
	 * 
	 * The related API doc is:
	 * 
	 * http://{fms}:{port}/api/doc/#api-TopologyType-
	 * GetApiV1TypeTypenameInstances
	 * 
	 * @throws Exception
	 */
	public JsonArray queryAllTopologyShells(String topologyType)
			throws Exception {
		WebTarget target = client.target(context.getBaseURL()).path(
				"type/" + topologyType + "/instances");

		// prepare http parameters
		target = target.queryParam("maxObjectCount", "1000");
		target = target.queryParam("maxDepth", "0");
		target = target.queryParam("includePropertyPatterns", "uniqueId,name");

		Invocation.Builder invocationBuilder = target
				.request(MediaType.APPLICATION_JSON_TYPE);
		// set the token from login service response into header
		invocationBuilder.header("Access-Token", context.getAccessToken());

		Response response = invocationBuilder.get();

		String responseText = response.readEntity(String.class);
		log("queryAllTopologyShells (" + topologyType + ") result: "
				+ responseText);

		if (response.getStatus() != 200) {
			throw new Exception("query failed: " + response.getStatus());
		}

		JsonObject obj = (JsonObject) parser.parse(responseText);

		return obj.getAsJsonArray("data");
	}

	/**
	 * Query host by name and get the cpu utilization metric.
	 * 
	 * The related API doc is:
	 * http://{fms}:{port}/api/doc/#api-Topology-PostApiV1TopologyQuery
	 * 
	 */
	public JsonArray queryHostsByName(String hostName) throws Exception {
		WebTarget target = client.target(context.getBaseURL()).path(
				"topology/query");

		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("queryText", "!Host where name=:name");
		JsonObject param = new JsonObject();
		param.addProperty("name", "name");
		param.addProperty("value", hostName);
		JsonArray params = new JsonArray();
		params.add(param);
		requestBody.add("params", params);
		requestBody.addProperty("queryTopologyObjects", true);

		log("queryHostsByName request body: " + requestBody.toString());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.header("Access-Token", context.getAccessToken())
				.post(Entity.json(requestBody.toString()));

		String responseText = response.readEntity(String.class);
		log("queryHostsByName (" + hostName + ") result: " + responseText);

		if (response.getStatus() != 200) {
			throw new Exception("query failed: " + response.getStatus());
		}

		JsonObject obj = (JsonObject) parser.parse(responseText);

		return obj.getAsJsonArray("data");
	}
}
