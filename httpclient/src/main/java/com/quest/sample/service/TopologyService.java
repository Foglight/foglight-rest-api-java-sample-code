package com.quest.sample.service;

import java.util.Map;

import com.quest.sample.restful.Utilization;

public interface TopologyService {

	Map<String, Utilization> getUtilizations();
}
