package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/*
 * 过渡动画，要有一个判断是否登录的操作，如果登录了，就直接跳到主界面，没有登录，则需要进行授权
*/
public class SplashActivity extends Activity {
	
	//Splash过渡的延迟时间
	private static final int SPLASH_DISPLAY_LENGHT = 3000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
/*		//去掉标题栏
		getWindow().requestFeature(Window.FEATURE_PROGRESS);*/
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SplashActivity.this, WBDemoMainActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
}
