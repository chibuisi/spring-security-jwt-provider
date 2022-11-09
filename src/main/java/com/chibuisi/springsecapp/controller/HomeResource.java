package com.chibuisi.springsecapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
public class HomeResource {
	
	private Logger log = Logger.getLogger("HomeResource");

	@RequestMapping({"/home","/index", "/"})
	public String home() {
		return "index";
	}
	
	@RequestMapping("/admin")
	public String admin() {
//		Log.info("Admin logged in");
		return "Welcome Admin";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "Welcome User";
	}
	
	@RequestMapping("/manager")
	public String manager() {
		return "Welcome Manager";
	}

}
