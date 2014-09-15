package cn.cjp.wb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.cjp.wb.domain.User;
import cn.cjp.wb.service.WBService;

public class LoginActivity extends Activity {
	WBService wbService;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		checkIfRemember();
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(LoginActivity.this, "请稍候",
						"正在连接服务器...", true, true);
				login();
			}
		});
		ImageButton ibExit = (ImageButton) findViewById(R.id.ibExit);
		ibExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				android.os.Process.killProcess(android.os.Process.myPid()); // 结束进程，退出程序
			}
		});

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
			EditText etUid = (EditText) findViewById(R.id.etUid);
			EditText etPwd = (EditText) findViewById(R.id.etPwd);
			CheckBox cbRemember = (CheckBox) findViewById(R.id.cbRemember);
			etUid.setText(uid);
			etPwd.setText(pwd);
			cbRemember.setChecked(true);
		}
	}

	@Override
	protected void onDestroy() {
		if (null != pd)
		{
			pd.dismiss();
			pd.cancel();
		}
		super.onDestroy();
	}

	protected void login() {
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					if (wbService == null) {
						wbService = new WBService();
					}
					EditText etUid = (EditText) findViewById(R.id.etUid); // 获得帐号EditText
					EditText etPwd = (EditText) findViewById(R.id.etPwd); // 获得密码EditText
					String uid = etUid.getEditableText().toString().trim(); // 获得输入的帐号
					String pwd = etPwd.getEditableText().toString().trim(); // 获得输入的密码
					if (uid.equals("") || pwd.equals("")) { // 判断输入是否为空
						Toast.makeText(LoginActivity.this, "请输入帐号或密码!",
								Toast.LENGTH_SHORT).show();// 输出提示消息
						return;
					}
					User user = wbService.login(uid, pwd); // 组织要返回的字符串
					if (user != null) { // 收到的消息为登录成功消息
						CheckBox cb = (CheckBox) findViewById(R.id.cbRemember); // 获得CheckBox对象
						if (cb.isChecked()) {
							rememberMe(uid, pwd);
						}
						// 跳转
						Intent intent = new Intent(LoginActivity.this,
								WeibosPageActivity.class);
						intent.putExtra("uname", user.getScreenName());
						intent.putExtra("headUrl", user.getHeadUrl());
						startActivity(intent);
						if (null != pd)
						{
							pd.dismiss();
							pd.cancel();
						}
						finish();
					} else if (user == null) { // 收到的消息为登录失败
						Toast.makeText(LoginActivity.this, "账号或密码错误",
								Toast.LENGTH_LONG).show();
						if (null != pd)
						{
							pd.cancel();
						}
						Looper.loop();
						Looper.myLooper().quit();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}.start();
	}

}