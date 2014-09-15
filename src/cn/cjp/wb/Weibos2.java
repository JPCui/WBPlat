package cn.cjp.wb;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Weibos2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout first = new LinearLayout(this);
		first.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		first.setOrientation(LinearLayout.VERTICAL);
		first.setBackgroundColor(Color.WHITE);

		ScrollView second = new ScrollView(first.getContext());
		second.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		second.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		second.setBackgroundColor(Color.YELLOW);

		LinearLayout linearLayout = new LinearLayout(second.getContext());
		linearLayout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setBackgroundColor(Color.GRAY);

		RelativeLayout relaLayout = new RelativeLayout(
				linearLayout.getContext());
		relaLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		relaLayout.setBackgroundColor(Color.BLUE);
		
		ListView listView = new ListView(relaLayout.getContext());

		// Head
		ImageView imageView = new ImageView(relaLayout.getContext());

		imageView.setScaleType(ScaleType.MATRIX);
		imageView.setImageResource(R.drawable.icon);

		MarginLayoutParams margin = new MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		margin.leftMargin = 8;
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				margin);
		imageView.setLayoutParams(rlp);
		imageView.setPadding(8, 8, 8, 8);

		// Name
		// new layout
		margin = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rlp = new RelativeLayout.LayoutParams(margin);
		rlp.leftMargin = 8;
		rlp.topMargin = 1;
		TextView nameView = new TextView(relaLayout.getContext());
		nameView.setLayoutParams(rlp);
		nameView.setPadding(15, 15, 15, 15);
		nameView.setText("name");
		nameView.setTextColor(Color.parseColor("#000000"));
		nameView.setTextSize(18);

		// Weibo
		// new layout
		margin = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rlp = new RelativeLayout.LayoutParams(margin);
		// rlp.topMargin = 50;
		rlp.leftMargin = 95;
		TextView weiboView = new TextView(relaLayout.getContext());
		weiboView.setLayoutParams(rlp);
		weiboView.setPadding(15, 15, 15, 15);
		weiboView
				.setText("weiboView weiboView weiboView weiboView weiboView weiboView ");
		weiboView.setTextColor(Color.parseColor("#303030"));
		weiboView.setTextSize(18);

		relaLayout.addView(imageView);
		relaLayout.addView(nameView);
		relaLayout.addView(weiboView);

		linearLayout.addView(relaLayout);

		second.addView(linearLayout);
		first.addView(second);
		setContentView(first);

	}

}