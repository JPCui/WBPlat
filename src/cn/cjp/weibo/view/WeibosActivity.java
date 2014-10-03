package cn.cjp.weibo.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.cjp.weibo.activity.Login;
import cn.cjp.weibo.bean.HomeWeibo;
import cn.cjp.weibo.service.WBConstant;
import cn.cjp.weibo.service.WBService;

@Deprecated
public class WeibosActivity extends Activity {

	WebView weibosWebView = null;
	
	public WeibosActivity() {
	}
	public WeibosActivity(Context context) {
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		weibosWebView = new WebView(this);

		// 加载本地中的html
		weibosWebView.loadUrl("file:///android_asset/page/show.html");

		// 加上下面这段代码可以使网页中的链接不以浏览器的方式打开
		weibosWebView.setWebViewClient(new WebViewClient());
		// 得到webview设置
		WebSettings webSettings = weibosWebView.getSettings();
		// 允许使用javascript
		webSettings.setJavaScriptEnabled(true);
		// 将WebAppInterface于javascript绑定WebAppInterface
		weibosWebView.addJavascriptInterface(this,
				"WeibosController");

		setContentView(weibosWebView);

	}

	/**
	 * 获取主页微博（每页大小固定）
	 * 
	 * @param pageNum
	 *            页码
	 * @return
	 * @throws Exception
	 */
	public String getMainWeibos(String next_cursor, int pageNum)
			throws Exception {
		// 获取样例数据
		// AssetManager am = this.context.getAssets();
		// InputStream fis = am.open("res/json.txt");
		//
		// byte[] b = new byte[1024];
		// StringBuffer sb = new StringBuffer();
		// while ((fis.read(b) != -1)) {
		// sb.append(new String(b));
		// b = new byte[1024];
		// }
		//
		// String jsonStr = sb.toString();
		// HomeWeibo homeWeibo = HomeWeibo.fromJson(jsonStr);

		if (null != next_cursor && next_cursor.trim().equals(""))
			next_cursor = null;

		WBService wbService = new WBService();
		if (null == WBConstant.LOGIN_COOKIE) {
			toLogin();
			return null;
			// wbService.login("1367471019@qq.com", "15838228248");
		}
		HomeWeibo homeWeibo = wbService.getHomeWeibo(next_cursor, pageNum);

		String html = homeWeibo.toHtml();
		return html;
	}

	private void toLogin() {
		Intent intent = new Intent(WeibosActivity.this, Login.class);
		startActivity(intent);
		finish();
	}

}