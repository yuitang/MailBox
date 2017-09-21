package com.example.yui.mailbox.Application;


import com.example.yui.mailbox.bean.MailBean;
import com.example.yui.mailbox.bean.UserBean;
import com.example.yui.mailbox.dao.DBUtils;
import com.example.yui.mailbox.utils.MailReceiverUtils;

import java.util.ArrayList;

public class MailApplication extends android.app.Application {

    private ArrayList<MailBean> mMailBeanList;

    private String USER, KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        mMailBeanList = new ArrayList<>();

        DBUtils.prepare(this);

        UserBean bean = DBUtils.getUserByToken();;
        if (bean != null){
            setUserAndKey(bean.getMailAddress(), bean.getPassword());
        }
    }

    public void setUserAndKey(String user, String key){
        USER = user;
        KEY = key;

        MailReceiverUtils.prepare(user, key);
    }

    public ArrayList<MailBean> getmMailBeanList(){
        return mMailBeanList;
    }
}
