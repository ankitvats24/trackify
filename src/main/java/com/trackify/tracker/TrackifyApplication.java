package com.trackify.tracker;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class TrackifyApplication {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		SpringApplication.run(TrackifyApplication.class, args);
	}
}
