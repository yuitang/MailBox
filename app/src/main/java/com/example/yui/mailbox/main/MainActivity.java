package com.example.yui.mailbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.activity.AddAccountActivity;
import com.example.yui.mailbox.activity.AddContactActivity;
import com.example.yui.mailbox.activity.FeedbackActivity;
import com.example.yui.mailbox.activity.SenderNameActivity;
import com.example.yui.mailbox.activity.SettingActivity;
import com.example.yui.mailbox.activity.SignatureActivity;
import com.example.yui.mailbox.activity.WriteActivity;
import com.example.yui.mailbox.adapter.FragmentPagerAdapter;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.view.GetPhotoWindow;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView mTextView ;
    private ImageView menu, addContact, write;

    private GetPhotoWindow mGetPhotoWindow;
    private WindowManager.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initContent();
    }

    private void initView(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu = (ImageView)findViewById(R.id.main_menu);
        addContact = (ImageView)findViewById(R.id.add_contact);
        write = (ImageView)findViewById(R.id.write_mail);

        menu.setVisibility(View.VISIBLE);
        addContact.setVisibility(View.GONE);
        write.setVisibility(View.VISIBLE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTextView = (TextView)findViewById(R.id.title_txt);
        mTextView.setText("收件箱");
    }

    //初始化内容区域
    private void initContent(){
        // 初始化ViewPager并设置适配器
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        // 初始化TabLayout并跟viewPage关联
        TabLayout tabLayout = (TabLayout) findViewById(R.id.bottom_bar);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        menu.setVisibility(View.VISIBLE);
                        addContact.setVisibility(View.GONE);
                        write.setVisibility(View.VISIBLE);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        mTextView.setText("收件箱");
                        break;
                    case 1:
                        menu.setVisibility(View.GONE);
                        addContact.setVisibility(View.VISIBLE);
                        write.setVisibility(View.GONE);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        mTextView.setText("联系人");
                        break;
                    case 2:
                        menu.setVisibility(View.GONE);
                        addContact.setVisibility(View.GONE);
                        write.setVisibility(View.GONE);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        mTextView.setText("我");
                        break;
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        tabLayout.setupWithViewPager(viewPager);
        // 设置MODE_FIXED避免TabLayout挤到一块去
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.shoujianxiang:
                mTextView.setText("收件箱");
                break;
            case R.id.caogaoxiang:
                mTextView.setText("草稿箱");
                break;
            case R.id.yifasong:
                mTextView.setText("已发送");
                break;
            case R.id.lajiyoujian:
                mTextView.setText("垃圾邮件");
                break;
            case R.id.yishanchu:
                mTextView.setText("已删除");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initPopWindow() {
        mGetPhotoWindow = new GetPhotoWindow(this, null);
        //设置Popupwindow显示位置（从底部弹出）
        mGetPhotoWindow.showAtLocation(findViewById(R.id.drawer_layout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha=0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mGetPhotoWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha=1f;
                getWindow().setAttributes(params);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.add_contact:
                Intent intent6 = new Intent(this, AddContactActivity.class);
                startActivity(intent6);
                break;
            case R.id.write_mail:
                Intent intent = new Intent(this, WriteActivity.class);
                startActivity(intent);
                break;
            case R.id.manage_account:
                Intent intent1 = new Intent(this, AddAccountActivity.class);
                startActivity(intent1);
                break;
            case R.id.signature_btn:
                Intent intent2 = new Intent(this, SignatureActivity.class);
                startActivity(intent2);
                break;
            case R.id.sender_name_btn:
                Intent intent3 = new Intent(this, SenderNameActivity.class);
                startActivity(intent3);
                break;
            case R.id.feedback_btn:
                Intent intent4 = new Intent(this, FeedbackActivity.class);
                startActivity(intent4);
                break;
            case R.id.setting_btn:
                Intent intent5 = new Intent(this, SettingActivity.class);
                startActivity(intent5);
                break;
            case R.id.change_img:
                initPopWindow();
                break;
            default:
                break;
        }
    }
}
