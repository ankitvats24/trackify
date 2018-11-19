package com.trackify.tracker.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

	@RequestMapping(path="/getReport",method=RequestMethod.GET)
	public void getUpTimeReport(@RequestParam(name="start") String start, @RequestParam(name="end") String end, HttpServletResponse response) {
		ServletOutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		try {
			outputStream = response.getOutputStream();
			workbook = deviceStatusService.getUpTimeReport(start, end);

			if(null != workbook) {
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader("Content-Disposition", "attachment; filename=TrackifyReport_"+start+"_"+end+".xlsx");

				workbook.write(outputStream);

			} else {
				response.setContentType("text/html");

				outputStream.print("<!DOCTYPE html><html><body><script>");
				outputStream.print("alert(\"No data found !\");window.close();");
				outputStream.print("</script></body></html>");
			}
		} catch (Exception e) {

			e.printStackTrace();
			if(null != outputStream) {
				try {
					outputStream.print("<!DOCTYPE html><html><body><script>");
					outputStream.print("alert(\"Some error occured !\");window.close();");
					outputStream.print("</script></body></html>");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if(null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(outputStream!=null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
