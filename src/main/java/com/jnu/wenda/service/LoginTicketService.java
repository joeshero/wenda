package com.jnu.wenda.service;

import com.jnu.wenda.dao.LoginTicketDao;
import com.jnu.wenda.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Joe
 */
@Service
public class LoginTicketService {

    @Autowired
    private LoginTicketDao loginTicketDao;

    public LoginTicket selectByTicket(String ticket){
        return loginTicketDao.selectByTicket(ticket);
    }

    public int addTicket(LoginTicket ticket) {
        return loginTicketDao.addTicket(ticket);
    }

    public void updateStatus(int status, String ticket) {
        loginTicketDao.updateStatus(status,ticket);
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        //有效时间100天
        now.setTime(3600 * 24 * 100 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicket.setStatus(0);
        addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    //退出
    public void logout(String ticket) {
        loginTicketDao.updateStatus(1,ticket);
    }


}
