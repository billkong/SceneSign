package com.scence.sign.util;

import com.scence.sign.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyDialog extends Dialog implements OnClickListener{

    private static final String TAG = "MyDialog";
    private Button mBtnReset;
	private Button mBtnForget;
	
	public MyDialog(Context context) {
		super(context,R.style.common_dialog);
		setContentView(R.layout.dialog_layout);
		mBtnReset = (Button) findViewById(R.id.dialog_reset);
		mBtnForget = (Button) findViewById(R.id.dialog_forget);
		mBtnReset.setOnClickListener(this);
		mBtnForget.setOnClickListener(this);
	}
	
    /**
     * 点击事件
    @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {
		if (v.getId() == R.id.dialog_reset) {
		    mBtnClickListener.onReset();
		    Log.d(TAG, "onReset");
		}else{
		    mBtnClickListener.onForget();
		    Log.d(TAG, "onForget");
		}
		MyDialog.this.dismiss();
	}

	private OnBtnClickListener mBtnClickListener;
	
	public void setOnBtnClickListener (OnBtnClickListener l){
		this.mBtnClickListener = l;
	}
	
	public interface OnBtnClickListener {
		void onReset();
        void onForget();
	}
}
