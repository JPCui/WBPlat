package cn.cjp.wb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.cjp.wb.util.PictureUtil;

public class HomePageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

		String name = intent.getStringExtra("uname");
		String headUrl = intent.getStringExtra("headUrl");
		Log.i("uname", name);
		Log.i("headUrl", headUrl);

		// Name
		TextView textView = new TextView(this);
		textView.setText(name);

		// Head
		ImageView imageView = new ImageView(this);
		if (headUrl != null && !headUrl.trim().equals(""))
		{
			Bitmap bitmap = PictureUtil.getHttpBitmap(headUrl, 40);
			imageView.setImageBitmap(bitmap);
		}
		
		LinearLayout layout = new LinearLayout(this);
		layout.setGravity(RESULT_CANCELED);
		layout.addView(textView, MODE_PRIVATE);
		layout.addView(imageView, MODE_PRIVATE);

		setContentView(layout);
	}

}