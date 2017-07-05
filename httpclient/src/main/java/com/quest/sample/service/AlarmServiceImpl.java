package com.quest.sample.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.sample.restful.AlarmConfiguration;
import com.quest.sample.restful.AlarmStatistics;
import com.quest.sample.restful.HttpRequestUtil;

@Service("alarmService")
public class AlarmServiceImpl implements AlarmService {

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	
	@Override
	public AlarmStatistics getSeverityStatistics(AlarmConfiguration alarmConfiguration) {
		Map<String, Integer> severityCount = new HashMap<String, Integer>();
		for (int i = 1; i <= 5; i++) {
			severityCount.put("severity" + i, 0);
		}
		
		AlarmStatistics statistics = new AlarmStatistics();
		String responseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()){
			@Override
			public String getPath() {
				return "/alarm/current";
			}}.get();
		if (responseText != null) {
			JSONObject response = new JSONObject(responseText);
			JSONArray datas = response.getJSONArray("data");
			for (int i = 0; i < datas.length(); i++) {
				JSONObject data = datas.getJSONObject(i);
				int severity = data.getInt("severity");
				boolean cleared = data.getBoolean("cleared");
				boolean acknowledged = data.getBoolean("acknowledged");
				boolean checkSign = false;
				//This is a sample
				if (severity == 0 || severity == 2 || severity == 4) {
					checkSign = acknowledged;
				} else {
					checkSign = cleared;
				}
				if (!checkSign) {
					severityCount.put("severity" + severity, severityCount.get("severity" + severity) + 1);
				}
			}
			for (int i = 1; i <= 5; i++) {
				int severityThreshold = alarmConfiguration.getThresholdBySeverity(i);
				statistics.setSeverity(i, (double) severityCount.get("severity" + i) / severityThreshold * 100);
			}
		}
		return statistics;
	}

	@Override
	public int acknowledge(int severity) {
		String responseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()){
			@Override
			public String getPath() {
				return "/alarm/current";
			}}.get();
		int count = 0;
		if (responseText != null) {
			JSONObject response = new JSONObject(responseText);
			JSONArray datas = response.getJSONArray("data");
			for (int i = 0; i < datas.length(); i++) {
				JSONObject data = datas.getJSONObject(i);
				if (data.getInt("severity") == severity) {
					String alarmId = data.getString("id");
					responseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()) {
						@Override
						public String getPath() {
							return "/alarm/ack/" + alarmId;
						}}.get();
					if (responseText == null) { //ack failed, still left
						count++;
					}
				}
			}
		}
		return count;
	}
	
	@Override
	public int clear(int severity) {
		String responseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()){
			@Override
			public String getPath() {
				return "/alarm/current";
			}}.get();
		int count = 0;
		if (responseText != null) {
			JSONObject response = new JSONObject(responseText);
			JSONArray datas = response.getJSONArray("data");
			for (int i = 0; i < datas.length(); i++) {
				JSONObject data = datas.getJSONObject(i);
				if (data.getInt("severity") == severity) {
					String alarmId = data.getString("id");
					responseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()) {
						@Override
						public String getPath() {
							return "/alarm/clear/" + alarmId;
						}}.get();
					if (responseText == null) { //clear failed, still left
						count++;
					}
				}
			}
		}
		return count;
	}
	
	
}
