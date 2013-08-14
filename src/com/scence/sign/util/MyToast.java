package com.scence.sign.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Description:
 * 
 * @author KongBin
 * @version 1.0
 * @since 2013-8-7 下午2:11:46
 */
public class MyToast {

	/**
	 * @param mCtx
	 * @param string
	 * @author kongbin 2013-8-7 下午2:13:05
	 */
	public static void show(Context mCtx, String string) {

		Toast.makeText(mCtx, string, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param mCtx
	 * @param string
	 * @author kongbin 2013-8-7 下午2:13:05
	 */
	public static void showLong(Context mCtx, String string) {

		Toast.makeText(mCtx, string, Toast.LENGTH_LONG).show();
	}

}
