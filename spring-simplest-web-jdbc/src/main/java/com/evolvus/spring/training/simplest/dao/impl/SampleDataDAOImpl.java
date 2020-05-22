package com.evolvus.spring.training.simplest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.evolvus.spring.training.simplest.dao.SampleDataDao;
import com.evolvus.spring.training.simplest.domain.SampleData;

@Component
public class SampleDataDAOImpl implements SampleDataDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<SampleData> getAll() {
		String sql = "select * from sample_data";
		return jdbcTemplate.query(sql, new RowMapper<SampleData>() {

			public SampleData mapRow(ResultSet rs, int rowNum) throws SQLException {
				SampleData result = new SampleData();
				result.setId(rs.getInt("ID"));
				result.setFirstName(rs.getString("FIRST_NAME"));
				result.setLastName(rs.getString("LAST_NAME"));
				return result;
			}
			
		});
	}

}
