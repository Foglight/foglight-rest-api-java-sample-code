package com.quest.foglight.sample.rest.client;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quest.foglight.sample.rest.client.RestContextBuilder.RestContext;

/**
 * A sample Rest client to consume Foglight REST service on top of Jersey client
 * libraries, for the Jersey client API, please refer to
 * https://jersey.java.net/documentation/latest/client.html
 *
 */
public class RestClient {

	private final RestContext context = buildRestContext();
	private final Client client = ClientBuilder.newClient();
	private final JsonParser parser = new JsonParser();

	public static void main(String[] args) throws Exception {
		RestClient client = new RestClient();
		client.login();
		JsonArray hosts = client.queryAllTopologyShells("Host");
		if (hosts.size() > 0) {
			JsonObject shell = hosts.get(0).getAsJsonObject();
			client.queryHostsByName(shell.get("name").getAsString());
		}
	}

	/**
	 * use credential or auth token to login, and get back another digital token
	 * which will expire after 30 minutes since last use of this token, it works
	 * exactly the same behavior as session id concept.
	 * 
	 * For the detail API usage, please refer to:
	 * 
	 * http://{fms}:{port}/api/doc/#api-Security-PostApiV1SecurityLogin
	 * 
	 */
	public void login() throws Exception {
		WebTarget target = client.target(context.getBaseURL()).path(
				"security/login");

		// prepare http post form
		Form form = new Form();
		if (context.getUsername() != null && context.getPassword() != null) {
			form.param("username", context.getUsername());
			form.param("pwd", new String(context.getPassword()));
		} else if (context.getAuthToken() != null) {
			form.param("authToken", new String(context.getAuthToken()));
		}

		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		String responseText = response.readEntity(String.class);
		log("login result: " + responseText);

		if (response.getStatus() != 200) {
			throw new Exception("login failed: " + response.getStatus());
		}

		JsonObject obj = (JsonObject) parser.parse(responseText);
		String token = obj.getAsJsonObject("data").get("token").getAsString();
		log("successfully acquired token:  " + token);
		context.updateAccessToken(token);
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
		invocationBuilder.header("Auth-Token", context.getAccessToken());

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
				.header("Auth-Token", context.getAccessToken())
				.post(Entity.json(requestBody.toString()));

		String responseText = response.readEntity(String.class);
		log("queryHostsByName (" + hostName + ") result: " + responseText);

		if (response.getStatus() != 200) {
			throw new Exception("query failed: " + response.getStatus());
		}

		JsonObject obj = (JsonObject) parser.parse(responseText);

		return obj.getAsJsonArray("data");
	}

	private RestContext buildRestContext() {
		RestContextBuilder builder = RestContext.customize()
				.configureTargetServer("10.30.169.9", 8080);
		// provide the credential to login
		builder.authenticate("apiuser", "apiuser@123");
		/*
		 * or comment out below line to use the auth token to login, the auth
		 * token could be generated from user management dashboard or foglight
		 * command line
		 */
		// builder.authenticate("0098a9522fb346:22a837897a339c73eacc7167");
		return builder.build();
	}

	private void log(String msg) {
		System.out.println(new Date() + " ---- " + msg);
	}
}
