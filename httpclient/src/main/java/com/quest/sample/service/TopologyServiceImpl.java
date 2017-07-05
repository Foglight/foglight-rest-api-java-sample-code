package com.quest.sample.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.sample.restful.CpuUtilization;
import com.quest.sample.restful.HttpRequestUtil;
import com.quest.sample.restful.MemoryUtilization;
import com.quest.sample.restful.NetworkUtilization;
import com.quest.sample.restful.Utilization;

@Service("topologyService")
public class TopologyServiceImpl implements TopologyService {

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	
	private JSONObject queryHosts() {
		try {
			String responseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()){
				@Override
				public String getPath() {
					return "/topology/query";
				}
				@Override
				public Object getEntity() {
					return "{\"queryText\":\"!Host\"}";
				}}.post();
			if (responseText != null && !responseText.isEmpty()) {
				return new JSONObject(responseText);
			}
		} catch (Exception e) {
			//Unknow host
		}
		return null;
	}
	
	private Map<String, MemoryUtilization> getMemoryUtilization() {
		Map<String, MemoryUtilization> memoryUtilization = new HashMap<String, MemoryUtilization>();
		try {
			JSONObject responseJson = queryHosts();
			if (responseJson != null) {
				JSONArray hosts = responseJson.getJSONArray("data");
				long endTime = new Date().getTime();
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(new Date(endTime));
				startCal.add(Calendar.MONTH, -1);
				long startTime = startCal.getTime().getTime();
				String memoryJsonStrs = "{\"includes\":[{\"ids\":[";
				for (int i = 0; i < hosts.length(); i++) {
					JSONObject host = hosts.getJSONObject(i);
					try {
						JSONObject memory = host.getJSONObject("properties").getJSONObject("memory");
						if (memory != null) {
							String hostId = host.getString("uniqueId");
							String hostName = host.getString("name");
							memoryUtilization.put(hostId + ":" + hostName, new MemoryUtilization(memory.getString("uniqueId")));
							memoryJsonStrs += "\"" + memory.getString("uniqueId") + "\"";
							if (i < (hosts.length() - 1)) {
								memoryJsonStrs += ",";
							}
						}
					} catch (Exception e) {
						//No memory found for this host
					}
				}
				if (memoryJsonStrs.endsWith(",")) {
					memoryJsonStrs = memoryJsonStrs.substring(0, memoryJsonStrs.length()-1);
				}
				memoryJsonStrs += "],\"observationName\":\"utilization\"}],\"startTime\":" + startTime + ",\"endTime\":" + endTime + ",\"retrievalType\":\"AGGREGATE_AND_LAST\"}";
				final String entityStrs = memoryJsonStrs;
				String memoryResponseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()){
					@Override
					public String getPath() {
						return "/topology/batchQuery";
					}
					@Override
					public Object getEntity() {
						return entityStrs;
					}}.post();
				if (memoryResponseText != null && !memoryResponseText.isEmpty()) {
					JSONObject memoryReponseJson = new JSONObject(memoryResponseText);
					JSONObject memories = memoryReponseJson.getJSONObject("data").getJSONObject("lastValues");
					for (String host : memoryUtilization.keySet()) {
						MemoryUtilization mu = memoryUtilization.get(host);
						JSONObject memory = memories.getJSONObject(mu.getId() + ":utilization");
						mu.setUtilization(memory.getJSONObject("value").getDouble("avg"));
					}
				}
			}
		} catch (Exception e) {
			//NO OUTPUT
		}
		return memoryUtilization;
	}

	private Map<String, CpuUtilization> getCpuUtilization() {
		Map<String, CpuUtilization> cpuUtilization = new HashMap<String, CpuUtilization>();
		try {
		JSONObject responseJson = queryHosts();
		if (responseJson != null) {
			JSONArray hosts = responseJson.getJSONArray("data");
			long endTime = new Date().getTime();
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(new Date(endTime));
			startCal.add(Calendar.MONTH, -1);
			long startTime = startCal.getTime().getTime();
			String cpuJsonStrs = "{\"includes\":[{\"ids\":[";
			for (int i = 0; i < hosts.length(); i++) {
				JSONObject host = hosts.getJSONObject(i);
				try {
					JSONObject cpu = host.getJSONObject("properties").getJSONObject("cpus");
					if (cpu != null) {
						String hostId = host.getString("uniqueId");
						String hostName = host.getString("name");
						cpuUtilization.put(hostId + ":" + hostName, new CpuUtilization(cpu.getString("uniqueId")));
						cpuJsonStrs += "\"" + cpu.getString("uniqueId") + "\"";
						if (i < (hosts.length() - 1)) {
							cpuJsonStrs += ",";
						}
					}
				} catch (Exception e) {
					//NO cpu or related properties found
				}
			}
			if (cpuJsonStrs.endsWith(",")) {
				cpuJsonStrs = cpuJsonStrs.substring(0, cpuJsonStrs.length()-1);
			}
			cpuJsonStrs += "],\"observationName\":\"utilization\"}],\"startTime\":" + startTime + ",\"endTime\":" + endTime + ",\"retrievalType\":\"AGGREGATE_AND_LAST\"}";
			final String entityStrs = cpuJsonStrs;
			String memoryResponseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()){
				@Override
				public String getPath() {
					return "/topology/batchQuery";
				}
				@Override
				public Object getEntity() {
					return entityStrs;
				}}.post();
			if (memoryResponseText != null && !memoryResponseText.isEmpty()) {
				JSONObject memoryReponseJson = new JSONObject(memoryResponseText);
				JSONObject memories = memoryReponseJson.getJSONObject("data").getJSONObject("lastValues");
				for (String host : cpuUtilization.keySet()) {
					CpuUtilization cu = cpuUtilization.get(host);
					JSONObject memory = memories.getJSONObject(cu.getId() + ":utilization");
					cu.setUtilization(memory.getJSONObject("value").getDouble("avg"));
				}
			}
		}
		} catch (Exception e) {}
		return cpuUtilization;
	}

	private Map<String, NetworkUtilization> getNetworkUtilization() {
		Map<String, NetworkUtilization> networkUtilization = new HashMap<String, NetworkUtilization>();
		try {
		JSONObject responseJson = queryHosts();
		if (responseJson != null) {
			JSONArray hosts = responseJson.getJSONArray("data");
			long endTime = new Date().getTime();
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(new Date(endTime));
			startCal.add(Calendar.MONTH, -1);
			long startTime = startCal.getTime().getTime();
			String networkJsonStrs = "{\"includes\":[{\"ids\":[";
			for (int i = 0; i < hosts.length(); i++) {
				JSONObject host = hosts.getJSONObject(i);
				try {
					JSONObject network = host.getJSONObject("properties").getJSONObject("network");
					if (network != null) {
						String hostId = host.getString("uniqueId");
						String hostName = host.getString("name");
						networkUtilization.put(hostId + ":" + hostName, new NetworkUtilization(network.getString("uniqueId")));
						networkJsonStrs += "\"" + network.getString("uniqueId") + "\"";
						if (i < (hosts.length() - 1)) {
							networkJsonStrs += ",";
						}
					}
				} catch (Exception e) {
					//NO network or related properties found
				}
			}
			if (networkJsonStrs.endsWith(",")) {
				networkJsonStrs = networkJsonStrs.substring(0, networkJsonStrs.length()-1);
			}
			networkJsonStrs += "],\"observationName\":\"utilization\"}],\"startTime\":" + startTime + ",\"endTime\":" + endTime + ",\"retrievalType\":\"AGGREGATE_AND_LAST\"}";
			final String entityStrs = networkJsonStrs;
			String networkResponseText = new HttpRequestUtil(globalConfigurationService.loadGlobalConfiguration()){
				@Override
				public String getPath() {
					return "/topology/batchQuery";
				}
				@Override
				public Object getEntity() {
					return entityStrs;
				}}.post();
			if (networkResponseText != null && !networkResponseText.isEmpty()) {
				JSONObject memoryReponseJson = new JSONObject(networkResponseText);
				JSONObject memories = memoryReponseJson.getJSONObject("data").getJSONObject("lastValues");
				for (String host : networkUtilization.keySet()) {
					NetworkUtilization nu = networkUtilization.get(host);
					JSONObject memory = memories.getJSONObject(nu.getId() + ":utilization");
					nu.setUtilization(memory.getJSONObject("value").getDouble("avg"));
				}
			}
		}
		} catch (Exception e) {}
		return networkUtilization;
	}

	@Override
	public Map<String, Utilization> getUtilizations() {
		Map<String, Utilization> utilizations = new HashMap<String, Utilization>();
		Map<String, MemoryUtilization> memoryUtilizations = getMemoryUtilization();
		Map<String, CpuUtilization> cpuUtilizations = getCpuUtilization();
		Map<String, NetworkUtilization> networkUtilizations = getNetworkUtilization();
		for (String key : memoryUtilizations.keySet()) {
			MemoryUtilization mu = memoryUtilizations.get(key);
			CpuUtilization cu = cpuUtilizations.get(key);
			NetworkUtilization nu = networkUtilizations.get(key);
			String hostId = key.split(":")[0];
			String hostName = key.split(":")[1];
			utilizations.put(key, new Utilization(hostId, hostName, mu, cu, nu));
		}
		for (String key : cpuUtilizations.keySet()) {
			if (!utilizations.containsKey(key)) {
				CpuUtilization cu = cpuUtilizations.get(key);
				NetworkUtilization nu = networkUtilizations.get(key);
				String hostId = key.split(":")[0];
				String hostName = key.split(":")[1];
				utilizations.put(key, new Utilization(hostId, hostName, new MemoryUtilization(key), cu, nu));
			}
		}
		for (String key : networkUtilizations.keySet()) {
			if (!utilizations.containsKey(key)) {
				NetworkUtilization nu = networkUtilizations.get(key);
				String hostId = key.split(":")[0];
				String hostName = key.split(":")[1];
				utilizations.put(key, new Utilization(hostId, hostName, new MemoryUtilization(key), new CpuUtilization(key), nu));
			}
		}
		return utilizations;
	}

	
	
}
