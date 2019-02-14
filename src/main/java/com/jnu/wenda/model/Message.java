package com.jnu.wenda.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by Joe
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createDate;
    private int hasRead;
    private String conversationId;


    public String getConversationId() {
        if (fromId < toId) {
            return String.format("%d_%d", fromId, toId);
        }else{
            return String.format("%d_%d", toId, fromId);
        }
    }
}
