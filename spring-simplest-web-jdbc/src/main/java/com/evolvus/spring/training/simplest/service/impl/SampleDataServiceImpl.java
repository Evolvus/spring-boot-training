package com.evolvus.spring.training.simplest.service.impl;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.evolvus.spring.training.simplest.dao.SampleDataDao;
import com.evolvus.spring.training.simplest.domain.SampleData;
import com.evolvus.spring.training.simplest.service.SampleDataService;

@Configuration
public class SampleDataServiceImpl implements SampleDataService {

	private SampleDataDao sampleDataDao;
	
	public SampleDataServiceImpl(SampleDataDao sampleDataDao) {
		this.sampleDataDao = sampleDataDao;
	}
	
	public List<SampleData> getAll() {
		return sampleDataDao.getAll();
	}

}
