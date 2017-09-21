package com.example.yui.mailbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.activity.ContactDetailActivity;
import com.example.yui.mailbox.utils.ListSizeHelper;
import com.example.yui.mailbox.utils.SearchEngine;
import com.example.yui.mailbox.utils.Utils;
import com.example.yui.mailbox.view.GroupListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/** 自定义的国家列表，适配器，用于填充国家listview*/
public class ContactAdapter extends GroupListView.GroupAdapter {

	private HashMap<String, ArrayList<String[]>> countryList;
	private HashMap<String, ArrayList<String[]>> rawData;
	private ArrayList<String> titles;
	private ArrayList<ArrayList<String[]>> countries;
	private Context context;
	private SearchEngine sEngine;

	private static int mark = 0;
	private LinkedList<String> list;

	public ContactAdapter(GroupListView view, Context context) {
		super(view);
		this.context = context;

		rawData = getCountryList();
		list = new LinkedList<>();

		initSearchEngine();
		search(null);
	}

	public static void setMark(int m){
		mark = m;
	}

	private void initSearchEngine() {
		sEngine = new SearchEngine();
		ArrayList<String> countries = new ArrayList<>();
		for (Map.Entry<String, ArrayList<String[]>> ent : rawData.entrySet()) {
			ArrayList<String[]> cl = ent.getValue();
			for (String[] paire : cl) {
				countries.add(paire[0]);
			}
		}
		sEngine.setIndex(countries);
	}

	private void search(String token) {
		ArrayList<String> res = sEngine.match(token);
		boolean isEmptyToken = false;
		if (res == null || res.size() <= 0) {
			res = new ArrayList<>();
			isEmptyToken = true;
		}

		HashMap<String, String> resMap = new HashMap<>();
		for (String r : res) {
			resMap.put(r, r);
		}

		titles = new ArrayList<>();
		countries = new ArrayList<>();
		for (Map.Entry<String, ArrayList<String[]>> ent : rawData.entrySet()) {
			ArrayList<String[]> cl = ent.getValue();
			ArrayList<String[]> list = new ArrayList<>();
			for (String[] paire : cl) {
				if (isEmptyToken || resMap.containsKey(paire[0])) {
					list.add(paire);
				}
			}
			if (list.size() > 0) {
				titles.add(String.valueOf(ent.getKey()));
				countries.add(list);
			}
		}
	}

	private HashMap<String, ArrayList<String[]>> getCountryList() {
		//String language = DeviceHelper.getInstance(this).getAppLanguage();

		if(this.countryList != null && this.countryList.size() > 0) {
			return this.countryList;
		} else {
			LinkedHashMap list = null;

			for(char title = 65; title <= 90; ++title) {
				String tips = "country_group_" + Character.toLowerCase(title);
				int resId = Utils.getStringArrayRes(context, tips);
				if(resId > 0) {
					String[] countrys = context.getResources().getStringArray(resId);
					ArrayList<String[]> itemList = null;
					if(countrys != null) {
						for (String item:countrys) {
							String[] items = item.split(",");
							if(itemList == null) {
								itemList = new ArrayList<>();
							}
							itemList.add(items);
						}
					}
					if(itemList != null) {
						if(list == null) {
							list = new LinkedHashMap();
						}
						list.put(title, itemList);
					}
				}
			}
			this.countryList = list;
			return this.countryList;
		}
	}

	public int getGroupCount() {
		return titles == null ? 0 : titles.size();
	}

	public int getCount(int group) {
		if (countries == null) {
			return 0;
		}

		ArrayList<String[]> list = countries.get(group);
		if (list == null) {
			return 0;
		}

		return list.size();
	}

	public String getGroupTitle(int group) {
		if(titles.size() != 0){
			return titles.get(group);
		}else{
			return null;
		}
	}

	public String[] getItem(int group, int position) {
		String[] countriesArray = null;
		if(countries.size() != 0){
			try {
				countriesArray = countries.get(group).get(position);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return countriesArray;
		}else{
			return null;
		}
	}

	/** 获取组标题的view,如 组 A*/
	public View getTitleView(int group, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LinearLayout ll = new LinearLayout(parent.getContext());
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setBackgroundColor(0xffffffff);
			convertView = ll;

			ListSizeHelper.prepare(parent.getContext());

			TextView tv = new TextView(parent.getContext());
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ListSizeHelper.fromPxWidth(16));
			int resId = Utils.getColorRes(parent.getContext(), "smssdk_lv_title_color");
			if (resId > 0) {
				tv.setTextColor(parent.getContext().getResources().getColor(resId));
			}
			int dp_6 = ListSizeHelper.fromPxWidth(14);
			tv.setPadding(0, dp_6, 0, dp_6);
			tv.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ll.addView(tv);

			View vDiv = new View(parent.getContext());
			vDiv.setBackgroundColor(0xffe3e3e3);
			ll.addView(vDiv, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
		}

		String title = getGroupTitle(group);
		TextView tv = (TextView) ((LinearLayout) convertView).getChildAt(0);
		tv.setText(title);
		return convertView;
	}

	/** listview 滑动时，改变组的标题 */
	public void onGroupChange(View titleView, String title) {
		TextView tv = (TextView) ((LinearLayout) titleView).getChildAt(0);
		tv.setText(title);
	}

	/** 设置国家列表listview组中的item项 */
	public View getView(int group, final int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_contact_list,
					parent, false);
		} else {
			view = convertView;
		}
		TextView textView = (TextView)view.findViewById(R.id.cantact_name);
		final ImageView imageView = (ImageView)view.findViewById(R.id.add_btn);

		final String[] data = getItem(group, position);

		if (mark == 0){
			imageView.setVisibility(View.GONE);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, ContactDetailActivity.class);
					context.startActivity(intent);
				}
			});
		} else {
			imageView.setVisibility(View.INVISIBLE);
			if (list.contains(data[0])){
				imageView.setVisibility(View.VISIBLE);
			}

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (imageView.getVisibility() == View.VISIBLE){
						if (list.contains(data[0])){
							list.remove(data[0]);
						}
						imageView.setVisibility(View.INVISIBLE);
					} else {
						if (!list.contains(data[0])){
							list.add(data[0]);
						}
						imageView.setVisibility(View.VISIBLE);
					}
				}
			});
		}

		if(data != null){
			textView.setText(data[0]);
		}
		return view;
	}
}
