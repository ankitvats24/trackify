package com.trackify.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackify.tracker.service.DeviceStatusService;

@RestController
public class DeviceStatusController {
	@Autowired
	DeviceStatusService deviceStatusService;
	@RequestMapping(path="/deviceLiveStatus",method=RequestMethod.GET)
	public String getDeviceLiveStatus() {
		return deviceStatusService.getUpTimeStatus();
	}

	@RequestMapping(path="/deviceLog",method=RequestMethod.GET)
	public String getUpTimeLog(@RequestParam(name="deviceId") String deviceId) {
		return deviceStatusService.getUpTimeLog(deviceId);
	}
}
