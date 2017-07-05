package com.quest.sample.service;

import com.quest.sample.util.GlobalConfiguration;

public interface GlobalConfigurationService {
	GlobalConfiguration loadGlobalConfiguration();
	void updateGlobalConfiguration(GlobalConfiguration configuration);
	void updateToken(String token);
}
