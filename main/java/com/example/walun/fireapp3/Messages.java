package com.example.walun.fireapp3;

/**
 * Created by walun on 26-08-2017.
 */

public class Messages {
    private String message;
    private String sender;
    private String sendername;
    private String time;
    private String imagery;

    public Messages(){

    }

    public Messages(String message, String sender,String sendername,String time,String imagery) {
        this.message = message;
        this.sender = sender;
        this.sendername=sendername;
        this.time=time;
        this.imagery=imagery;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message=message;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getSendername(){
        return sendername;
    }

    public void setSendername(String sendername){
        this.sendername=sendername;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImagery() {
        return imagery;
    }

    public void setImagery(String imagery) {
        this.imagery = imagery;
    }


}
