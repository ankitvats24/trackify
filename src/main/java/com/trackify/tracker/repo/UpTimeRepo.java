package com.trackify.tracker.repo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackify.tracker.model.UpTimeModel;


@Configuration
public class UpTimeRepo {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	NamedParameterJdbcTemplate namedJdbcTemplate;
	@Autowired
	JdbcTemplate jdbcTemplate;	

	public void logUpTimeStatus(UpTimeModel upTimeModel) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("deviceId", upTimeModel.getDeviceId());
		namedParameters.addValue("deviceName", upTimeModel.getDeviceName());
		namedParameters.addValue("logTime", new Timestamp(new Date().getTime()));
		namedParameters.addValue("status", upTimeModel.isStatus());
		namedParameters.addValue("upTime", 0);
		namedParameters.addValue("downTime", 0);
		String sql = "INSERT INTO DEVICE_UPTIME_TRACKER VALUES(:deviceId,:deviceName,:logTime,:status,:upTime,:downTime)";
		namedJdbcTemplate.update(sql, namedParameters);
	}
	
	public void updateUpTimeStatus(UpTimeModel upTimeModel) {
		String sql = "";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("deviceId", upTimeModel.getDeviceId());
		namedParameters.addValue("logTime", new Timestamp(new Date().getTime()));
		namedParameters.addValue("status", upTimeModel.isStatus());

		if(upTimeModel.isStatus()) {
			sql = "UPDATE DEVICE_UPTIME_TRACKER SET LOG_TIME=:logTime, STATUS=:status, UP_TIME=CASEWHEN(STATUS='TRUE',(UP_TIME + DATEDIFF('SECOND', LOG_TIME, :logTime)),(UP_TIME + DATEDIFF('SECOND', LOG_TIME, :logTime)/2)), DOWN_TIME=CASEWHEN(STATUS='TRUE',DOWN_TIME + 0,(DOWN_TIME + DATEDIFF('SECOND', LOG_TIME, :logTime)/2))  WHERE DEVICE_ID=:deviceId";
		}else {
			sql = "UPDATE DEVICE_UPTIME_TRACKER SET LOG_TIME=:logTime, STATUS=:status, UP_TIME=CASEWHEN(STATUS='TRUE',(UP_TIME + DATEDIFF('SECOND', LOG_TIME, :logTime)/2),UP_TIME+0), DOWN_TIME=CASEWHEN(STATUS='TRUE',(DOWN_TIME + DATEDIFF('SECOND', LOG_TIME, :logTime)/2),DOWN_TIME + DATEDIFF('SECOND', LOG_TIME, :logTime))  WHERE DEVICE_ID=:deviceId";
		}
		namedJdbcTemplate.update(sql, namedParameters);
	}

	@Async
	public void logDeviceStatus(UpTimeModel upTimeModel) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("deviceId", upTimeModel.getDeviceId());
		namedParameters.addValue("deviceName", upTimeModel.getDeviceName());
		namedParameters.addValue("logTime", new Timestamp(new Date().getTime()));
		namedParameters.addValue("status", upTimeModel.isStatus());
		String sql = "INSERT INTO DEVICE_STATUS_TRACKER VALUES(:deviceId,:deviceName,:logTime,:status)";
		namedJdbcTemplate.update(sql, namedParameters);
	}

	public String getUpTimeStatus() {
		String json = "{}";
		String sql = "SELECT DEVICE_NAME, DEVICE_ID, LOG_TIME, STATUS, UP_TIME, DOWN_TIME FROM DEVICE_UPTIME_TRACKER ORDER BY STATUS";
		List<UpTimeModel> upTimeModelList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UpTimeModel>(UpTimeModel.class));
		if(null != upTimeModelList && !upTimeModelList.isEmpty()) {
			try {
				json = new ObjectMapper().writeValueAsString(upTimeModelList);
			} catch (JsonProcessingException e) {

				logger.error("Error while json conversion", e);
			}
		}
		return json;
	}

	public String getUpTimeLog(String deviceId) {
		String json = "{}";
		String sql = "SELECT LOG_TIME, STATUS FROM DEVICE_STATUS_TRACKER WHERE DEVICE_ID=? ORDER BY LOG_TIME DESC";
		List<UpTimeModel> upTimeModelList = jdbcTemplate.query(sql, new Object[]{deviceId}, new BeanPropertyRowMapper<UpTimeModel>(UpTimeModel.class));
		if(null != upTimeModelList && !upTimeModelList.isEmpty()) {
			try {
				json = new ObjectMapper().writeValueAsString(upTimeModelList);
			} catch (JsonProcessingException e) {

				logger.error("Error while json conversion", e);
			}
		}
		return json;
	}
}
