package cn.cjp.wb.service;

import org.json.JSONObject;

import android.util.Log;
import cn.cjp.wb.domain.HomeWeibo;
import cn.cjp.wb.domain.User;
import cn.cjp.wb.domain.Weibo;

public class WBService {

	WBConnect conn = new WBConnect();

	public User login(String uid, String pwd) throws Exception {
		String loginMsg = conn.login(uid, pwd);
		if (null == loginMsg || loginMsg.trim().equals(WBConstant.LOGIN_FAIL)) {
			return null;
		} else {
			JSONObject jsonObj = new JSONObject(loginMsg);
			if(null!=jsonObj.get("retcode") && jsonObj.getInt("retcode") == 50011015)
			{
				return null;
			}
			
			User user = conn.getUserFromLogin(loginMsg);
			Log.i("cjp_info", "user logined : " + user.getScreenName());
			Log.i("cjp_info", "user headUrl : " + user.getHeadUrl());
			return user;
		}
	}

	/**
	 * 
	 * @param next_cursor
	 * @param page
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
