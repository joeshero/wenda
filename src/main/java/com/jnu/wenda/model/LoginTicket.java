package com.jnu.wenda.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by Joe
 */
@Data
public class LoginTicket {

    private int id;
    private int userId;
    private Date expired;
    private String ticket;
    private int status;

}
