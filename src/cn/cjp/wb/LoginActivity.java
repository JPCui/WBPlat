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
				pd = ProgressDialog.show(LoginActivity.this, "���Ժ�",
						"�������ӷ�����...", true, true);
				login();
			}
		});
		ImageButton ibExit = (ImageButton) findViewById(R.id.ibExit);
		ibExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				android.os.Process.killProcess(android.os.Process.myPid()); // �������̣��˳�����
			}
		});

	}

	// ���������û���id���������Preferences
	public void rememberMe(String uid, String pwd) {
		SharedPreferences sp = getPreferences(MODE_PRIVATE); // ���Preferences
		SharedPreferences.Editor editor = sp.edit(); // ���Editor
		editor.putString("uid", uid); // ���û�������Preferences
		editor.putString("pwd", pwd); // ���������Preferences
		editor.commit();
	}

	// ��������Preferences�ж�ȡ�û���������
	public void checkIfRemember() {
		SharedPreferences sp = getPreferences(MODE_PRIVATE); // ���Preferences
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
					EditText etUid = (EditText) findViewById(R.id.etUid); // ����ʺ�EditText
					EditText etPwd = (EditText) findViewById(R.id.etPwd); // �������EditText
					String uid = etUid.getEditableText().toString().trim(); // ���������ʺ�
					String pwd = etPwd.getEditableText().toString().trim(); // ������������
					if (uid.equals("") || pwd.equals("")) { // �ж������Ƿ�Ϊ��
						Toast.makeText(LoginActivity.this, "�������ʺŻ�����!",
								Toast.LENGTH_SHORT).show();// �����ʾ��Ϣ
						return;
					}
					User user = wbService.login(uid, pwd); // ��֯Ҫ���ص��ַ���
					if (user != null) { // �յ�����ϢΪ��¼�ɹ���Ϣ
						CheckBox cb = (CheckBox) findViewById(R.id.cbRemember); // ���CheckBox����
						if (cb.isChecked()) {
							rememberMe(uid, pwd);
						}
						// ��ת
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
					} else if (user == null) { // �յ�����ϢΪ��¼ʧ��
						Toast.makeText(LoginActivity.this, "�˺Ż��������",
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