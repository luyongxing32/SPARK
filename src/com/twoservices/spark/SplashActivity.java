/**
 * @author Ry
 * @date   2012.12.01
 * @filename SplashActivity.java 
 */

package com.twoservices.spark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SplashActivity extends FragmentActivity {

	private static final String TAG = SplashActivity.class.getSimpleName();
	
	private static final int SPLASH_SHOW_DURATION = 3000;

	private Thread mLoadingThread;

	Handler mHandlerLoadingDone = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "Loading has been done.");
			moveLoginAcitivty();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		ImageView splashView = (ImageView) findViewById(R.id.splash_img);
		
		splashView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mLoadingThread != null)
					mLoadingThread.interrupt();
				
				SplashActivity.this.mHandlerLoadingDone.sendEmptyMessage(0);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		mLoadingThread = new Thread(new LoadRunnable());
		mLoadingThread.start();
	}

	private class LoadRunnable implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(SPLASH_SHOW_DURATION);

				SplashActivity.this.mHandlerLoadingDone.sendEmptyMessage(0);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void moveLoginAcitivty() {
		Intent intent = new Intent(SplashActivity.this,	LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
}
