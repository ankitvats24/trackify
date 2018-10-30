package com.trackify.tracker.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PingUtil {

	public static boolean isReachable(int nping, int wping, String ipping) throws IOException, InterruptedException {

		int nReceived = 0;
		

		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec("ping -n " + nping + " -w " + wping + " " + ipping);
		Scanner scanner = new Scanner(process.getInputStream());
		process.waitFor();
		ArrayList<String> strings = new ArrayList<>();
		String data = "";

		while (scanner.hasNextLine()) {
			String string = scanner.nextLine();
			data = data + string + "\n";
			strings.add(string);
		}

		scanner.close();


		int index = 2;

		for (int i = index; i < nping + index; i++) {
			String string = strings.get(i);
			if (string.contains("bytes") && string.contains("time") && string.contains("TTL")) {
				nReceived++;
			}
		}
		return nReceived > 0;
	}

}
