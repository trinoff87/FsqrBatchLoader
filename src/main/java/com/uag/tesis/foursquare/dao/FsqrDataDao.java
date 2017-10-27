package com.uag.tesis.foursquare.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.uag.tesis.fousquare.domain.FsqrData;

public class FsqrDataDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_FSQR_DATA_SQL = 
			"insert into foursquare_data(run_date,point_name,users,checkins,likes) values (?,?,?,?,?)";
	
	public int insertFsqrData(FsqrData fsqrData){
		return jdbcTemplate.update(INSERT_FSQR_DATA_SQL, new Object[] {fsqrData.getRunDate(), fsqrData.getPointName(), fsqrData.getUsers(),
				fsqrData.getCheckins(), fsqrData.getLikes()});
	}

}
