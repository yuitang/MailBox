package com.example.yui.mailbox.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class SizeHelper {
	private static float designedDensity = 1.5f;
	private static int designedScreenWidth = 540;
	private static Context context = null;

	private SizeHelper() {
	}

	public static void prepare(Context c) {
		if(context == null || context != c.getApplicationContext()) {
			context = c;
		}
		DisplayMetrics metric = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		designedScreenWidth = metric.widthPixels;  // 屏幕宽度（像素）
		designedDensity = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
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
