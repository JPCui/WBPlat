package cn.cjp.weibo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import cn.cjp.weibo.R;
import cn.cjp.weibo.service.WBService;

public class Welcome extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			if (checkIfRemember() == 1)
				return;
		} catch (Exception e) {
		}

		setContentView(R.layout.welcome);
	}

	// 方法：从Preferences中读取用户名和密码
	public int checkIfRemember() throws Exception {
		SharedPreferences sp = getPreferences(MODE_PRIVATE); // 获得Preferences
		String uid = sp.getString("uid", null);
		String pwd = sp.getString("pwd", null);
		if (uid != null && pwd != null) {
			WBService service = new WBService();
			service.login(uid, pwd);
			Intent intent = new Intent(this, MainWeixin.class);
			startActivity(intent);
			finish();
			return 1;
		}
		return 0;
	}

	public void welcome_login(View v) {
		Intent intent = new Intent();
		intent.setClass(Welcome.this, Login.class);
		startActivity(intent);
	}

	public void welcome_register(View v) {
		Intent intent = new Intent();
		intent.setClass(Welcome.this, MainWeixin.class);
		startActivity(intent);
	}

}
