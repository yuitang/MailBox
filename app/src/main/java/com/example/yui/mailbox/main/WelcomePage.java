package com.example.yui.mailbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.bean.UserBean;
import com.example.yui.mailbox.dao.DBUtils;
import com.example.yui.mailbox.utils.ListSizeHelper;
import com.example.yui.mailbox.utils.Typefaces;


public class WelcomePage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        TextView splashTitle = (TextView) findViewById(R.id.splash_title);
        TextView youKe = (TextView) findViewById(R.id.youke);
        TextView version = (TextView) findViewById(R.id.version);

        ListSizeHelper.prepare(this);
        splashTitle.setTextSize(ListSizeHelper.fromPx(30));
        youKe.setTextSize(ListSizeHelper.fromPx(12));
        youKe.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        version.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        new Thread(start).start();
    }

    Runnable start = new Runnable() {
        @Override
        public void run() {
            Intent intent = null;
            try {
                UserBean bean = DBUtils.getUserByToken();
                if (bean != null){
                    intent = new Intent(WelcomePage.this, MainActivity.class);
                } else {
                    intent = new Intent(WelcomePage.this, LoginActivity.class);
                }
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                startActivity(intent);
                finish();
            }
        }
    };
}
