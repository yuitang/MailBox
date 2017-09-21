package com.example.yui.mailbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.example.yui.mailbox.R;
import com.example.yui.mailbox.adapter.AccountAdapter;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.bean.UserBean;
import com.example.yui.mailbox.dao.DBUtils;
import com.example.yui.mailbox.main.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class AddAccountActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private AccountAdapter adapter;

    private List<UserBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account);

        initList();
        initView();
    }

    private void initList() {
        list = new ArrayList();
    }

    private void initView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.account_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new AccountAdapter(list);
        mRecyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        list.clear();
        list.addAll(DBUtils.queryUser());
        adapter.notifyDataSetChanged();
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
            case R.id.add_btn:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
