package org.terna.chitchat;

import java.util.Date;

/**
 * Created by user on 02/06/2018.
 */

public class chatMessage {

    private String messagetext;
    private String messageUser;
    private long messageTime;

    public chatMessage(String messagetext, String messageUser) {
        this.messagetext = messagetext;
        this.messageUser = messageUser;

        messageTime = new Date().getTime();
    }

    public chatMessage()
    {}

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
