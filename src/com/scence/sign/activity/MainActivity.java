package com.scence.sign.activity;

import java.util.Random;

import com.scence.sign.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	
	private static final int PWD_LENGTH = 10;
	private ImageView[] mPwdImage = new ImageView[PWD_LENGTH];
	private final static int[] PWD_VIEW_IMAGE_ID = {
		R.id.imageView0,R.id.imageView1,R.id.imageView2,
		R.id.imageView3,R.id.imageView4,R.id.imageView5,
		R.id.imageView6,R.id.imageView7,R.id.imageView8,
		R.id.imageView9
	};
	
	private final static int[] DYNAMIC_IMAGE_ID = {
		R.drawable.otp_number_zero,R.drawable.otp_number_one,R.drawable.otp_number_two,
		R.drawable.otp_number_three,R.drawable.otp_number_four,R.drawable.otp_number_five,
		R.drawable.otp_number_six,R.drawable.otp_number_seven,R.drawable.otp_number_eight,
		R.drawable.otp_number_nine
	};
	
	private final static int[] FUZZY_IMAGE_ID = {
		R.drawable.otp_number_zero_fuzzy,R.drawable.otp_number_one_fuzzy,
		R.drawable.otp_number_two_fuzzy,R.drawable.otp_number_three_fuzzy,
		R.drawable.otp_number_four_fuzzy,R.drawable.otp_number_five_fuzzy,
		R.drawable.otp_number_six_fuzzy,R.drawable.otp_number_seven_fuzzy,
		R.drawable.otp_number_eight_fuzzy,R.drawable.otp_number_nine_fuzzy
	};
	protected static final int MSG_SHOW_FUZZY = 0;
	protected static final int MSG_SHOW_DYNAMIC = 1;
	
	private Button mGetBtn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        for (int i = 0; i < PWD_LENGTH; i++) {
        	mPwdImage[i] = (ImageView) findViewById(PWD_VIEW_IMAGE_ID[i]);
		}
        showFuzzyNum();
        mGetBtn = (Button) findViewById(R.id.btnGet);
        mGetBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.removeMessages(MSG_SHOW_FUZZY);
				mHandler.removeMessages(MSG_SHOW_DYNAMIC);
				mHandler.sendEmptyMessage(MSG_SHOW_FUZZY);
				mHandler.sendEmptyMessageDelayed(MSG_SHOW_DYNAMIC, 300);
			}
		});
    }
    private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SHOW_FUZZY:
				showFuzzyNum();
				break;
			case MSG_SHOW_DYNAMIC:
				showDynamicNum();
				break;

			default:
				break;
			}
		}
    	
    };
	protected void showDynamicNum() {
		String time = String.valueOf(System.currentTimeMillis()/1000L);
		for (int i = 0; i < PWD_LENGTH; i++) {
			int index = Integer.parseInt(time.substring(i, i+1));
			mPwdImage[i].setImageResource(DYNAMIC_IMAGE_ID[index]);
		}
	}
	
	protected void showFuzzyNum() {
		for (int i = 0; i < PWD_LENGTH; i++) {
			Random random = new Random();
			int index = random.nextInt(9);
			Log.d(TAG, "" + index);
			mPwdImage[i].setImageResource(FUZZY_IMAGE_ID[index]);
		}
	}
	
}