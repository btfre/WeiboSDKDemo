package com.sina.weibo.sdk.demo.adapter;

import java.util.List;
import java.util.Map;

import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.demo.utils.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;

	public HomeAdapter(Context context, List<Map<String, Object>> list) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.home_item_template, null);
			holder.profile_image = (ImageView) convertView
					.findViewById(R.id.profile_image);
			holder.screen_name = (TextView) convertView
					.findViewById(R.id.screen_name);
			holder.weibo_text = (TextView) convertView
					.findViewById(R.id.weibo_text);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.download((String)list.get(position)
				.get("profile_image_url"), holder.profile_image);
		
		holder.screen_name.setText((String) list.get(position).get(
				"screen_name"));
		holder.weibo_text
				.setText((String) list.get(position).get("weibo_text"));

		return convertView;
	}
	
	/** 用来优化ListView */
	public final class ViewHolder {
		/** 用户昵称view */
		public TextView screen_name;
		/** 用户头像view */
		public ImageView profile_image;
		/** 微博内容view */
		public TextView weibo_text;
	}

}
