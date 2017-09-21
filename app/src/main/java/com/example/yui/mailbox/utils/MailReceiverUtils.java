package com.example.yui.mailbox.utils;

import android.content.ContentValues;
import android.util.Log;


import com.example.yui.mailbox.bean.MailBean;
import com.example.yui.mailbox.dao.DBUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

public class MailReceiverUtils {

    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    private static Store store;

    private static String USER, KEY;

    private static int lastPosition;

    public static void prepare(String user, String key){
        USER = user;
        KEY = key;

        // 创建一个有具体连接信息的Properties对象
        Properties prop = new Properties();
        prop.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.setProperty("mail.store.protocol", "imap");
        prop.setProperty("mail.imap.host", "imap.qq.com");
        prop.setProperty("mail.imap.port", "993");

        Session session = Session.getInstance(prop);
        session.setDebug(false);

        try{
            store = session.getStore("imap");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getLastPositionFromServer() throws Exception{
        // 连上邮件服务器
        if (!store.isConnected()) {
            store.connect(USER, KEY);
        }

        // 获得邮箱内的邮件夹
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);

        lastPosition = folder.getMessageCount();

        folder.close(false);
        store.close();
        return lastPosition;
    }

    public static void updateMessageById(int messageId, String box) throws Exception{
        // 连上邮件服务器
        if (!store.isConnected()) {
            store.connect(USER, KEY);
        }

        // 获得邮箱内的邮件夹
        Folder folder = store.getFolder(box);
        folder.open(Folder.READ_WRITE);

        Message message = folder.getMessage(messageId);

        ContentValues values = new ContentValues();
        values.put("token", 1);
        values.put("mailContent", getMailContent(message));

        DBUtils.update("maillist", "messageId", messageId + "", values);

        folder.close(false);
        store.close();
    }

    public static void receiveMail(String box, int start, int end) throws Exception{

        ArrayList<MailBean> list = new ArrayList<>();

        // 连上邮件服务器
        if (!store.isConnected()) {
            store.connect(USER, KEY);
        }

        // 获得邮箱内的邮件夹
        Folder folder = store.getFolder(box);
        folder.open(Folder.READ_WRITE);

        ContentValues values1 = new ContentValues();
        values1.put("lastNumber", folder.getMessageCount());
        DBUtils.update("user", "token", "1", values1);

        for (int i = end ; i >= start; i--){
            DBUtils.insertDateByMailBean(
                    packagingDate(folder.getMessage(i)));

            ContentValues values = new ContentValues();
            values.put("startNumber", i);
            DBUtils.update("user", "token", "1", values);
        }

        Collections.reverse(list);

        // 关闭
        folder.close(false);
        store.close();
    }

    private static MailBean packagingDate(Message message)
            throws Exception {
        MailBean bean = new MailBean();

        InternetAddress address[] = (InternetAddress[]) message.getFrom();
        bean.setName(address[0].getPersonal());

        bean.setMessageId(message.getMessageNumber() + "");
        Log.d("-------->", message.getMessageNumber() + "");

        bean.setSender(getFrom(message));

        bean.setReceiver(message.getReplyTo()[0].toString());

        bean.setSubject(getSubject(message));

        if (isNew(message)) {
            bean.setNew(1);
        }else{
            bean.setNew(0);
        }

        if (message.isMimeType("text/plain")){
            bean.setMailType(0);
        } else {
            bean.setMailType(1);
        }

        if (message.getFileName() != null){
            bean.setHasFile(1);
        }else{
            bean.setHasFile(0);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        bean.setSentDate(formatter.format(message.getSentDate()));

        formatter = new SimpleDateFormat("MM-dd");
        bean.setReceivedData(formatter.format(message.getReceivedDate()));

        bean.setMailContext("");

        return bean;
    }

    private static String getFrom(Message mimeMessage) throws Exception {
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        String from = address[0].getAddress();
        if (from == null)
            from = "";
        String personal = address[0].getPersonal();
        if (personal == null)
            personal = "";
        String fromaddr = personal + "<" + from + ">";
        return fromaddr;
    }

    /**
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同
     * "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
     */
    private static String getMailAddress(String type, Message mimeMessage) throws Exception {
        String mailaddr = "";
        String addtype = type.toUpperCase();
        InternetAddress[] address = null;
        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {
            if (addtype.equals("TO")) {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
            } else if (addtype.equals("CC")) {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
            } else {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
            }
            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    String email = address[i].getAddress();
                    if (email == null)
                        email = "";
                    else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = address[i].getPersonal();
                    if (personal == null)
                        personal = "";
                    else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    String compositeto = personal + "<" + email + ">";
                    mailaddr += "," + compositeto;
                }
                mailaddr = mailaddr.substring(1);
            }
        } else {
            throw new Exception("Error emailaddr type!");
        }
        return mailaddr;
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件
     * 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
     */
    private static String getMailContent(Part part) throws Exception {
        StringBuffer bodytext = new StringBuffer();//存放邮件内容
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");

        boolean conname = false;
        if (nameindex != -1)
            conname = true;

        if (part.isMimeType("text/plain") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                if (i == counts - 1)
                    bodytext.append(getMailContent(multipart.getBodyPart(i)));
                else
                    getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            bodytext.append(getMailContent((Part) part.getContent()));
        }
        return bodytext.toString();
    }

    /**
     * 判断此邮件是否包含附件
     */
    private static boolean isContainAttach(Part part) throws Exception {
        boolean attachflag = false;

        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE))))
                    attachflag = true;
                else if (mpart.isMimeType("multipart/*")) {
                    attachflag = isContainAttach((Part) mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().contains("application"))
                        attachflag = true;
                    if (contype.toLowerCase().contains("name"))
                        attachflag = true;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            attachflag = isContainAttach((Part) part.getContent());
        }
        return attachflag;
    }

    /**
     * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
     */
    private static boolean isNew(Message mimeMessage) throws MessagingException {
        boolean isnew = false;
        Flags flags = (mimeMessage).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();

        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                break;
            }
        }
        return !isnew;
    }

    /**
     * 获得邮件主题
     */
    private static String getSubject(Message mimeMessage) throws MessagingException {
        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (subject == null)
                subject = "";
        } catch (Exception exce) {}
        return subject;
    }

    public static void accountOrPasswordIsAvailable(final String user, final String pswd) throws Exception{

        // 创建一个有具体连接信息的Properties对象
        Properties prop = new Properties();
        prop.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.setProperty("mail.store.protocol", "imap");
        prop.setProperty("mail.imap.host", "imap.qq.com");
        prop.setProperty("mail.imap.port", "993");

        Session session = Session.getInstance(prop);
        session.setDebug(false);

        Store store1;

        store1 = session.getStore("imap");

        store1.connect("imap.qq.com", user, pswd);

        store1.close();
    }
}
