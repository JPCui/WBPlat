package cn.cjp.weibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import cn.cjp.weibo.R;

public class InfoXiaoheiHead extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_xiaohei_head);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}