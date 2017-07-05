package com.quest.sample.service;

import org.springframework.stereotype.Service;

import com.quest.sample.util.GlobalConfiguration;

@Service("globalConfigurationService")
public class GlobalConfigurationServiceImpl implements GlobalConfigurationService {

	private static GlobalConfiguration globalConfiguration;
	
	public GlobalConfiguration loadGlobalConfiguration() {
		if (globalConfiguration == null) {
			globalConfiguration = new GlobalConfiguration();
		} 
		return globalConfiguration;
	}

	public void updateGlobalConfiguration(GlobalConfiguration configuration) {
		if (configuration != null) {
			if (configuration.getAuthToken() != null && !configuration.getAuthToken().isEmpty()) {
				globalConfiguration.setAuthToken(configuration.getAuthToken());
			}
			if (configuration.getPassword() != null && !configuration.getPassword().isEmpty()) {
				globalConfiguration.setPassword(configuration.getPassword());
			}
			if (configuration.getUsername() != null && !configuration.getUsername().isEmpty()) {
				globalConfiguration.setUsername(configuration.getUsername());
			}
			if (configuration.getServerPort() != null && !configuration.getServerPort().isEmpty()) {
				globalConfiguration.setServerPort(configuration.getServerPort());
			}
			if (configuration.getServerUrl() != null && !configuration.getServerUrl().isEmpty()) {
				globalConfiguration.setServerUrl(configuration.getServerUrl());
			}
			if (configuration.getSecurityLoginType() != null) {
				globalConfiguration.setSecurityLoginType(configuration.getSecurityLoginType());
			}
		}
	}

	@Override
	public void updateToken(String token) {
		globalConfiguration.setToken(token);
	}
}
