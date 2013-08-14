package com.scence.sign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scence.sign.R;
import com.scence.sign.util.Consts;
import com.scence.sign.util.MyDialog;
import com.scence.sign.util.MyDialog.OnBtnClickListener;
import com.scence.sign.util.MyToast;
import com.scence.sign.util.Tools;

public class SignOnActivity extends Activity {
	private static final String TAG = "SignOnActivity";
	private Context mCtx;
	private String mIMSI;
	private TextView mPromptTxt;
	
	private EditText[] mPwdText = new EditText[PWD_LENGTH];
	private final static int[] EDIT_ID = {
		R.id.pwd01,R.id.pwd02,
		R.id.pwd03,R.id.pwd04
	};

	private Button[] mSecBtn = new Button[10];
	private final static int[] BTN_ID = { 
		R.id.btn00, R.id.btn01, R.id.btn02,
		R.id.btn03, R.id.btn04, R.id.btn05, R.id.btn06, R.id.btn07,
		R.id.btn08, R.id.btn09 };
	
	// 密码长度
	private static final int PWD_LENGTH = 4;
	// 初始密码
	private static final String INIT_PWD = "1234";
	// 重置密码状态
	protected static final int INPUT_STATUS_RESET = -1;
	// 第一次新密码输入状态
	protected static final int INPUT_STATUS_1ST = -2;
	// 第二次新密码输入状态
	protected static final int INPUT_STATUS_2ND = -3;
	// 正常密码输入状态
	protected static final int INPUT_STATUS_NORMAL = 0;
	// 输入状态：密码重置状态为true；正常密码输入状态为fasle
	private int mInputStatus = 0;
	// 第一次输入密码
	private String mFirstInput;
	
	private Button mReSetPwd;
	private Button mCleanInput;
	
	private int mInputCount;
	private String mPwdPref;
	private String mPwdInput = "";
	private SharedPreferences mSp;
	private MyDialog mDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signon);
        mCtx = this;
        for (int i = 0; i < PWD_LENGTH; i++) {
        	mPwdText[i] = (EditText) findViewById(EDIT_ID[i]);
		}
        
        for (int i = 0; i < mSecBtn.length; i++) {
        	mSecBtn[i] = (Button) findViewById(BTN_ID[i]);
        	mSecBtn[i].setOnClickListener(mOnClickLisenter);
		}
        
        mReSetPwd = (Button) findViewById(R.id.btnReset);
        mReSetPwd.setOnClickListener(mOnClickLisenter);
        
        mCleanInput = (Button) findViewById(R.id.btnCleanInput);
        mCleanInput.setOnClickListener(mOnClickLisenter);
        
        mPromptTxt = (TextView) findViewById(R.id.textPrompt);
        
        mDialog = new MyDialog(mCtx);
        mDialog.setOnBtnClickListener(new OnBtnClickListener() {
			
			@Override
			public void onReset() {
				mInputStatus = INPUT_STATUS_RESET;
				cleanSecText();
				mFirstInput = "";
				mPromptTxt.setText("请输入旧密码");
			}
			
			@Override
			public void onForget() {
				startActivity(new Intent(mCtx, PhoneCheckActivity.class));
				finish();
			}
		});
        
        Intent intent = getIntent();
        mIMSI = intent.getStringExtra(Consts.INTENT_IMSI);
        
        mSp = getSharedPreferences(Consts.PREF_FILE_NAME, Context.MODE_PRIVATE);
        mPwdPref = mSp.getString(Consts.PREF_PWD, "");
        Log.d(TAG, "mPwdPref = " + mPwdPref);
        if(TextUtils.isEmpty(mPwdPref)){
        	mPwdPref = Tools.md5(INIT_PWD);
        	mInputStatus = INPUT_STATUS_RESET;
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		String imsi = Tools.getIMSI(mCtx);
		Log.d(TAG, "get IMSI is " + imsi);
		if (!TextUtils.isEmpty(imsi)) {
			String md5IMSI = Tools.md5(imsi);
			Log.d(TAG, "get IMSI after md5 is " + md5IMSI);
			if (md5IMSI.equals(mIMSI)) {
				Log.d(TAG, "SIM Card check suceessful");
			} else {
				MyToast.show(mCtx, "请使用指定手机卡，或重新绑定手机号");
				startActivity(new Intent(mCtx, PhoneCheckActivity.class));
				finish();
			}
		}
	}

	private OnClickListener mOnClickLisenter = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnReset:
				mDialog.show();
				break;
			case R.id.btnCleanInput:
				cleanSecText();
				break;

			default:
				showInput(v.getId());
				break;
			}
		}
	};

	protected void showInput(int id) {
		
		if(mInputCount == PWD_LENGTH){
			if(mInputStatus == INPUT_STATUS_NORMAL){
				MyToast.showLong(mCtx, "密码错误，请重新输入");
				cleanSecText();
				return;
			}
		}
		
		for (int i = 0; i < BTN_ID.length; i++) {
			
			if(id == BTN_ID[i]){
				// 输入值为i
				++mInputCount;
				mPwdText[mInputCount-1].setText(String.valueOf(i));
				mPwdInput += String.valueOf(i);
				Log.d(TAG, "mSecretInput = " + mPwdInput + ", mInputCount = " + mInputCount + ", mInputStatus = " + mInputStatus);
				
				if (mInputCount == PWD_LENGTH) {
					
					if(INPUT_STATUS_1ST == mInputStatus){// 第一次输入新密码
						Log.d(TAG, "INPUT_STATUS_1ST " + mPwdInput);
						mFirstInput = mPwdInput;
						mInputStatus--;
						cleanSecText();
						mPromptTxt.setText("请再次输入新密码");
					}else if(INPUT_STATUS_2ND == mInputStatus){// 再次输入新密码
						Log.d(TAG, "INPUT_STATUS_2ND secretInput = " + mPwdInput + ", firstSecret = " + mFirstInput);
						if(mFirstInput.equals(mPwdInput)){
							mPromptTxt.setText("密码修改成功");
							MyToast.showLong(mCtx, "密码修改成功,如果忘记密码，请在重置中选择“忘记密码”进行恢复");
							mSp.edit().putString(Consts.PREF_PWD, Tools.md5(mPwdInput)).commit();
							startMainActivity();
						}else{
							MyToast.showLong(mCtx, "两次密码输入不一致，请重新输入");
							mInputStatus = INPUT_STATUS_1ST;
							cleanSecText();
							mPromptTxt.setText("请输入新密码");
						}
					} else if (INPUT_STATUS_RESET == mInputStatus) { // 旧密码输入完成，进行验证
						Log.d(TAG, "INPUT_STATUS_RESET : " + mPwdInput);
						if(mPwdInput.equals(INIT_PWD)){
							mInputStatus--;
							mPromptTxt.setText("请输入新密码");
						}else{
							mPromptTxt.setText("旧密码验证失败");
							MyToast.show(mCtx, "请重新输入");
						}
						cleanSecText();
					} else if (INPUT_STATUS_NORMAL == mInputStatus){
						if(mPwdPref.equals(Tools.md5(mPwdInput))){
							startMainActivity();
						}else{
							mPromptTxt.setText("密码验证失败");
							MyToast.showLong(mCtx, "请重新输入,如果忘记密码，请在重置中选择“忘记密码”进行恢复");
							cleanSecText();
						}
					}
				}
				
				break;
			}
		}
	}

	private void cleanSecText() {
		for (int i = 0; i < PWD_LENGTH; i++) {
			mPwdText[i].setText("");
			mInputCount = 0;
			mPwdInput = "";
		}
	}

	private void startMainActivity() {
		Intent intent = new Intent(SignOnActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}  
}