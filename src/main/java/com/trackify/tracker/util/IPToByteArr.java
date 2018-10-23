package com.trackify.tracker.util;
import java.util.StringTokenizer;
public class IPToByteArr {

	public final static byte[] asBytes(String addr) {



		int ipInt = parseStringAddress(addr);
		if ( ipInt == 0) {
			return null;
		}


		byte[] ipByts = new byte[4];

		ipByts[3] = (byte) (ipInt & 0xFF);
		ipByts[2] = (byte) ((ipInt >> 8) & 0xFF);
		ipByts[1] = (byte) ((ipInt >> 16) & 0xFF);
		ipByts[0] = (byte) ((ipInt >> 24) & 0xFF);

		return ipByts;
	}

	public final static int parseStringAddress(String ipaddr) {


		if ( ipaddr == null || ipaddr.length() < 7 || ipaddr.length() > 15) {
			return 0;
		}
		StringTokenizer token = new StringTokenizer(ipaddr,".");
		if ( token.countTokens() != 4) {
			return 0;
		}
		int ipInt = 0;

		while ( token.hasMoreTokens()) {


			String ipNum = token.nextToken();

			try {

				int ipVal = Integer.valueOf(ipNum).intValue();
				if ( ipVal < 0 || ipVal > 255) {
					return 0;
				}
				ipInt = (ipInt << 8) + ipVal;
			}
			catch (NumberFormatException ex) {
				return 0;
			}
		}

		return ipInt;
	}


}
