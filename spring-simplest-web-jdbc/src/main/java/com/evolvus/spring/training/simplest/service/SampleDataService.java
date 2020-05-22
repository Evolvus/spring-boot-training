package com.evolvus.spring.training.simplest.service;

import java.util.List;

import com.evolvus.spring.training.simplest.domain.SampleData;

public interface SampleDataService {
    List<SampleData> getAll();
}
