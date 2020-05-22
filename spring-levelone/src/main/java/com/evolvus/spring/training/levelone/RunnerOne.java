package com.evolvus.spring.training.levelone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4000)
public class RunnerOne implements CommandLineRunner {

	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
       System.out.println("CommandLineOne..hello");
	}

}
