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
	 * ���ڵ�¼
	 * <br>
	 * Method : POST
	 */
	public static final String LOGIN_URL = "https://passport.sina.cn/sso/login";
	
	public static final String LOGIN_SUCCESS = "success";
	public static final String LOGIN_FAIL = "fail";
	
	/**
	 * ��¼�û�����ҳ����ʾ��ע���û���΢��
	 * <br>
	 * Method : GET
	 * <br>
	 * �������
	 * <br>
	 * page : ���Բ��ӣ���������Ϊ�գ���СΪ1
	 * <br>
	 * next_cursor : ���Բ��ӣ���������Ϊ��
	 */
	public static final String HOME_PAGE_URL = "http://m.weibo.cn/index/feed?"
			+ "format=cards&uicode=20000174&rl=1";
	
	
	
}
