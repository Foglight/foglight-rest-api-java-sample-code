package com.quest.sample.restful;

public class Utilization {

	private String hostId;
	private String hostName;
	private String viewId;
	private MemoryUtilization memoryUtilization;
	private CpuUtilization cpuUtilization;
	private NetworkUtilization networkUtilization;
	
	public Utilization(String hostId, String hostName, MemoryUtilization memoryUtilization, CpuUtilization cpuUtilization, NetworkUtilization networkUtilization) {
		this.hostId = hostId;
		this.hostName = hostName;
		this.viewId = refactorName(hostName);
		this.memoryUtilization = memoryUtilization;
		this.cpuUtilization = cpuUtilization;
		this.networkUtilization = networkUtilization;
	}
	
	public Utilization(String hostId, String hostName) {
		this.hostId = hostId;
		this.hostName = hostName;
		this.viewId = refactorName(hostName);
	}
	
	private String refactorName(String name) {
		return "util" + name.replaceAll("\\W", ""); 
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getHostId() {
		return hostId;
	}
	
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public MemoryUtilization getMemoryUtilization() {
		return memoryUtilization;
	}

	public void setMemoryUtilization(MemoryUtilization memoryUtilization) {
		this.memoryUtilization = memoryUtilization;
	}

	public CpuUtilization getCpuUtilization() {
		return cpuUtilization;
	}

	public void setCpuUtilization(CpuUtilization cpuUtilization) {
		this.cpuUtilization = cpuUtilization;
	}

	public NetworkUtilization getNetworkUtilization() {
		return networkUtilization;
	}

	public void setNetworkUtilization(NetworkUtilization networkUtilization) {
		this.networkUtilization = networkUtilization;
	}
	
}
