package com.trackify.tracker.scheduler;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import com.trackify.tracker.model.UpTimeModel;
import com.trackify.tracker.repo.UpTimeRepo;
@Configurable
public class Processor implements Runnable{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final com.trackify.tracker.model.UpTimeModel upTimeModel;
	private UpTimeRepo upTimeRepo;
	private final int timeout;
	public Processor(UpTimeModel upTimeModel, int timeout, UpTimeRepo upTimeRepo) {
		this.upTimeModel = upTimeModel;
		this.timeout = timeout;
		this.upTimeRepo = upTimeRepo;
	}
	@Override
	public void run() {

		try {
			if (InetAddress.getByName(upTimeModel.getDeviceId()).isReachable(timeout)) {
				upTimeModel.setStatus(true);
				logger.info("{} Available", upTimeModel.getDeviceId());
			}else {
				upTimeModel.setStatus(false);
				logger.warn("{} Unavailable", upTimeModel.getDeviceId());
			}
			upTimeRepo.logDeviceStatus(upTimeModel);
			upTimeRepo.updateUpTimeStatus(upTimeModel);

		} catch (IOException e) {
			logger.error(upTimeModel.getDeviceId() + " Unreachable");
			upTimeModel.setStatus(false);
			upTimeRepo.logDeviceStatus(upTimeModel);
			upTimeRepo.updateUpTimeStatus(upTimeModel);
		}

	}

}