package cn.cjp.weibo.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

	public Map<String, String> cookies = null;
	public Map<String, String> set_cookies = null;
	public Map<String, String> header = null;
	public Map<String, String> params = null;

	public String exeHttp(String urlStr, String method) {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Accept",
					"application/json, text/javascript, */*; q=0.01");
			// Cookies
			if (null != cookies) {
				String cookieStr = "";
				for (String key : cookies.keySet()) {
					cookieStr += key + "=" + cookies.get(key) + ";";
				}
				connection.setRequestProperty("Cookie", cookieStr);
			}
			// Props
			if (null != header)
				for (String key : header.keySet()) {
					connection.setRequestProperty(key, header.get(key));
				}
			// Params
			DataOutputStream os = new DataOutputStream(
					connection.getOutputStream());
			if (null != params)
				for (String key : params.keySet()) {
					os.writeBytes(key + "="
							+ URLEncoder.encode(params.get(key), "UTF-8") + "&");
					os.flush();
				}
			os.close();
			connection.connect();

			// Set-Cookie
			Map<String, List<String>> map = connection.getHeaderFields();
			set_cookies = new HashMap<String, String>();
			if (null != map.get("Set-Cookie")) {
				List<String> strList = map.get("Set-Cookie");
				for (String str : strList) {
					String[] strArray = str.split("=");
					set_cookies.put(strArray[0].trim(), strArray[1].trim());
				}
			}

			in = new InputStreamReader(connection.getInputStream(), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}

	/**
	 * 上传方法 返回上传完毕的文件名 *
	 */
	public String upload(File f) {
		String bound = "haha";
		try {
			// 服务器IP(这里是从属性文件中读取出来的)
			URL url = new URL("http://m.weibo.cn/mblogDeal/addPic");

			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("POST");
			// 上传图片的一些参数设置
			uc.setRequestProperty("Accept",
					"application/json, text/javascript, */*; q=0.01");
			uc.setRequestProperty("Accept-Language", "zh-cn");
			uc.setRequestProperty("Content-type",
					"multipart/form-data;   boundary=" + bound);
			uc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
			uc.setRequestProperty("Referer", "http://m.weibo.cn/mblog");
			uc.setRequestProperty(
					"Cookie",
					"_T_WM=da11bb49ecbdab69035457a72cdb1c04; gsid_CTandWM=4uGEe7771Ao5fJzea7HnGfndt3i; SUB=_2AkMjc-RZa8NlrABXnvwUymzgbIlH-jyQL_SvAn7oJhMyCBh87lAGqScV3_Ai1mDTD599DwFyNNpSj_RZCA..; M_WEIBOCN_PARAMS=uicode%3D20000165%26rl%3D1%26luicode%3D20000174; H5_INDEX=0_all; H5_INDEX_TITLE=%E5%A4%A7%E4%BE%A0%E8%A6%81%E5%90%83%E8%82%AF%E5%BE%B7%E5%9F%BA");
			uc.setRequestProperty("Accept-Charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setUseCaches(true);

			// 读取文件流
			int size = (int) f.length();
			byte[] data = new byte[size];
			OutputStream out = uc.getOutputStream();

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
					+ "Content-Disposition: form-data; name=\"pic\"; filename=\""
					+ f.getName().trim() + "\"" + "\r\n"
					+ "Content-Type: image/png" + "\r\n\r\n";
			out.write(ts.getBytes("UTF-8"));

			// 写入图片流
			FileInputStream fis = new FileInputStream(f);
			fis.read(data, 0, size);
			out.write(data);

			out.flush();
			out.close();
			fis.close();

			// 读取响应数据
			int code = uc.getResponseCode();

			System.out.println(uc.getContent());

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
				sTotalString = "远程服务器连接失败,错误代码:" + code;
			}
			return sTotalString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传方法 返回上传完毕的文件名 *
	 */
	public String upload(String boundary, List<byte[]> data) {
		try {
			// 服务器IP(这里是从属性文件中读取出来的)
			URL url = new URL("http://m.weibo.cn/mblogDeal/addPic");

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
			uc.setRequestProperty(
					"Cookie",
					"_T_WM=9d5a84109595f7cc195a22c726f1ef1a;"
					+ "gsid_CTandWM=4uGEe7771Ao5fJzea7HnGfndt3i;"
					+ "SUB=_2AkMjc-RZa8NlrABXnvwUymzgbIlH-jyQL_SvAn7oJhMyCBh87lAGqScV3_Ai1mDTD599DwFyNNpSj_RZCA..;"
					+ "M_WEIBOCN_PARAMS=uicode%3D20000174%26rl%3D1");
			uc.setRequestProperty("Referer", "http://m.weibo.cn/mblog");
			uc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
			uc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			uc.setDoOutput(true);
			uc.setUseCaches(true);

			OutputStream out = uc.getOutputStream();
			for(byte[] bs : data)
			{
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
						sTotalString = sTotalString + sCurrentLine.trim();
			} else {
				sTotalString = "远程服务器连接失败,错误代码:" + code;
			}
			return sTotalString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String pubWeibo(String data)
	{
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
			// uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
			uc.setRequestProperty("Connection", "keep-alive");
			uc.setRequestProperty(
					"Cookie",
					"_T_WM=9d5a84109595f7cc195a22c726f1ef1a;"
					+ "gsid_CTandWM=4uGEe7771Ao5fJzea7HnGfndt3i;"
					+ "SUB=_2AkMjc-RZa8NlrABXnvwUymzgbIlH-jyQL_SvAn7oJhMyCBh87lAGqScV3_Ai1mDTD599DwFyNNpSj_RZCA..;"
					+ "M_WEIBOCN_PARAMS=uicode%3D20000174%26rl%3D1");
			uc.setRequestProperty("Referer", "http://m.weibo.cn/mblog");
			uc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
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
				sTotalString = "远程服务器连接失败,错误代码:" + code;
			}
			return sTotalString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
