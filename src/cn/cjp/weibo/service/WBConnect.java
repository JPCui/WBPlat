package cn.cjp.weibo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import android.util.Log;
import cn.cjp.weibo.bean.User;
import cn.cjp.weibo.utils.CodeUtil;
import cn.cjp.weibo.utils.HttpUtil;

public class WBConnect {

	static Map<String, String> cookies = null;

	/**
	 * 登录
	 * 
	 * @param uid
	 *            账号
	 * @param pwd
	 *            密码
	 * @return 登录状态
	 */
	public String login(String uid, String pwd) {
		Response response = null;
		Connection conn = Jsoup
				.connect(WBConstant.LOGIN_URL)
				.header("Referer",
						"https://passport.sina.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F")
				.header("User-Agent",
						"Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")

				.data("username", uid)
				.data("password", pwd)
				// .data("username", "1367471019@qq.com")
				// .data("password", "15238771688")
				.data("savestate", "1")
				.data("ec", "0")
				.data("pagerefer",
						"http://passport.sina.cn/sso/logout?entry=mweibo&r=http%3A%2F%2Fm.weibo.cn%2Flogin")
				.data("entry", "mweibo");

		conn.method(Method.POST);
		int times = 3;
		while ((times--) != 0) {
			try {
				response = conn.execute();
				break;
			} catch (IOException e) {
				Log.d("CJP_DEBUG", "link to sina err : " + e.getMessage());
			}
		}
		if (null == response)
			return null;
		WBConstant.setLoginCookie(response.cookies());
		return response.body();
	}

	/**
	 * 获取关注的用户的微博
	 * 
	 * @param next_cursor
	 * @param page
	 *            页码
	 * @return json
	 * @throws Exception
	 */
	public String getHomePage(String next_cursor, int page) throws Exception {
		String homePageUrl = WBConstant.HOME_PAGE_URL;

		if (null != next_cursor && !next_cursor.trim().equals(""))
			homePageUrl += "&next_cursor=" + next_cursor;
		if (page > 0)
			homePageUrl += "&page=" + page;

		HttpUtil httpUtil = new HttpUtil();
		httpUtil.cookies = WBConstant.LOGIN_COOKIE;
		String text = httpUtil.exeHttp(homePageUrl, "GET");

		return text;
	}

	public User getUserFromLogin(String loginMsg) throws Exception {
		// 获取uid
		JSONObject jsonObj = new JSONObject(loginMsg);
		String uid = jsonObj.getJSONObject("data").getString("uid");

		HttpUtil httpUtil = new HttpUtil();
		httpUtil.header = new HashMap<String, String>();
		httpUtil.header
				.put("User-Agent",
						"Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

		httpUtil.cookies = WBConstant.LOGIN_COOKIE;
		String text = httpUtil.exeHttp("http://m.weibo.cn/u/" + uid, "GET");

		Element elem = new Element(Tag.valueOf("<>"), "");
		elem.append(text);
		System.out.println("Text : " + elem.text());
		String screenName = "";
		int i = text.indexOf("\"name\"") + 8;
		for (;; i++) {
			if (text.charAt(i) == '\"')
				break;
			screenName += text.charAt(i);
		}

		String headUrl = "";
		i = text.indexOf("profile_image_url") + 20;
		for (;; i++) {
			if (text.charAt(i) == '\"')
				break;
			headUrl += text.charAt(i);
		}

		User user = new User();
		user.setUid(CodeUtil.unicodeToString(uid));
		user.setScreenName(CodeUtil.unicodeToString(screenName));
		user.setHeadUrl(CodeUtil.unicodeToString(headUrl));

		return user;
	}

	public String getHeadUrl(String uid) throws Exception {
		String uMegUrl = "http://m.weibo.cn/users/" + uid;
		String headUrl = "";

		HttpUtil httpUtil = new HttpUtil();
		httpUtil.header = new HashMap<String, String>();
		httpUtil.header
				.put("User-Agent",
						"Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		httpUtil.cookies = new HashMap<String, String>();
		for (String key : cookies.keySet()) {
			httpUtil.cookies.put(key, cookies.get(key));
		}

		String text = httpUtil.exeHttp(uMegUrl, "GET");

		int i = text.indexOf("profile_image_url") + 19;
		for (;; i++) {
			if (text.charAt(i) == '\"')
				break;
			headUrl += text.charAt(i);
		}

		return headUrl;
	}

	/**
	 * 上传方法 返回上传完毕的文件名
	 * 
	 * @return 如果正常，返回：{"ok":1,"msg":null,"pic_url":
	 *         "http:\/\/ww4.sinaimg.cn\/thumbnail\/da66c124jw1el0lhc07x7j200w0owmxg.jpg","pic_id":"da66c124jw1el0lhc07x7j200w0owmxg"
	 *         } <br>
	 *         如果通信异常，返回：{\"ok\":0,\"msg\":"000"}
	 */
	public String upload(String urlStr, String boundary, List<byte[]> data) {
		try {
			// 服务器IP(这里是从属性文件中读取出来的)
			URL url = new URL(urlStr);

			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setConnectTimeout(5000);
			uc.setReadTimeout(30000);
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setRequestMethod("POST");
			// 上传图片的一些参数设置
			uc.setRequestProperty("Accept",
					"application/json, text/javascript, */*; q=0.01");
			// uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
			uc.setRequestProperty("Connection", "keep-alive");
			uc.setRequestProperty("Content-type",
					"multipart/form-data; boundary=" + boundary);
			
			Map<String, String> cookieMap = WBConstant.LOGIN_COOKIE;
			String cookieStr = "";
			for (String key : cookieMap.keySet()) {
				cookieStr += key + "=" + cookieMap.get(key) + ";";
			}
			uc.setRequestProperty(
					"Cookie",
					cookieStr);
			
			uc.setRequestProperty("Referer", "http://m.weibo.cn/mblog");
			uc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
			uc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			uc.setDoOutput(true);
			uc.setUseCaches(true);

			OutputStream out = uc.getOutputStream();
			for (byte[] bs : data) {
				out.write(bs);
			}
			out.flush();
			out.close();

			// 读取响应数据
			int code = uc.getResponseCode();

			String sCurrentLine = "";
			// 存放响应结果
			String sTotalString = "";
			if (code == 200) {
				java.io.InputStream is = uc.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((sCurrentLine = reader.readLine()) != null)
					if (sCurrentLine.length() > 0)
						sTotalString += sCurrentLine.trim();
			} else {
				sTotalString = "{\"ok\":0,\"msg\":" + code + "}";
			}
			return sTotalString;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * @param data
	 * @return 
	 *         如果正常，返回（{"id":"3762426509592884","ok":1,"msg":"\u53d1\u5e03\u6210\u529f"
	 *         } ），pic_id为server为图片分配的id
	 */
	public String pubWeibo(String data) {
		try {
			// 服务器IP(这里是从属性文件中读取出来的)
			URL url = new URL("http://m.weibo.cn/mblogDeal/addAMblog");

			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setConnectTimeout(5000);
			uc.setReadTimeout(30000);
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setRequestMethod("POST");
			// 上传图片的一些参数设置
			uc.setRequestProperty("Accept",
					"application/json, text/javascript, */*; q=0.01");
			uc.setRequestProperty("Connection", "keep-alive");
			Map<String, String> cookieMap = WBConstant.LOGIN_COOKIE;
			String cookieStr = "";
			for (String key : cookieMap.keySet()) {
				cookieStr += key + "=" + cookies.get(key) + ";";
			}
			uc.setRequestProperty("Cookie", cookieStr);
			uc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
			uc.setRequestProperty("Referer", "http://m.weibo.cn/mblog");
			uc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			uc.setDoOutput(true);
			uc.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			uc.setUseCaches(true);

			OutputStream out = uc.getOutputStream();
			out.write(data.getBytes());
			out.flush();
			out.close();

			// 读取响应数据
			int code = uc.getResponseCode();

			String sCurrentLine = "";
			// 存放响应结果
			String sTotalString = "";
			if (code == 200) {
				java.io.InputStream is = uc.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((sCurrentLine = reader.readLine()) != null)
					if (sCurrentLine.length() > 0)
						sTotalString = sTotalString + sCurrentLine.trim();
			} else {
				sTotalString = "{\"ok\":0,\"msg\":" + code + "}";
			}
			return sTotalString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
