package cn.cjp.wb.service;

import java.util.Map;

public class WBConstant {

	public static final String USER_AGENT = "Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	
	public static Map<String, String> LOGIN_COOKIE = null;
	public static void setLoginCookie(Map<String, String> map)
	{
		LOGIN_COOKIE = map;
	}
	
	/**
	 * 用于登录
	 * <br>
	 * Method : POST
	 */
	public static final String LOGIN_URL = "https://passport.sina.cn/sso/login";
	
	public static final String LOGIN_SUCCESS = "success";
	public static final String LOGIN_FAIL = "fail";
	
	/**
	 * 登录用户的首页，显示关注的用户的微博
	 * <br>
	 * Method : GET
	 * <br>
	 * 额外参数
	 * <br>
	 * page : 可以不加，但不可以为空，最小为1
	 * <br>
	 * next_cursor : 可以不加，但不可以为空
	 */
	public static final String HOME_PAGE_URL = "http://m.weibo.cn/index/feed?"
			+ "format=cards&uicode=20000174&rl=1";
	
	
	
}
