package com.sina.weibo.sdk.demo.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.Constants;
import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.demo.R.id;
import com.sina.weibo.sdk.demo.R.layout;
import com.sina.weibo.sdk.demo.adapter.HomeAdapter;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

public class HomeActivity extends Activity {
	private static final String TAG = HomeActivity.class.getName();
	/** 当前 Token 信息 */
	private Oauth2AccessToken mAccessToken;
	/** 用于获取微博信息流等操作的API */
	private StatusesAPI mStatusesAPI;
	/** 微博列表（头像，昵称，内容） */
	private List<Map<String, Object>> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// 获取当前已保存过的 Token
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		// 对statusAPI实例化
		mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);

		new Thread() {
			public void run() {
				if (mAccessToken != null && mAccessToken.isSessionValid()) {
					// 获取当前登录用户及其所关注用户的最新微博
					mStatusesAPI.friendsTimeline(0L, 0L, 10, 1, false, 0,
							false, mListener);
				}
			}
		}.start();

	}

	/**
	 * 微博 OpenAPI 回调接口。 所有获取微博数目和信息都放在statuses.statusList中
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				LogUtil.i(TAG, response);
				if (response.startsWith("{\"statuses\"")) {
					// 调用 StatusList#parse 解析字符串成微博列表对象
					StatusList statuses = StatusList.parse(response);
					if (statuses != null && statuses.total_number > 0) {
						// Toast.makeText(HomeActivity.this,
						// "获取微博信息流成功, 条数: " + statuses.statusList.size(),
						// Toast.LENGTH_LONG).show();

						list = new ArrayList<Map<String, Object>>();

						for (int i = 0; i < statuses.statusList.size(); i++) {
							Map<String, Object> listItem = new HashMap<String, Object>();
							listItem.put(
									"profile_image_url",
									statuses.statusList.get(i).user.profile_image_url);
							listItem.put("screen_name",
									statuses.statusList.get(i).user.screen_name);
							listItem.put("weibo_text",
									statuses.statusList.get(i).text);
							list.add(listItem);
						}
						HomeAdapter myAdapter = new HomeAdapter(HomeActivity.this, list);

						ListView list = (ListView) findViewById(R.id.mylist);
						list.setAdapter(myAdapter);
					}
				} else if (response.startsWith("{\"created_at\"")) {
					// 调用 Status#parse 解析字符串成微博对象
					Status status = Status.parse(response);
					Toast.makeText(HomeActivity.this,
							"发送一送微博成功, id = " + status.id, Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(HomeActivity.this, response,
							Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			LogUtil.e(TAG, e.getMessage());
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(HomeActivity.this, info.toString(),
					Toast.LENGTH_LONG).show();
		}

	};

}
