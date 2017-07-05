package com.quest.sample.util;

public class GlobalConfiguration {

	private String serverUrl;
	private String serverPort;
	private String authToken;
	private String username;
	private String password;
	private String token;
	private SecurityLoginType securityLoginType;
	
	public SecurityLoginType getSecurityLoginType() {
		return securityLoginType;
	}
	public void setSecurityLoginType(SecurityLoginType securityLoginType) {
		this.securityLoginType = securityLoginType;
	}
	public String getProtocol() {
		return "http";
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getApiVersion() {
		return "v1";
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
