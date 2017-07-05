package com.quest.sample.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quest.sample.restful.AlarmConfiguration;
import com.quest.sample.restful.AlarmStatistics;
import com.quest.sample.service.AlarmService;

@Controller
@RequestMapping(value = "/alarm")
public class AlarmController {

	private static AlarmConfiguration alarmConfiguration;
	@Autowired
	private AlarmService alarmService;
	
	@RequestMapping(method = RequestMethod.GET)
    public String home(@ModelAttribute("errorMessage") String errorMessage,
			@ModelAttribute("successMessage") String successMessage,
			@ModelAttribute("alarmConfiguration") AlarmConfiguration alarmConfiguration,
			@ModelAttribute("alarmSeverityStatistics") AlarmStatistics alarmSeverityStatistics,
			Model model) {
		if (alarmConfiguration == null) {
			alarmConfiguration = new AlarmConfiguration();
		}
		if (alarmSeverityStatistics == null) {
			alarmSeverityStatistics = new AlarmStatistics();
		}
		model.addAttribute("alarmConfiguration", alarmConfiguration);
		model.addAttribute("alarmSeverityStatistics", alarmSeverityStatistics);
		if (!StringUtils.isEmpty(errorMessage)) {
			model.addAttribute("errorMessage", errorMessage);
		}
		if (!StringUtils.isEmpty(successMessage)) {
			model.addAttribute("successMessage", successMessage);
		}
		return "alarm/alarm";
	}
	
	@RequestMapping(value = "/configure", method = RequestMethod.POST)
    public String update(@ModelAttribute("alarmConfiguration") AlarmConfiguration alarmConfiguration,
            Model model, 
            final RedirectAttributes redirectAttributes) {
		try {
			AlarmController.alarmConfiguration = alarmConfiguration;
			redirectAttributes.addFlashAttribute("alarmConfiguration", alarmConfiguration);
			redirectAttributes.addFlashAttribute("alarmSeverityStatistics", alarmService.getSeverityStatistics(alarmConfiguration));
			redirectAttributes.addFlashAttribute("successMessage", "Alarm sample updated successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Alarm sample updated failed!");
		}
		return "redirect:/alarm";
	}
	
	@RequestMapping(value = "/ack/{severity}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String ack(@PathVariable String severity) {
		JSONObject response = new JSONObject();
		if (severity != null && !severity.isEmpty()) {
			try {
				int newCount = alarmService.acknowledge(Integer.parseInt(severity));
				int configure = alarmConfiguration.getThresholdBySeverity(Integer.parseInt(severity));
				response.append("count", String.valueOf((double) newCount / configure * 100));
			} catch (NumberFormatException e) {
			}
		}
		return response.toString();
	}
	
	@RequestMapping(value = "/clear/{severity}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String clear(@PathVariable String severity) {
		JSONObject response = new JSONObject();
		if (severity != null && !severity.isEmpty()) {
			try {
				int newCount = alarmService.clear(Integer.parseInt(severity));
				int configure = alarmConfiguration.getThresholdBySeverity(Integer.parseInt(severity));
				response.append("count", String.valueOf((double) newCount / configure * 100));
			} catch (NumberFormatException e) {
			}
		}
		return response.toString();
	}
}
