package com.example.yui.mailbox.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.bean.MailBean;
import com.example.yui.mailbox.dao.DBUtils;
import com.example.yui.mailbox.utils.MailReceiverUtils;
import com.example.yui.mailbox.view.SelectOperationWindow;


public class MailDetailActivity extends BaseActivity {

    private SelectOperationWindow mSelectOperationWindow;
    private WindowManager.LayoutParams params;

    private MailBean bean;

    private TextView sender, date, receiver, content, project;
    private WebView mWebView;

    private Toolbar toolbar;

    private LinearLayout body;
    private RelativeLayout progress;

    private String mailBox = "inbox";

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    progress.setVisibility(View.GONE);
                    body.setVisibility(View.VISIBLE);
                    setViewByBean(bean);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_detail);

        initView();
        initList();
        initToolBar();
    }

    private void initView() {

        bean = new MailBean();

        sender = (TextView)findViewById(R.id.sender);
        date = (TextView)findViewById(R.id.time);
        receiver = (TextView)findViewById(R.id.receiver);
        content = (TextView)findViewById(R.id.mail_content);
        project = (TextView)findViewById(R.id.project) ;

        mWebView = (WebView)findViewById(R.id.web_view);

        body = (LinearLayout)findViewById(R.id.body);
        progress = (RelativeLayout)findViewById(R.id.tips);
    }

    private void getMailBean(final int messageId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (DBUtils.queryTokenByMessageId(messageId) == 0){
                       MailReceiverUtils.updateMessageById(messageId, mailBox);
                    }
                    bean = DBUtils.queryMaiByMessageId(messageId);

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private void setViewByBean(MailBean bean){

        ContentValues values = new ContentValues();
        values.put("isNew", 0);
        DBUtils.update("maillist", "messageId",bean.getMessageId() + "", values);

        String sender_txt = bean.getSender();

        if (sender_txt != null && sender_txt.contains("1224269321@qq.com")) {
            sender.setText("我");
        } else {
            sender.setText(bean.getName());
        }

        date.setText(bean.getSentDate());
        project.setText(bean.getSubject());

        if (bean.getMailType() == 0){
            content.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            content.setText(bean.getMailContext());
        } else {
            content.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            WebSettings settings = mWebView.getSettings();

            // 设置可以支持缩放
            settings.setSupportZoom(true);
            // 设置出现缩放工具
            settings.setBuiltInZoomControls(true);

            //自适应屏幕
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            mWebView.getSettings().setLoadWithOverviewMode(true);

            mWebView.loadDataWithBaseURL("file://", bean.getMailContext(),"text/html", "UTF-8", "about:blank");
        }
    }

    private void initList() {
        Intent intent = getIntent();
        int messageId = Integer.parseInt(intent.getStringExtra("messageId"));

        getMailBean(messageId);
    }

    private void initToolBar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPopWindow() {
        mSelectOperationWindow = new SelectOperationWindow(this, null);
        //设置Popupwindow显示位置（从底部弹出）
        mSelectOperationWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha=0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mSelectOperationWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha=1f;
                getWindow().setAttributes(params);
            }
        });
    }

    public void onClick(View v){
        Intent intent = new Intent(this, WriteActivity.class);
        switch (v.getId()){
            case R.id.detail_btn:
                Toast.makeText(this, "详情", Toast.LENGTH_SHORT).show();
                break;
            case R.id.response:
                intent.putExtra("type", 0);
                intent.putExtra("name", bean.getName());
                intent.putExtra("address", bean.getSender());
                intent.putExtra("subject", bean.getSubject());
                startActivity(intent);
                mSelectOperationWindow.dismiss();
                break;
            case R.id.forwarding:
                intent.putExtra("type", 1);
                intent.putExtra("address", bean.getSender());
                intent.putExtra("subject", bean.getSubject());
                startActivity(intent);
                mSelectOperationWindow.dismiss();
                break;
            case R.id.temp_3:
                ContentValues values = new ContentValues();
                values.put("isNew", 1);
                DBUtils.update("maillist", "messageId", bean.getMessageId(), values);
                mSelectOperationWindow.dismiss();
                Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.temp_4:
                DBUtils.delete("maillist", "messageId", bean.getMessageId());
                Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.temp_5:
                break;
            case R.id.btn_send:
                initPopWindow();
                break;
        }
    }
}
