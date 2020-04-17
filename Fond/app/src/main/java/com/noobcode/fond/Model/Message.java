package com.noobcode.fond.Model;

public class Message {

    String sentby;
    String sentto;
    String message;
    String number;

    public Message(String message, String number, String sentby, String sentto) {
        this.sentby = sentby;
        this.sentto = sentto;
        this.message = message;
        this.number = number;
    }

    public String getSentby() {
        return sentby;
    }

    public String getSentto() {
        return sentto;
    }

    public String getMessage() {
        return message;
    }

    public String getNumber() {
        return number;
    }
}
