package com.quest.foglight.sample.rest.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.JsonObject;

/**
 * A sample Rest client to consume Foglight REST service on top of Jersey client
 * libraries, for the Jersey client API, please refer to
 * https://jersey.java.net/documentation/latest/client.html
 *
 */
public class SecurityRestClient extends AbstractRestClient {

	public static void main(String[] args) throws Exception {
		SecurityRestClient client = new SecurityRestClient();
		client.login();
		client.logout();
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
		String token = obj.getAsJsonObject("data").get("access-token").getAsString();
		log("successfully acquired access token:  " + token);
		context.updateAccessToken(token);
	}
	
	/**
	 * logout
	 * 
	 * @throws Exception
	 */
	public void logout() throws Exception {
		WebTarget target = client.target(context.getBaseURL()).path(
				"security/logout");
		Invocation.Builder invocationBuilder = target
				.request(MediaType.APPLICATION_JSON_TYPE);
		// set the token from login service response into header
		invocationBuilder.header("Access-Token", context.getAccessToken());
		Response response = invocationBuilder.get();
		
		String responseText = response.readEntity(String.class);

		if (response.getStatus() != 200) {
			throw new Exception("query failed: " + response.getStatus());
		} else {
			log("logout result: " + responseText);
		}
	}
	
	

	
}
