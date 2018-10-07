package com.trackify.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.trackify.tracker.model.UpTimeModel;
import com.trackify.tracker.scheduler.ScheduledScanner;



@SpringBootApplication
@EnableScheduling
public class TrackifyApplication {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(TrackifyApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		List<UpTimeModel> deviceList = new ArrayList<>();
//
//		UpTimeModel upTimeModelA = new UpTimeModel();
//		upTimeModelA.setIpAddress("192.168.1.1");
//
//		UpTimeModel upTimeModelB = new UpTimeModel();
//		upTimeModelB.setIpAddress("192.168.1.5");
//
//		UpTimeModel upTimeModelC = new UpTimeModel();
//		upTimeModelC.setIpAddress("192.168.1.8");
//
//		deviceList.add(upTimeModelA);
//		deviceList.add(upTimeModelB);
//		deviceList.add(upTimeModelC);
//
//		ScheduledScanner scheduledScanner = new ScheduledScanner(deviceList);
//	}

}
