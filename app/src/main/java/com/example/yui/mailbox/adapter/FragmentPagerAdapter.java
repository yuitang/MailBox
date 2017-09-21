package com.example.yui.mailbox.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.fragment.ContactsFragment;
import com.example.yui.mailbox.fragment.MailListFragment;
import com.example.yui.mailbox.fragment.PersonalFragment;


public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private int imageResId[] = {
            R.drawable.setting,
            R.drawable.setting,
            R.drawable.setting,
            R.drawable.setting
    };

    private String tabTitles[] = {"邮件", "联系人", "我"};

    final int PAGE_COUNT = 3;

    private Context mContext;

    public FragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return ContactsFragment.newInstance(position);
            case 2:
                return PersonalFragment.newInstance(position);
        }

        return MailListFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        // 设置文字和图片
        // Generate title based on item position
        Drawable image = mContext.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * 自定义tab
     * 如果需要每个TAB都需要指定成单独的布局，switch即可，如果是相同的，写一个即可
     * 这里自定义的不是Fragment的布局，不要搞混了，仅仅是TAB的样式
     */
    public View getTabView(int position) {
        View view  = null;

        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }

        return view;
    }
}
