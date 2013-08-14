package com.scence.sign.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Description:
 * 
 * @author KongBin
 * @version 1.0
 * @since 2013-8-7 下午2:27:53
 */
public class Tools {
	private static final char HEX_DIGITS[] = {
			'0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };
	private static final String TAG = "Tools";

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5(String s) {
		if(TextUtils.isEmpty(s)){
			return "";
		}
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 	1.	getPhoneNumber方法返回当前手机的电话号码，
	 * 同时必须在androidmanifest.xml中
	 * 加入 android.permission.READ_PHONE_STATE 这个权限，
	 * 	2.	主流的获取用户手机号码一般采用用户主动发送短信到SP或接收手机来获取。
	 * @param context
	 * @return 
	 */
	public static String getIMSI(Context context){
		String imsi = null;
		if(context == null){
			Log.e(TAG, "context is null");
			return imsi;
		}
	    try {
			TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if(telephonyMgr != null){
				imsi = telephonyMgr.getSubscriberId();
				Log.d(TAG, "imsi = " + imsi);
			}else{
				Log.e(TAG, "telephonyMgr is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return imsi;  
	}
}
