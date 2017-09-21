package com.example.yui.mailbox.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.dao.DBUtils;

public class SenderNameActivity extends BaseActivity {

    private EditText sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_name);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");

        sign = (EditText)findViewById(R.id.user_name);
        sign.setText(DBUtils.getUserName());

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

    public void onClick(View v){
        switch (v.getId()){
            case R.id.preserve:
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                ContentValues values = new ContentValues();
                values.put("userName", sign.getText().toString().trim());
                DBUtils.update("user", "token", "1", values);
                finish();
                break;
            default:
                break;
        }
    }
}
