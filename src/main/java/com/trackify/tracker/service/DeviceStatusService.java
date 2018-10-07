package com.trackify.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackify.tracker.repo.UpTimeRepo;

@Service
public class DeviceStatusService {
	@Autowired
	UpTimeRepo upTimeRepo;
	public String getUpTimeStatus() {
		return upTimeRepo.getUpTimeStatus();
	}

	public String getUpTimeLog(String deviceId) {
		return upTimeRepo.getUpTimeLog(deviceId);
	}
}
