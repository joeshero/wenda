package com.jnu.wenda.model;

import lombok.Data;

/**
 * Created by Joe
 */
@Data
public class UserAndComment {

    private User user;
    private Comment comment;
    private long liked;
    private int status;
}
