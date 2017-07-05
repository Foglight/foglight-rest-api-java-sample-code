package com.quest.foglight.sample.rest.client;

public class RestContextBuilder {

	private final RestContext context = new RestContext();

	private RestContextBuilder() {
	}

	public RestContextBuilder configureTargetServer(String server, int port) {
		context.server = server;
		context.port = port;
		return this;
	}

	public RestContextBuilder authenticate(String username, String password) {
		context.username = username;
		context.password = password.toCharArray();
		return this;
	}

	public RestContextBuilder authenticate(String authToken) {
		context.authToken = authToken.toCharArray();
		return this;
	}

	public RestContext build() {
		return context;
	}

	public static class RestContext {
		private String username;
		private char[] password;
		/**
		 * The auth token generated from user management dashboard or foglight
		 * command line
		 */
		private char[] authToken;

		/**
		 * The foglight server name or ip
		 */
		private String server;
		private int port;
		private String protocol = "http";

		/**
		 * The token from login response, all the requests to REST services
		 * should carry this token in the http header
		 */
		private String accessToken;

		public static RestContextBuilder customize() {
			return new RestContextBuilder();
		}

		public String getBaseURL() {
			return protocol + "://" + server
					+ (port != 443 && port != 80 ? (":" + port) : "")
					+ "/api/v1";
		}

		public String getUsername() {
			return username;
		}

		public char[] getPassword() {
			return password;
		}

		public char[] getAuthToken() {
			return authToken;
		}

		public String getAccessToken() {
			return accessToken;
		}

		public void updateAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

	}

}
