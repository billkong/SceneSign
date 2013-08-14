package com.scence.sign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.scence.sign.R;
import com.scence.sign.util.Consts;
import com.scence.sign.util.MyToast;
import com.scence.sign.util.Tools;

public class PhoneCheckActivity extends Activity {
	protected static final String TAG = "PhoneCheckActivity";
	private EditText mPhoneNum;
	private EditText mPhoneNumConfirm;
	private Button mOkBtn;
	private Context mCtx;
	private String mIMSI;
	protected static final int PHONE_NUM_LENGHT = 11;
    
    private SharedPreferences mSP;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mCtx = this;
        
		// 手机号码已绑定，进入密码登录界面
		if (checkIMSI()) {
			startSignOnActivity(mIMSI);
			Log.d(TAG, "IMSI is right, goto signon " + mIMSI);
			return;
		} else {
			Log.e(TAG, "not bund");
		}
        
        // 未绑定启动绑定界面
        setContentView(R.layout.bind_phone);
        mPhoneNum = (EditText) findViewById(R.id.phoneNum);
        mPhoneNum.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String input = mPhoneNum.getText().toString().trim();
				if(input.length() == PHONE_NUM_LENGHT){
					if(checkPhone(input, false)){
						mPhoneNumConfirm.requestFocus();
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        
        mPhoneNumConfirm = (EditText) findViewById(R.id.phoneNum_confirm);
        mPhoneNumConfirm.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String input = mPhoneNumConfirm.getText().toString().trim();
				if(input.length() == PHONE_NUM_LENGHT){
					checkPhone(input,true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        
        mOkBtn = (Button) findViewById(R.id.okBtn);
        
        mOkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneNum = mPhoneNumConfirm.getText().toString().trim();
				checkPhone(phoneNum, true);
			}
		});
    }
    
    private boolean checkIMSI() {
    	mSP = getSharedPreferences(Consts.PREF_FILE_NAME, Context.MODE_PRIVATE);
		String pNum = mSP.getString(Consts.PREF_KEY_NUM, "");
		if(!TextUtils.isEmpty(pNum)){
			String imsi = mSP.getString(Consts.PREF_KEY_IMSI, "");
			if(!TextUtils.isEmpty(imsi)){
				mIMSI = Tools.md5(Tools.getIMSI(mCtx));
				if(imsi.equals(mIMSI)){
					return true;
				}
			}
		}
		return false;
	}

	protected boolean checkPhone(String phoneNum, boolean confirm) {
		Log.d(TAG, "phoneNum" + phoneNum);
		if (TextUtils.isEmpty(phoneNum)
				|| phoneNum.length() != PHONE_NUM_LENGHT
				|| !phoneNum.startsWith("1")) {
			MyToast.show(mCtx, "请输入正确格式的手机号码");
			return false;
		} else if (confirm) {
			if(phoneNum.equals(mPhoneNum.getText().toString().trim())){
				String md5IMSI = Tools.md5(Tools.getIMSI(mCtx));
				if(!TextUtils.isEmpty(md5IMSI)){
					SharedPreferences sp = getSharedPreferences(Consts.PREF_FILE_NAME, Context.MODE_PRIVATE);
					String md5Num = Tools.md5(phoneNum);
					sp.edit().putString(Consts.PREF_KEY_NUM, md5Num).commit();
					sp.edit().putString(Consts.PREF_KEY_IMSI, md5IMSI).commit();
					Log.d(TAG, "phoneNum after md5 " + md5Num);
					MyToast.show(mCtx, "绑定成功，请输入初始密码，进行密码设置");
					startSignOnActivity(md5IMSI);
				}else{
					return false;
				}
			}else {
				MyToast.show(mCtx, "两次输入不一致，请确认后重新输入");
				mPhoneNum.requestFocus();
				return false;
			}
		} 
		return true;
	}

	/**
     * 启动密码登录
     * @param pNum
     */
    private void startSignOnActivity(String pNum) {
		Intent intent = new Intent();
		intent.putExtra(Consts.INTENT_IMSI, pNum);
		intent.setClass(PhoneCheckActivity.this, SignOnActivity.class);
		startActivity(intent);
		finish();
	}
}