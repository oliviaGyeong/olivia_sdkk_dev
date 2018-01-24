package com.example.kcpsecutest.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {
	public static String getNetwork(Context con) {
		ConnectivityManager manager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

		if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting()) { 		// WIFI 연결
			return "TEST_WIFI";
		} else 																																																						// 3G 연결
			return "TEST_3G";
	}
}
