package com.evolvus.spring.training.simplest.dao;

import java.util.List;

import com.evolvus.spring.training.simplest.domain.SampleData;

public interface SampleDataDao {
    List<SampleData> getAll();
}
