package cn.cjp.weibo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import cn.cjp.weibo.R;

public class LoadingActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(LoadingActivity.this, Whatsnew.class);
				startActivity(intent);
				LoadingActivity.this.finish();
				Toast.makeText(getApplicationContext(), "��¼�ɹ�",
						Toast.LENGTH_SHORT).show();
			}
		}, 200);
	}
}