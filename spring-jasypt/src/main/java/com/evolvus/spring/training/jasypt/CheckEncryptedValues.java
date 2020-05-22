package com.evolvus.spring.training.jasypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CheckEncryptedValues implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(CheckEncryptedValues.class);
	
	@Value("${sample.text}")
    private String simpleString;
	
	@Value("${sample.text.encrypted}")
    private String encryptedString;
    
	public void run(String... args) throws Exception {
        LOG.info("The simple string value is: {}", simpleString);
        LOG.info("The encrypted value is: {}", encryptedString);
	}

}
