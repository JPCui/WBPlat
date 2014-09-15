package cn.cjp.wb.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 圖片處理類
 * @author REAL
 *
 */
public class PictureUtil {

	/**
	 * 从服务器取图片 http://bbs.3gstdy.com
	 * 
	 * @param url 圖片大小
	 * @param px 將圖片大小降低為px
	 * @return 
	 */
	public static Bitmap getHttpBitmap(String url, int px) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println(url+"");
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int h = bitmap.getHeight();
		int w = bitmap.getWidth();
		int t = h/px;
		int th = h/t;
		int tw = w/t;
		
		return Bitmap.createBitmap(bitmap, 0, 0, tw, th);
	}
	
}
