package com.jnu.wenda.service;

import com.jnu.wenda.controller.LoginController;
import com.jnu.wenda.dao.UserDao;
import com.jnu.wenda.model.User;
import com.jnu.wenda.utils.WendaUtil;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Joe
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketService loginTicketService;

    public void addUser(User user) {
        userDao.addUser(user);
    }

    public User selectById(int id) {
        return userDao.selectById(id);
    }

    public void updatePassword(User user) {
        userDao.updatePassword(user);
    }

    public User selectByName(String name){
        return userDao.selectByName(name);
    }

    public void deleteById(int id) {
        userDao.deleteUser(id);
    }

    public Map<String, String> register(String name, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(password)) {
            logger.error("用户名或密码为空");
            map.put("msg", "用户名或密码不能为空");
            return map;
        }

        //查询用户名是否已经存在
        User user = userDao.selectByName(name);
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }
        user = new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDao.addUser(user);

        //登录成功，记录ticket
        String ticket = loginTicketService.addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;

    }

    public Map<String, String> login(String name, String password) {
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(password)) {

            map.put("msg", "用户名或密码不能为空");
            return map;
        }

        //查询用户名是否已经存在
        User user = userDao.selectByName(name);
        if (user == null) {
            map.put("msg", "用户不存在");
            return map;
        }
        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "用户名或密码不正确");
            return map;
        }
        //登录成功，记录ticket
        String ticket = loginTicketService.addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;

    }


    public void logout(String ticket) {
        loginTicketService.logout(ticket);
    }


}
