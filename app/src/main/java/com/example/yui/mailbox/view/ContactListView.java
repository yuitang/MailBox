package com.example.yui.mailbox.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yui.mailbox.adapter.ContactAdapter;
import com.example.yui.mailbox.utils.ListSizeHelper;
import com.example.yui.mailbox.utils.Utils;


/** 自定义国家列表控件listview */
public class ContactListView extends RelativeLayout implements OnTouchListener {
	private GroupListView lvContries;
	private TextView tvScroll;
	private LinearLayout llScroll;
	private ContactAdapter adapter;

	public ContactListView(Context context) {
		this(context, null);
	}

	public ContactListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ContactListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		ListSizeHelper.prepare(context);

		lvContries = new GroupListView(context);
		int resId = Utils.getBitmapRes(context, "cl_divider");
		if (resId > 0) {
			lvContries.setDivider(context.getResources().getDrawable(resId));
		}
		lvContries.setDividerHeight(ListSizeHelper.fromPxWidth(1));
		adapter = new ContactAdapter(lvContries, context);
		lvContries.setAdapter(adapter);
		LayoutParams lpContries = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		int dp_9 = ListSizeHelper.fromPxWidth(12);

		lvContries.setPadding(dp_9, 0, 3*dp_9, 0);
		addView(lvContries, lpContries);

		tvScroll = new TextView(context);
		resId = Utils.getColorRes(context, "sendcloud_white");
		if (resId > 0) {
			tvScroll.setTextColor(context.getResources().getColor(resId));
		}
		resId = Utils.getBitmapRes(context, "sdk_country_group_scroll_down");
		if (resId > 0) {
			tvScroll.setBackgroundResource(resId);
		}
		tvScroll.setTextSize(TypedValue.COMPLEX_UNIT_PX, ListSizeHelper.fromPxWidth(80));
		tvScroll.setTypeface(Typeface.DEFAULT);
		tvScroll.setVisibility(GONE);
		tvScroll.setGravity(Gravity.CENTER);
		int dp_80 = ListSizeHelper.fromPxWidth(120);
		LayoutParams lp = new LayoutParams(dp_80, dp_80);
		lp.addRule(CENTER_IN_PARENT);
		addView(tvScroll, lp);

		llScroll = new LinearLayout(context);
		resId = Utils.getBitmapRes(context, "sdk_country_group_scroll_up");
		if (resId > 0) {
			llScroll.setBackgroundResource(resId);
		}
		llScroll.setOrientation(LinearLayout.VERTICAL);
		llScroll.setOnTouchListener(this);
		lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_RIGHT);
		lp.addRule(CENTER_VERTICAL);
		lp.rightMargin = ListSizeHelper.fromPxWidth(7);
		addView(llScroll, lp);

		initScroll(context);
	}

	private void initScroll(Context context) {
		llScroll.removeAllViews();
		ListSizeHelper.prepare(context);

		int size = adapter.getGroupCount();
		int dp_3 = ListSizeHelper.fromPxWidth(6);
		int dp_2 = ListSizeHelper.fromPxWidth(4);
		for (int i = 0; i < size; i++) {
			TextView tv = new TextView(context);
			tv.setText(adapter.getGroupTitle(i));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ListSizeHelper.fromPxWidth(18));
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(dp_3, dp_2, dp_3, dp_2);
			tv.setTextColor(Color.parseColor("#bfbfbf"));
			llScroll.addView(tv);
		}
	}

	/** 设置列表右边的字母索引的滑动监听**/
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				int resId = Utils.getBitmapRes(v.getContext(), "sdk_country_group_scroll_down");
				if (resId > 0) {
					v.setBackgroundResource(resId);
				}
				float x = event.getX();
				float y = event.getY();
				ViewGroup vg = (ViewGroup) v;
				onScroll(vg, x, y);
			}
			break;
			case MotionEvent.ACTION_MOVE: {
				float x = event.getX();
				float y = event.getY();
				ViewGroup vg = (ViewGroup) v;
				onScroll(vg, x, y);
			}
			break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				int resId = Utils.getBitmapRes(v.getContext(), "sdk_country_group_scroll_up");
				if (resId > 0) {
					v.setBackgroundResource(resId);
				}
				tvScroll.setVisibility(GONE);
			}
			break;
		}
		return true;
	}

	/** 设置列表右边的字母索引的滑动时的显示*/
	public void onScroll(ViewGroup llScroll, float x, float y) {
		for (int i = 0, count = llScroll.getChildCount(); i < count; i++) {
			TextView v = (TextView)llScroll.getChildAt(i);
			if (x >= v.getLeft() && x <= v.getRight()
					&& y >= v.getTop() && y <= v.getBottom()) {

				lvContries.setSelection(i);
				tvScroll.setVisibility(VISIBLE);
				tvScroll.setText(v.getText());
				break;
			}
		}
	}

	/**
	 * 设置listview item 的点击事件监听
	 * @param listener
	 */
	public void setOnItemClickListener(GroupListView.OnItemClickListener listener) {
		lvContries.setOnItemClickListener(listener);
	}

	/**
	 * 获取国家对象<国家名，区号， ID>
	 * @param group
	 * @param position
	 * @return
	 */
	public String[] getCountry(int group, int position) {
		return adapter.getItem(group, position);
	}

}
