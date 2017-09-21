package com.example.yui.mailbox.bean;

public class MailBean {

    private String messageId;

    //发件人名称
    private String name;
    //发送人邮箱
    private String sender;
    //接受者邮箱
    private String receiver;
    //标题
    private String subject ;

    private String mailContext;
    //接收日期
    private String receivedData;
    //发送日期
    private String sentDate ;
    //是否是新消息
    private int isNew;

    private int hasFile;

    private int mailType;

    public int getMailType() {
        return mailType;
    }

    public void setMailType(int mailType) {
        this.mailType = mailType;
    }

    public String getMailContext() {
        return mailContext;
    }

    public void setMailContext(String mailContext) {
        this.mailContext = mailContext;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setHasFile(int hasFile) {
        this.hasFile = hasFile;
    }
    public int getHasFile() {
        return hasFile;
    }
    public String getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(String receiveData) {
        this.receivedData = receiveData;
    }

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentDate() {
        return sentDate;
    }
    public void setSentDate(String chatDate) {
        this.sentDate = chatDate;
    }

    public int getNew(){
        return isNew;
    }
    public void setNew(int isNew){
        this.isNew = isNew;
    }
}
