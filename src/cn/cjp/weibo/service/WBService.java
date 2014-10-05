package cn.cjp.weibo.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import cn.cjp.weibo.bean.HomeWeibo;
import cn.cjp.weibo.bean.User;
import cn.cjp.weibo.bean.Weibo;

public class WBService {

	public User login(String uid, String pwd) throws Exception {
		WBConnect conn = new WBConnect();
		String loginMsg = conn.login(uid, pwd);
		if (null == loginMsg || loginMsg.trim().equals(WBConstant.LOGIN_FAIL)) {
			return null;
		} else {
			JSONObject jsonObj = new JSONObject(loginMsg);
			if (null != jsonObj.get("retcode")
					&& jsonObj.getInt("retcode") == 50011015) {
				return null;
			}

			User user = conn.getUserFromLogin(loginMsg);
			Log.i("cjp_info", "user logined : " + user.getScreenName());
			Log.i("cjp_info", "user headUrl : " + user.getHeadUrl());
			return user;
		}
	}

	/**
	 * 获取主页微博
	 * 
	 * @param next_cursor
	 *            可为null
	 * @param page
	 *            最小为1
	 * @return
	 * @throws Exception
	 */
	public HomeWeibo getHomeWeibo(String next_cursor, int page)
			throws Exception {
		WBConnect conn = new WBConnect();
		String homeWeiboJson = conn.getHomePage(next_cursor, page);
		if (null == homeWeiboJson)
			return new HomeWeibo();
		HomeWeibo weibo = HomeWeibo.fromJson(homeWeiboJson);
		homeWeiboJson = null;

		return weibo;
	}

	/**
	 * 
	 * @param f
	 *            图片文件
	 * @return 返回 picId
	 * @throws Exception
	 */
	public String addPic(File f) {
		try {

			String bound = "---------------------------20226268138071";

			List<byte[]> bts = new ArrayList<byte[]>();

			String ts = "--"
					+ bound
					+ "\r\n"
					+ "Content-Disposition: form-data; name=\"type\""
					+ "\r\n\r\n"
					+ "json"
					+ "\r\n"
					+ "--"
					+ bound
					+ "\r\n"
					+ "Content-Disposition: form-data; name=\"pic\"; filename=\"2.png\""
					+ "\r\n" + "Content-Type: image/png" + "\r\n\r\n";
			bts.add(ts.getBytes());
			ts = "";

			// 写入图片流
			int size = (int) f.length();
			byte[] data = new byte[size];
			try {
				FileInputStream fis = new FileInputStream(f);
				fis.read(data, 0, size);
				fis.close();
				bts.add(data);
			} catch (Exception e) {
				return e.getMessage();
			}

			ts += "\r\n--" + bound + "--\r\n";
			bts.add(ts.getBytes());
			ts = "";

			WBConnect wbConnect = new WBConnect();

			String str = "";
			try {
				str = wbConnect.upload(WBConstant.ADD_PIC_URL,
						bound, bts);
			} catch (Exception e) {
				return e.getMessage();
			}

			JSONObject jsonObj = null;
			try {
				jsonObj = new JSONObject(str);
				ts = jsonObj.getString("pic_id");
			} catch (JSONException e) {
				return e.getMessage();
			}

			return ts;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 
	 * @param f
	 *            图片文件
	 * @return 返回 picId
	 * @throws Exception
	 */
	public String addPic(byte[] bs) throws Exception {
		String bound = "---------------------------20226268138071";

		List<byte[]> bts = new ArrayList<byte[]>();

		String ts = "--"
				+ bound
				+ "\r\n"
				+ "Content-Disposition: form-data; name=\"type\""
				+ "\r\n\r\n"
				+ "json"
				+ "\r\n"
				+ "--"
				+ bound
				+ "\r\n"
				+ "Content-Disposition: form-data; name=\"pic\"; filename=\"2.png\""
				+ "\r\n" + "Content-Type: image/png" + "\r\n\r\n";
		bts.add(ts.getBytes());
		ts = "";

		// 写入图片流
		bts.add(bs);

		ts += "\r\n--" + bound + "--\r\n";
		bts.add(ts.getBytes());
		ts = "";

		WBConnect wbConnect = new WBConnect();

		String str = "";
		try {
			str = wbConnect.upload("http://m.weibo.cn/mblogDeal/addPic", bound,
					bts);
		} catch (Exception e) {
			return e.getMessage();
		}

		JSONObject jsonObj = new JSONObject(str);

		return jsonObj.getString("pic_id");
	}

	/**
	 * 
	 * @param content
	 * @param picIds
	 * @return 1 则正常
	 * @throws JSONException
	 */
	public int addWeibo(String content, String[] picIds) throws JSONException {
		WBConnect wbConnect = new WBConnect();

		String data = "";
		data += "content=" + content;
		if (null != picIds && picIds.length > 0) {
			for (String picId : picIds)
				data += picId;
		}

		wbConnect.pubWeibo(data);
		return 1;
	}

	public static void main(String[] args) throws Exception {
		WBConnect conn = new WBConnect();

		conn.login("1367471019@qq.com", "15238771688");

		String homeWeiboJson = conn.getHomePage("", 1);
		HomeWeibo weibo = HomeWeibo.fromJson(homeWeiboJson);

		for (Weibo wb : weibo.getWeibos()) {
			System.out.println(wb.getUser().getScreenName() + " : "
					+ wb.getText());
		}
	}

}
