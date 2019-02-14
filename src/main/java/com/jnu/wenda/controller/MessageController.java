package com.jnu.wenda.controller;

import com.jnu.wenda.model.HostHolder;
import com.jnu.wenda.model.Message;
import com.jnu.wenda.model.User;
import com.jnu.wenda.model.UserAndMessage;
import com.jnu.wenda.service.MessageService;
import com.jnu.wenda.service.UserService;
import com.jnu.wenda.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe
 */
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @GetMapping("/msg/list")
    public String getCnversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/";
        }
        model.addAttribute("user", hostHolder.getUser());
        int userId = hostHolder.getUser().getId();
        List<Message> messageList = messageService.getMessage(userId, 0, 10);
        List<UserAndMessage> vos = new ArrayList<>();
        for (Message message : messageList) {
            UserAndMessage vo = new UserAndMessage();
            int id = message.getFromId() == userId ? message.getToId() : message.getFromId();
            vo.setUser(userService.selectById(id));
            vo.setMessage(message);
            vo.setMessageCount(messageService.getMessageCount(message.getConversationId()));
            vo.setUnReadCount(messageService.getUnreadCount(message.getConversationId()));
            vos.add(vo);

        }
        model.addAttribute("vos", vos);
        return "letter";
    }

    @GetMapping("/msg/detail")
    public String getCOnversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId) {
        if (hostHolder.getUser() == null) {
            return "redirect:/";
        }
        model.addAttribute("user", hostHolder.getUser());
        try {
            List<Message> messageList = messageService.getConversationDatail(conversationId, 0, 10);
            List<UserAndMessage> vos = new ArrayList<>();
            for (Message message : messageList) {
                UserAndMessage vo = new UserAndMessage();
                vo.setMessage(message);
                vo.setUser(userService.selectById(message.getFromId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            messageService.updateHasRead(conversationId);
        } catch (Exception e) {
            logger.error("获取详情失败" + e.getMessage());
        }


        return "letterDetail";
    }

    @PostMapping("/msg/addMessage")
    @ResponseBody
    public String addMesage(@RequestParam("toName") String toName,
                            @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            //查看是否有发送者
            User fromUser = hostHolder.getUser();

            User user = userService.selectByName(toName);
            if (user == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }
            if (fromUser.getName().equals(user.getName())) {
                return WendaUtil.getJSONString(1,"不能给自己发送私信");
            }


            Message message = new Message();
            message.setCreateDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("消息发送失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发送失败");
        }
    }
}
