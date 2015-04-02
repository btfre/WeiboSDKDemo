package com.sina.weibo.sdk.demo.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.sina.weibo.sdk.demo.activity.HomeActivity;

public class ImageLoader {

	private static final String TAG = HomeActivity.class.getName();
	
	/** download方法可以从给定URL下载图片并且将该图片赋给一个ImageView控件 */
	public void download(String url, ImageView imageView){
		BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
		task.execute(url);
	}
	
	
	/**
	 * BitmapDownloaderTask类继承自AsynTask，它提供下载图片的功能。
	 * 它的execute方法可以即时返回，因此速度非常快，从UI线程调用的
	 * 时候就不会感觉到有什么卡顿的感觉
	 */
	private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap>{
		private String url;
		private final WeakReference<ImageView> imageViewReference;
		public BitmapDownloaderTask(ImageView imageView){
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		
		// 这里是在下载线程中真正要执行的代码
		@Override
		protected Bitmap doInBackground(String... params) {
			// 参数params是从execute方法传递过来的，params[0]就是要下载的图片url
			return downloadBitmap(params[0]);
		}
		
		// 一旦图片下载完成，就将它在ImageView控件上显示出来
		@Override
		  protected void onPostExecute(Bitmap bitmap) {
		    if (isCancelled()) {
		      bitmap = null;
		    }
		    if (imageViewReference != null) {
		      ImageView imageView = imageViewReference.get();
		      if (imageView != null) {
		        imageView.setImageBitmap(bitmap);
		      }
		    }
		  }
		
	}
	
	// 从网上下载图片，返回Bitmap类型
	public Bitmap downloadBitmap(String url) {
		Bitmap bitmap = null;
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpResponse response = null;
		InputStream inputStream = null;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				Log.d(TAG, "func [loadImage] stateCode=" + stateCode);
				return bitmap;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					// BitmapFactory.decodeStream 在网络连接速度慢的情况下可能会导致图片编码失败
					return bitmap = BitmapFactory.decodeStream(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
			((AndroidHttpClient) client).close();
		}
		return bitmap;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int bytes = read();
					if (bytes < 0) {
						break; // 读到文件结束
					} else {
						bytesSkipped = 1; // 读一个字节
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

}
