package cn.cjp.weibo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.cjp.weibo.R;
import cn.cjp.weibo.bean.User;
import cn.cjp.weibo.service.WBService;

public class Login extends Activity {
	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框
	private WBService wbService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);
	}

	public void login_mainweixin(View v) throws Exception {
		if (wbService == null) {
			wbService = new WBService();
		}
		String uid = mUser.getText().toString();
		String pwd = mPassword.getText().toString();
		User user = wbService.login(uid, pwd); // 组织要返回的字符串

		if (null != user) // 判断 帐号和密码
		{
			rememberMe(uid, pwd);	//记住账号
			Intent intent = new Intent();
			intent.setClass(Login.this, LoadingActivity.class);
			startActivity(intent);
			this.finish();
		} else if ("".equals(uid) || "".equals(pwd)) // 判断 帐号和密码
		{
			new AlertDialog.Builder(Login.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("登录错误").setMessage("微信帐号或者密码不能为空，\n请输入后再登录！")
					.create().show();
		} else {
			new AlertDialog.Builder(Login.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("登录失败").setMessage("微信帐号或者密码不正确，\n请检查后重新输入！")
					.create().show();
		}

	}

	public void login_back(View v) { // 标题栏 返回按钮
		this.finish();
	}

	public void login_pw(View v) { // 忘记密码按钮
		Uri uri = Uri.parse("http://3g.qq.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	// 方法：将用户的id和密码存入Preferences
	public void rememberMe(String uid, String pwd) {
		SharedPreferences sp = getPreferences(MODE_PRIVATE); // 获得Preferences
		SharedPreferences.Editor editor = sp.edit(); // 获得Editor
		editor.putString("uid", uid); // 将用户名存入Preferences
		editor.putString("pwd", pwd); // 将密码存入Preferences
		editor.commit();
	}

	// 方法：从Preferences中读取用户名和密码
	public void checkIfRemember() {
		SharedPreferences sp = getPreferences(MODE_PRIVATE); // 获得Preferences
		String uid = sp.getString("uid", null);
		String pwd = sp.getString("pwd", null);
		if (uid != null && pwd != null) {
			mUser.setText(uid);
			mPassword.setText(pwd);
			// CheckBox cbRemember = (CheckBox) findViewById(R.id.cbRemember);
			// cbRemember.setChecked(true);
		}
	}
}
