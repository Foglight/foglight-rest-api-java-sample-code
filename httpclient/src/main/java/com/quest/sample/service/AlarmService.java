package com.quest.sample.service;

import com.quest.sample.restful.AlarmConfiguration;
import com.quest.sample.restful.AlarmStatistics;

public interface AlarmService {
	AlarmStatistics getSeverityStatistics(AlarmConfiguration alarmConfiguration);
	int acknowledge(int severity);
	int clear(int severity);
}
