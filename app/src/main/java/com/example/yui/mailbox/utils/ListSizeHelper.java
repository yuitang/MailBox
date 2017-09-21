package com.example.yui.mailbox.utils;

import android.content.Context;

public class ListSizeHelper {
	private static float designedDensity = 1.5f;
	private static int designedScreenWidth = 540;
	private static Context context = null;

	private ListSizeHelper() {
	}

	public static void prepare(Context c) {
		if(context == null || context != c.getApplicationContext()) {
			context = c;
		}
	}

	/**
	 * 根据density转换设计的px到目标机器，返回px大小
	 * @return 像素大小
	 */
	public static int fromPx(int px) {
		return Utils.designToDevice(context, designedDensity, px);
	}

	/**
	* 根据屏幕宽度转换设计的px到目标机器，返回px大小
	* @return 像素大小
	*/
	public static int fromPxWidth(int px) {
		return Utils.designToDevice(context, designedScreenWidth, px);
	}

	/**
	* 根据density转换设计的dp到目标机器，返回px大小
	* @return 像素大小
	*/
	public static int fromDp(int dp) {
		int px = Utils.dipToPx(context, dp);
		return Utils.designToDevice(context, designedDensity, px);
	}
}
