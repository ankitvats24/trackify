package com.trackify.tracker.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackify.tracker.model.UpTimeModel;
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

	public XSSFWorkbook getUpTimeReport(String start, String end) {
		List<UpTimeModel> upTimeModelList = upTimeRepo.getUpTimeReport(start, end);
		XSSFWorkbook workbook = null;
		if(upTimeModelList != null && !upTimeModelList.isEmpty()) {
			Map<Date, List<UpTimeModel>> upTimeModelListGrpByDate = upTimeModelList.stream().collect(Collectors.groupingBy(UpTimeModel::getCreatedDate));
			List<Date> uniqueDates = new ArrayList<>(upTimeModelListGrpByDate.keySet());
			if(1<uniqueDates.size()) {
				uniqueDates.sort((d1,d2)->d1.compareTo(d2));
			}
			workbook = new XSSFWorkbook ();

			
			for(Date d : uniqueDates) {
				int rowCount = 0;
				Sheet sheet = workbook.createSheet(d.toString());

				Row headerRow = sheet.createRow(rowCount);
				headerRow.createCell(0).setCellValue("Device Name");
				headerRow.createCell(1).setCellValue("IP Address");
				headerRow.createCell(2).setCellValue("Up Time");
				headerRow.createCell(3).setCellValue("Down Time");

				List<UpTimeModel> upTimeModelListPerDay = upTimeModelListGrpByDate.get(d);

				for(UpTimeModel upTimeModel:upTimeModelListPerDay) {
					int columnCount = 0;
					Row dataRow = sheet.createRow(++rowCount);
					dataRow.createCell(columnCount++).setCellValue(upTimeModel.getDeviceName());
					dataRow.createCell(columnCount++).setCellValue(upTimeModel.getDeviceId());
					dataRow.createCell(columnCount++).setCellValue(formatSeconds(upTimeModel.getUpTime()));
					dataRow.createCell(columnCount++).setCellValue(formatSeconds(upTimeModel.getDownTime()));
				}

			}

		}

		return workbook;
	}

	private String formatSeconds(int timeInSeconds)
	{
	    int hours = timeInSeconds / 3600;
	    int secondsLeft = timeInSeconds - hours * 3600;
	    int minutes = secondsLeft / 60;
	    int seconds = secondsLeft - minutes * 60;

	    String formattedTime = "";
	    if (hours < 10)
	        formattedTime += "0";
	    formattedTime += hours + ":";

	    if (minutes < 10)
	        formattedTime += "0";
	    formattedTime += minutes + ":";

	    if (seconds < 10)
	        formattedTime += "0";
	    formattedTime += seconds ;

	    return formattedTime;
	}
}
