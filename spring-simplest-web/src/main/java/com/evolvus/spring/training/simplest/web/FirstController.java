package com.evolvus.spring.training.simplest.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FirstController {

	@ResponseBody
	@RequestMapping("/hello")
	public String helloWorld() {
		return "Hello World";
	}
}
