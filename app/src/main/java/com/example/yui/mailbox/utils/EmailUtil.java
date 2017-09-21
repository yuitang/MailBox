package com.example.yui.mailbox.utils;

import android.content.Context;

import com.example.yui.mailbox.bean.MailSenderInfo;
import com.example.yui.mailbox.bean.UserBean;
import com.example.yui.mailbox.dao.DBUtils;


public class EmailUtil {
    public static void autoSendMail(Context context, String to,
                                    String subject, String content) {
        UserBean bean = DBUtils.getUserByToken();

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.qq.com");//smtp地址
        mailInfo.setMailServerPort("587");
        mailInfo.setValidate(true);

        mailInfo.setUserName(bean.getMailAddress());//邮箱名字
        mailInfo.setPassword(bean.getPassword());// 邮箱密码

        mailInfo.setFromAddress("1013954268@qq.com");// 发送方邮件地址
        mailInfo.setToAddress(to);//接受方邮件地址
        mailInfo.setSubject("Yui");
        mailInfo.setContent(content);
        // 这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);// 发送文体格式
        // sms.sendHtmlMail(mailInfo);// 发送html格式
    }
}
