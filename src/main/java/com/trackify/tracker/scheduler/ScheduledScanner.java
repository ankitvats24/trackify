package com.trackify.tracker.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.trackify.tracker.model.UpTimeModel;
import com.trackify.tracker.repo.UpTimeRepo;
@Service
public class ScheduledScanner {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UpTimeRepo upTimeRepo;
	private int isReachableTimeout;
	private String deviceDetailsFilePth;
	private List<UpTimeModel> deviceList;
	private boolean initialized = false;
	ExecutorService exec = null;
	@Autowired
	public ScheduledScanner(@Value("${device.details.file.path}") final String deviceDetailsFilePth, 
			@Value("${device.isreachable.timeout}") final int isReachableTimeout) throws IOException {
		this.deviceDetailsFilePth=deviceDetailsFilePth;
		this.isReachableTimeout=isReachableTimeout;
		setDeviceList();
		this.exec = Executors.newFixedThreadPool(this.deviceList.size());
	}
	void setDeviceList() throws IOException {
		List<String> ipList = FileUtils.readLines(new File(deviceDetailsFilePth), "utf-8");

		List<UpTimeModel> deviceList = new ArrayList<>();
		for(String deviceDetails : ipList) {
			if(!"".equalsIgnoreCase(deviceDetails.trim())) {
				String[] deviceIpName = deviceDetails.split(",");
				UpTimeModel upTimeModel = new UpTimeModel();
				upTimeModel.setDeviceId(deviceIpName[0].trim());
				upTimeModel.setDeviceName(deviceIpName[1].trim());
				deviceList.add(upTimeModel);
			}
		}

		this.deviceList = deviceList;
	}

	@Scheduled(fixedDelayString="${schedular.interval:5}000")
	public void run() {
		final int ADDRESS_COUNT = deviceList.size();
		int existingRecord = upTimeRepo.checkRecordEntry();

		List<Future<?>> futures = new ArrayList<Future<?>>(ADDRESS_COUNT);
		for (int count = 0; count < ADDRESS_COUNT; count++) {
			if(!initialized && 0==existingRecord) {
				upTimeRepo.logUpTimeStatus(deviceList.get(count));
				if(count == ADDRESS_COUNT-1) {
					initialized = true;
				}
			}
			Runnable r = new Processor(deviceList.get(count), isReachableTimeout,upTimeRepo);
			futures.add(exec.submit(r));

		}

		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void resetTime() {
		initialized = false;
	}
}
