package com.quest.sample.restful;

public class AlarmStatistics {

	private Double severity1;
	private Double severity2;
	private Double severity3;
	private Double severity4;
	private Double severity5;
	
	public AlarmStatistics() {
		severity1 = 0.0;
		severity2 = 0.0;
		severity3 = 0.0;
		severity4 = 0.0;
		severity5 = 0.0;
	}
	
	public void setSeverity(int severity, Double value) {
		if (severity == 1) {
			this.severity1 = value;
		} else if (severity == 2) {
			this.severity2 = value;
		} else if (severity == 3) {
			this.severity3 = value;
		} else if (severity == 4) {
			this.severity4 = value;
		} else if (severity == 5) {
			this.severity5 = value;
		}
	}
	
	public Double getSeverity1() {
		return severity1;
	}
	public void setSeverity1(Double severity1) {
		this.severity1 = severity1;
	}
	public Double getSeverity2() {
		return severity2;
	}
	public void setSeverity2(Double severity2) {
		this.severity2 = severity2;
	}
	public Double getSeverity3() {
		return severity3;
	}
	public void setSeverity3(Double severity3) {
		this.severity3 = severity3;
	}
	public Double getSeverity4() {
		return severity4;
	}
	public void setSeverity4(Double severity4) {
		this.severity4 = severity4;
	}
	public Double getSeverity5() {
		return severity5;
	}
	public void setSeverity5(Double severity5) {
		this.severity5 = severity5;
	}
}
