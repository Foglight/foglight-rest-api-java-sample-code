package com.quest.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quest.sample.service.GlobalConfigurationService;
import com.quest.sample.util.GlobalConfiguration;
import com.quest.sample.util.SecurityLoginType;

@Controller
public class MainController {

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	
	@RequestMapping(value = {"/", "/globalConfiguration"}, method = RequestMethod.GET)
    public String home(@ModelAttribute("errorMessage") String errorMessage,
			@ModelAttribute("successMessage") String successMessage,
			Model model) {
		model.addAttribute("globalConfiguration", globalConfigurationService.loadGlobalConfiguration());
		model.addAttribute("securityLoginTypes", SecurityLoginType.values());
		if (!StringUtils.isEmpty(errorMessage)) {
			model.addAttribute("errorMessage", errorMessage);
		}
		if (!StringUtils.isEmpty(successMessage)) {
			model.addAttribute("successMessage", successMessage);
		}
        return "main";
    }
	
	@RequestMapping(value = "/globalConfiguration/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("globalConfiguration") GlobalConfiguration globalConfiguration,
            Model model, 
            final RedirectAttributes redirectAttributes) {
		try {
			globalConfigurationService.updateGlobalConfiguration(globalConfiguration);
			redirectAttributes.addFlashAttribute("successMessage", "Global configuration updated successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Global configuration updated failed!");
		}
		return "redirect:/";
	}
	
}
