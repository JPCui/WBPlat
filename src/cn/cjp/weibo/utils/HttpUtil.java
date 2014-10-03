package cn.cjp.weibo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.cjp.weibo.service.WBConstant;

public class HttpUtil {

	public Map<String, String> cookies = new HashMap<String, String>();
	public Map<String, String> header = new HashMap<String, String>();
	public Map<String, String> params = new HashMap<String, String>();

	public String exeHttp(String urlStr, String method) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlStr);
		for (String key : header.keySet()) {
			httpPost.setHeader(key, header.get(key));
		}
		String cookie = "";
		for (String key : cookies.keySet()) {
			cookie += key + "=" + cookies.get(key) + ";";
		}
		httpPost.setHeader("cookie", cookie);
		HttpResponse response = httpClient.execute(httpPost);
		httpPost = null;

		if (null != response.getFirstHeader("Set-Cookie")
				&& null == WBConstant.LOGIN_COOKIE) {
			Map<String, String> map = new HashMap<String, String>();
			String cookieStr = response.getFirstHeader("Set-Cookie").getValue()
					.trim();
			String[] subCookiesStrs = cookieStr.split(";");
			for (String subCookieStr : subCookiesStrs) {
				String[] strs = subCookieStr.trim().split("=");
				for (int j = 0; j + 1 < strs.length; j += 2) {
					map.put(strs[j], strs[j + 1]);
				}
			}
			WBConstant.setLoginCookie(map);
		}

		InputStreamReader isr = new InputStreamReader(response
				.getEntity().getContent());
		BufferedReader br = new BufferedReader(isr);
		String res = "";
		String line = null;
		while ((line = br.readLine()) != null) {
			res += line;
		}
		isr.close();
		br.close();
		isr = null;
		br = null;

		return res;
	}
	// public String exeHttp(String urlStr, String method) {
	// String result = null;
	// URL url = null;
	// HttpURLConnection connection = null;
	// InputStreamReader in = null;
	// try {
	// url = new URL(urlStr);
	// connection = (HttpURLConnection) url.openConnection();
	// connection.setDoOutput(true);
	// connection.setDoInput(true);
	// connection.setRequestMethod("POST");
	// connection.setRequestProperty("Content-Type",
	// "application/x-www-form-urlencoded; charset=UTF-8");
	// connection.setRequestProperty("Accept",
	// "application/json, text/javascript, */*; q=0.01");
	// // Cookies
	// if (null != cookies) {
	// String cookieStr = "";
	// for (String key : cookies.keySet()) {
	// cookieStr += key + "=" + cookies.get(key) + ";";
	// }
	// connection.setRequestProperty("Cookie", cookieStr);
	// }
	// // Props
	// if (null != header)
	// for (String key : header.keySet()) {
	// connection.setRequestProperty(key, header.get(key));
	// }
	// // Params
	// DataOutputStream os = new DataOutputStream(
	// connection.getOutputStream());
	// if (null != params)
	// for (String key : params.keySet()) {
	// os.writeBytes(key + "="
	// + URLEncoder.encode(params.get(key), "UTF-8") + "&");
	// os.flush();
	// }
	// os.close();
	// connection.connect();
	//
	// // Set-Cookie
	// if(null == WBConstant.LOGIN_COOKIE)
	// {
	// Map<String, List<String>> map = connection.getHeaderFields();
	// Map<String, String> set_cookies = new HashMap<String, String>();
	// if (null != map.get("Set-Cookie")) {
	// List<String> strList = map.get("Set-Cookie");
	// for (String str : strList) {
	// String[] strArray = str.split("=");
	// set_cookies.put(strArray[0].trim(), strArray[1].trim());
	// }
	// }
	// WBConstant.setLoginCookie(set_cookies);
	// }
	//
	// in = new InputStreamReader(connection.getInputStream(), "UTF-8");
	// BufferedReader bufferedReader = new BufferedReader(in);
	// StringBuffer strBuffer = new StringBuffer();
	// String line = null;
	// while ((line = bufferedReader.readLine()) != null) {
	// strBuffer.append(line);
	// }
	// result = strBuffer.toString();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (connection != null) {
	// connection.disconnect();
	// }
	// if (in != null) {
	// try {
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
	// return CodeUtil.unicodeToString(result);
	// }

}
