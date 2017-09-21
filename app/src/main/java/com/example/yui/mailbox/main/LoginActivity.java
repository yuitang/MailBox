package com.example.yui.mailbox.main;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.Application.MailApplication;
import com.example.yui.mailbox.base.BaseActivity;
import com.example.yui.mailbox.dao.DBUtils;
import com.example.yui.mailbox.utils.MailReceiverUtils;


public class LoginActivity extends BaseActivity implements TextWatcher{

    private EditText account, password;
    private ImageView del_accounnt, del_password, look ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_account);

        initView();
        initToolbar();
    }

    private void initToolbar() {
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

    private void initView() {
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.password);

        del_accounnt = (ImageView)findViewById(R.id.del_edtx);
        del_password = (ImageView)findViewById(R.id.cancel_password);
        look = (ImageView)findViewById(R.id.look);
        look.setClickable(false);

        account.addTextChangedListener(this);
        password.addTextChangedListener(this);
    }

    private void setAccount(final String account, final String password) {
        if (account.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "正在验证。。。", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {

            private void startIntent(){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void run() {
                ContentValues values = new ContentValues();
                try {
                    MailReceiverUtils.accountOrPasswordIsAvailable(account, password);
                }catch (Exception e){
                    if (e instanceof javax.mail.AuthenticationFailedException){
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "账户或密码错误",
                                Toast.LENGTH_SHORT).show();
                        Looper.loop();

                        return;
                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "未知错误",
                                Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        return;
                    }
                }

                if (DBUtils.hasAccountOrNot(account)){
                    values.put("mailAddress", account);
                    values.put("password", password);
                    DBUtils.update("user", "mailAddress",
                            account, values);
                    values.clear();

                    startIntent();
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "修改账户成功",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    values.put("mailAddress", account);
                    values.put("password", password);
                    values.put("mailAddress", account);
                    values.put("password", password);
                    values.put("userName", "未命名");
                    values.put("lastNumber", 0);
                    values.put("token", 1);
                    DBUtils.insertDate("user", values);
                    values.clear();

                    ((MailApplication)getApplication()).setUserAndKey(account, password);

                    startIntent();
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "添加账户成功",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:
                setAccount(account.getText().toString().trim(),
                        password.getText().toString().trim());
                break;
            case R.id.del_edtx:
                account.setText("");
                break;
            case R.id.cancel_password:
                password.setText("");
                break;
            case R.id.look:
                if (!look.isClickable()){
                    return;
                }
                if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    look.setImageResource(R.drawable.close_password);
                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    look.setImageResource(R.drawable.open_password);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (account.getText().toString().isEmpty()){
            del_accounnt.setVisibility(View.GONE);
        } else {
            del_accounnt.setVisibility(View.VISIBLE);
        }

        if (password.getText().toString().isEmpty()){
            look.setClickable(false);
            del_password.setVisibility(View.GONE);
        } else {
            look.setClickable(true);
            del_password.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {}
}
