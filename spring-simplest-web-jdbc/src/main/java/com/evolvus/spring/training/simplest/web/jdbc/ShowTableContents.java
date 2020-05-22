package com.evolvus.spring.training.simplest.web.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.evolvus.spring.training.simplest.domain.SampleData;
import com.evolvus.spring.training.simplest.service.SampleDataService;

//@Component
public class ShowTableContents implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ShowTableContents.class);

	@Autowired
	private SampleDataService sampleDataService;
	
	
	public void run(String... args) throws Exception {
		List<SampleData> list = sampleDataService.getAll();
		for(SampleData sd: list) {
			LOG.info("Id: {}, Fistname: {}, LastName: {}", sd.getId(), sd.getFirstName(), sd.getLastName());
		}

	}

}
