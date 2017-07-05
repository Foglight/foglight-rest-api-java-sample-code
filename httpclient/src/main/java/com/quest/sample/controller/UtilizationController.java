package com.quest.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.quest.sample.service.TopologyService;

@Controller
@RequestMapping(value = "/utilization")
public class UtilizationController {
	
	@Autowired
	private TopologyService topologyService;

	@RequestMapping(method = RequestMethod.GET)
    public String home(@ModelAttribute("errorMessage") String errorMessage,
			@ModelAttribute("successMessage") String successMessage,
			Model model) {
		model.addAttribute("utilizations", topologyService.getUtilizations());
		if (!StringUtils.isEmpty(errorMessage)) {
			model.addAttribute("errorMessage", errorMessage);
		}
		if (!StringUtils.isEmpty(successMessage)) {
			model.addAttribute("successMessage", successMessage);
		}
		return "utilization/utilization";
	}
	
}
