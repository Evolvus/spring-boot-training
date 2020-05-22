package com.evolvus.spring.training.levelone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4000)
public class RunnerTwo implements CommandLineRunner {

	@Value("${sampletext}")
	private String someText;
	
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
       System.out.println("CommandLineTwo..hello" + someText);
	}

}
