package com.quest.foglight.sample.rest.client;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.google.gson.JsonParser;
import com.quest.foglight.sample.rest.client.RestContextBuilder.RestContext;

public class AbstractRestClient {

	protected static final String HOST = "10.30.154.41";
	protected static final int PORT = 8080;
	protected static final String USERNAME = "foglight";
	protected static final String PASSWORD = "foglight";
	protected static final String AUTHTOKEN = null;
	
	protected final RestContext context = buildRestContext();
	protected final Client client = ClientBuilder.newClient();
	protected final JsonParser parser = new JsonParser();
	
	protected RestContext buildRestContext() {
		RestContextBuilder builder = RestContext.customize()
				.configureTargetServer(HOST, PORT);
		// provide the credential to login, consider AUTHTOKEN first
		if (AUTHTOKEN != null && AUTHTOKEN.trim().length() > 0) {
			builder.authenticate(AUTHTOKEN);
		} else {
			builder.authenticate(USERNAME, PASSWORD);
		}
		return builder.build();
	}

	protected void log(String msg) {
		System.out.println(new Date() + " ---- " + msg);
	}
}
