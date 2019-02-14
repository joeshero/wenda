package com.jnu.wenda.model;

import lombok.Data;

/**
 * Created by Joe
 */
@Data
public class UserAndMessage {

    private User user;
    private Message message;
    private int messageCount;
    private int unReadCount;
}
