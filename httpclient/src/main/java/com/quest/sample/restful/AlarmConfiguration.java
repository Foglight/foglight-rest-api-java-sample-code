package com.quest.sample.restful;

public class AlarmConfiguration {

	private Integer severity1Threshold;
	private Integer severity2Threshold;
	private Integer severity3Threshold;
	private Integer severity4Threshold;
	private Integer severity5Threshold;
	
	public AlarmConfiguration() {
		this.severity1Threshold = 500;
		this.severity2Threshold = 500;
		this.severity3Threshold = 500;
		this.severity4Threshold = 500;
		this.severity5Threshold = 500;
	}
	
	public Integer getThresholdBySeverity(Integer severity) {
		if (severity == 1) {
			return this.severity1Threshold;
		} else if (severity == 2) {
			return this.severity2Threshold;
		} else if (severity == 3) {
			return this.severity3Threshold;
		} else if (severity == 4) {
			return this.severity4Threshold;
		} else if (severity == 5) {
			return this.severity5Threshold;
		} else {
			return Integer.MAX_VALUE;
		}
	}
	public Integer getSeverity1Threshold() {
		return severity1Threshold;
	}
	public void setSeverity1Threshold(Integer severity1Threshold) {
		this.severity1Threshold = severity1Threshold;
	}
	public Integer getSeverity2Threshold() {
		return severity2Threshold;
	}
	public void setSeverity2Threshold(Integer severity2Threshold) {
		this.severity2Threshold = severity2Threshold;
	}
	public Integer getSeverity3Threshold() {
		return severity3Threshold;
	}
	public void setSeverity3Threshold(Integer severity3Threshold) {
		this.severity3Threshold = severity3Threshold;
	}
	public Integer getSeverity4Threshold() {
		return severity4Threshold;
	}
	public void setSeverity4Threshold(Integer severity4Threshold) {
		this.severity4Threshold = severity4Threshold;
	}
	public Integer getSeverity5Threshold() {
		return severity5Threshold;
	}
	public void setSeverity5Threshold(Integer severity5Threshold) {
		this.severity5Threshold = severity5Threshold;
	}
}
