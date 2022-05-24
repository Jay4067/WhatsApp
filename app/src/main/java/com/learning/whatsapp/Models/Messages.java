package com.learning.whatsapp.Models;

public class Messages {

    String uId,message;
    Long timeStamp;
    String msgID;

    public Messages(String uId, String message, Long timeStamp) {
        this.uId = uId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public Messages(String message, Long timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public Messages() {
    }

    public Messages(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
