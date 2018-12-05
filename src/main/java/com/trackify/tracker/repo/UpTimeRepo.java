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
		namedParameters.addValue("createdDate", new Date());
		namedParameters.addValue("currentTimestamp",new Timestamp(new Date().getTime()));
		String sql = "insert into device_uptime_tracker values(:deviceId,:deviceName,:logTime,:status,:upTime,:downTime,:createdDate,:currentTimestamp)";
		namedJdbcTemplate.update(sql, namedParameters);
	}

	public int checkRecordEntry() {

		String sql = "SELECT count(1) FROM device_uptime_tracker WHERE created_date=?";

		return jdbcTemplate.queryForObject(sql, new Object[] {new Date()}, Integer.class);
	}

	public void updateUpTimeStatus(UpTimeModel upTimeModel) {
		String sql = "";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("deviceId", upTimeModel.getDeviceId());
		namedParameters.addValue("logTime", new Timestamp(new Date().getTime()));
		namedParameters.addValue("status", upTimeModel.isStatus());
		namedParameters.addValue("createdDate", new Date());
		namedParameters.addValue("currentTimestamp",new Timestamp(new Date().getTime()));
		if(upTimeModel.isStatus()) {
			sql = "update device_uptime_tracker set log_time=:logTime, status=:status, status_last_updated=CASE WHEN STATUS='FALSE' THEN :currentTimestamp::timestamptz ELSE status_last_updated END, up_time=CASE WHEN STATUS='TRUE' THEN (up_time + DATE_PART('second', :logTime::timestamp-log_time)) ELSE (up_time + (DATE_PART('second', :logTime::timestamp-log_time)/2)) END, down_time=CASE WHEN STATUS='TRUE' THEN down_time + 0 ELSE (down_time + (DATE_PART('second', :logTime::timestamp-log_time)/2)) END WHERE DEVICE_ID=:deviceId AND CREATED_DATE=:createdDate";
		}else {
			sql = "update device_uptime_tracker set log_time=:logTime, status=:status, status_last_updated=CASE WHEN STATUS='TRUE' THEN :currentTimestamp::timestamptz ELSE status_last_updated END, up_time=CASE WHEN STATUS='TRUE' THEN (up_time + (DATE_PART('second', :logTime::timestamp-log_time)/2)) ELSE up_time+0 END, down_time=CASE WHEN STATUS='TRUE' THEN (down_time + (DATE_PART('second', :logTime::timestamp-log_time)/2)) ELSE down_time + DATE_PART('second', :logTime::timestamp - log_time) END WHERE DEVICE_ID=:deviceId AND CREATED_DATE=:createdDate";
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
			String sql = "insert into device_status_tracker values(:deviceId,:deviceName,:logTime,:status)";
			namedJdbcTemplate.update(sql, namedParameters);
	}

	public String getUpTimeStatus() {
		String json = "{}";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("createdDate", new Date());
		String sql = "select device_name, device_id, status_last_updated, status, up_time, down_time from device_uptime_tracker where created_date = :createdDate order by status";
		List<UpTimeModel> upTimeModelList = namedJdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<UpTimeModel>(UpTimeModel.class));
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
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("deviceId", deviceId);
		namedParameters.addValue("createdDate", new Date());

		String sql = "select log_time, status from device_status_tracker where device_id=:deviceId and date(log_time)=:createdDate order by log_time desc";
		List<UpTimeModel> upTimeModelList = namedJdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<UpTimeModel>(UpTimeModel.class));
		if(null != upTimeModelList && !upTimeModelList.isEmpty()) {
			try {
				json = new ObjectMapper().writeValueAsString(upTimeModelList);
			} catch (JsonProcessingException e) {

				logger.error("Error while json conversion", e);
			}
		}
		return json;
	}

	public List<UpTimeModel> getUpTimeReport(String start, String end) {

		String sql = "select device_name, device_id, log_time, status, up_time, down_time, created_date from device_uptime_tracker where created_date between '"+start+"' and '"+end+"' order by status";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<UpTimeModel>(UpTimeModel.class));
	}
}
