package cn.cjp.weibo.service;

import java.io.IOException;
import java.util.HashMap;
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
//				.data("username", "1367471019@qq.com")
//				.data("password", "15238771688")
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

}
