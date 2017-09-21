package com.example.yui.mailbox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.base.BaseActivity;


public class ContactDetailActivity extends BaseActivity {

    private EditText name, address ;
    private TextView edit;

    private boolean EDIT_STATE = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail);

        initView();
    }

    private void initView() {
        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.mail_address);
        edit = (TextView)findViewById(R.id.edit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
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

    public void onClick(View v){
        switch (v.getId()){
            case R.id.edit:
                if (EDIT_STATE){
                    name.setFocusable(false);
                    name.setFocusableInTouchMode(false);
                    address.setFocusable(false);
                    address.setFocusableInTouchMode(false);

                    edit.setText("编辑");
                } else {
                    name.setFocusable(true);
                    name.setFocusableInTouchMode(true);
                    name.requestFocus();
                    address.setFocusable(true);
                    address.setFocusableInTouchMode(true);

                    edit.setText("保存");
                }
                EDIT_STATE = !EDIT_STATE;
                break;
            default:
                break;
        }
    }
}
