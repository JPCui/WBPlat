package cn.cjp.wb;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.cjp.wb.domain.User;
import cn.cjp.wb.domain.Weibo;
import cn.cjp.wb.service.WBService;
import cn.cjp.wb.util.PictureUtil;

@TargetApi(Build.VERSION_CODES.FROYO)
public class WeibosPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibos);

		WBService wbService = new WBService();
		User loginUser = new User();

		// 获取登录用户的信息
		Intent intent = new Intent();
		if (null != intent.getStringExtra("uname"))
			loginUser.setScreenName(intent.getStringExtra("uname"));
		else
			loginUser.setScreenName("游客");
		if (null != intent.getStringExtra("headUrl"))
			loginUser.setHeadUrl(intent.getStringExtra("headUrl"));
		else
			loginUser.setHeadUrl("http://tp4.sinaimg.cn/1642909335/50/22867541466/0");
		
		((TextView)findViewById(R.id.login_uname)).setText(loginUser.getScreenName());
		Bitmap bitmap = PictureUtil.getHttpBitmap(loginUser.getHeadUrl(), 40);
		((ImageView)findViewById(R.id.login_uheader_url)).setImageBitmap(bitmap);
		loginUser = null;

		LinearLayout box = (LinearLayout) findViewById(R.id.weibo_box);
		List<Weibo> weibos = new ArrayList<Weibo>();
		try {
			weibos = wbService.getHomeWeibo(null, 1).getWeibos();
			Log.i("info", weibos.size()+"");
			for(Weibo weibo : weibos)
			{
				box.addView(createWeiboBox(box.getContext(), weibo));
			}
		} catch (Exception e) {
			Weibo weibo = new Weibo();
			weibo.setMid(123456L);
			weibo.setSource("Exception");
			weibo.setText("网络异常：\r\n"+e.getMessage()+"\r\n"+e);
			Log.e("CJP", "", e);
			
			User user = new User();
			user.setHeadUrl("http://tp4.sinaimg.cn/1642909335/50/22867541466/0");
			user.setScreenName("管理員");
			weibo.setUser(user);
			
			box.addView(createWeiboBox(box.getContext(), weibo));
		}
	}

	/**
	 * 创建WeiboBox
	 * 
	 * @param context
	 * @param user
	 * @param weibo
	 * @return
	 */
	public LinearLayout createWeiboBox(Context context, Weibo weibo) {
		LinearLayout box = new LinearLayout(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 3, 0, 0);
		box.setLayoutParams(params);
		box.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout head = createHeader(box.getContext(), weibo.getUser());
		RelativeLayout body = createBody(box.getContext(), weibo);

		box.addView(head);
		box.addView(body);
		return box;
	}

	/**
	 * 创建Body
	 * 
	 * @param context
	 * @param weibo
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private RelativeLayout createBody(Context context, Weibo weibo) {
		RelativeLayout body = new RelativeLayout(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		body.setLayoutParams(params);
		body.setBackgroundResource(R.drawable.preference_last_item);
		body.setClickable(true);
		body.setGravity(Gravity.CENTER_VERTICAL);

		TextView weiboView = createWBContentView(body.getContext(),
				weibo.getText());

		body.addView(weiboView);

		return body;
	}

	/**
	 * 创建微博内容View
	 * 
	 * @param context
	 * @param text
	 * @return
	 */
	private TextView createWBContentView(Context context, String text) {
		TextView view = new TextView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(0, 0, 0, 0);
		view.setLayoutParams(params);
		view.setPadding(5, 5, 5, 5);
		view.setText(text);
		view.setTextColor(Color.parseColor("#000000"));
		view.setTextSize(15);

		return view;
	}

	/**
	 * 创建Head区域
	 * 
	 * @param context
	 * @param user
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private RelativeLayout createHeader(Context context, User user) {
		RelativeLayout rel = new RelativeLayout(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rel.setLayoutParams(params);
		rel.setBackgroundResource(R.drawable.preference_first_item);

		ImageView headView = createHead(rel.getContext(), user.getHeadUrl());
		TextView nameView = createName(rel.getContext(), user.getScreenName());

		rel.addView(headView);
		rel.addView(nameView);

		return rel;
	}

	/**
	 * 创建昵称区域
	 * 
	 * @param context
	 * @param screenName
	 * @return
	 */
	private TextView createName(Context context, String screenName) {
		TextView view = new TextView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(80, 0, 0, 0);
		view.setLayoutParams(params);
		view.setPadding(5, 5, 5, 5);
		view.setText(screenName);
		view.setTextColor(Color.parseColor("#000000"));
		view.setTextSize(15);

		return view;
	}

	private ImageView createHead(Context context, String headUrl) {
		ImageView view = new ImageView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(0, 0, 0, 0);
		view.setLayoutParams(params);
		view.setScaleType(ScaleType.MATRIX);
		if (headUrl != null && !headUrl.trim().equals("")) {
			Bitmap bitmap = PictureUtil.getHttpBitmap(headUrl, 40);
			view.setImageBitmap(bitmap);
		}
		view.setPadding(5, 5, 5, 5);

		return view;
	}

}