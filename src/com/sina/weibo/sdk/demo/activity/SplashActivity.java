package com.sina.weibo.sdk.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.R;

/*
 * 过渡动画，要有一个判断是否登录的操作，如果登录了，就直接跳到主界面，没有登录，则需要进行授权
*/
public class SplashActivity extends Activity {
	
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
	private Oauth2AccessToken mAccessToken;
	//Splash过渡的延迟时间
	private static final int SPLASH_DISPLAY_LENGHT = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		


		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

		        mAccessToken = AccessTokenKeeper.readAccessToken(SplashActivity.this);
		        Intent intent = new Intent();
		        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
		        // 第一次启动本应用，AccessToken 不可用
		        if (mAccessToken.isSessionValid()) {
		        	intent.setClass(SplashActivity.this, WBDemoMainActivity.class);
					startActivity(intent);
					finish();
		        }else{
		        	intent.setClass(SplashActivity.this, WBAuthActivity.class);
					startActivity(intent);
					finish();
		        }
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
}
