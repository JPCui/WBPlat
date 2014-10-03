package cn.cjp.weibo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

	WebView myWebView = null;
	
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myWebView = new WebView(this);

		// 加载本地中的html
		myWebView.loadUrl("file:///android_asset/page/show.html");

		// 加上下面这段代码可以使网页中的链接不以浏览器的方式打开
		myWebView.setWebViewClient(new WebViewClient());
		// 得到webview设置
		WebSettings webSettings = myWebView.getSettings();
		// 允许使用javascript
		webSettings.setJavaScriptEnabled(true);
		// 将WebAppInterface于javascript绑定WebAppInterface
		myWebView.addJavascriptInterface(this,
				"WeibosController");

		setContentView(myWebView);

	}

}