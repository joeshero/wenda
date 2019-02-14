package com.jnu.wenda.service;

import com.jnu.wenda.dao.MessageDao;
import com.jnu.wenda.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Joe
 */
@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    //添加消息
    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }

    //获取对话
    public List<Message> getConversationDatail(String conversaionId, int offset, int limit) {
        return messageDao.getConversationDetail(conversaionId, offset, limit);
    }

    //获取当前用户相关所有消息
    public List<Message> getMessage(int userId, int offset, int limit) {
        return messageDao.getMessage(userId, offset, limit);
    }

    //获取当前对话消息总数
    public int getMessageCount(String conversationId) {
        return messageDao.getMessageCount(conversationId);
    }

    //获取当前对话未读消息总数
    public int getUnreadCount(String conversationId) {
        return messageDao.getUnreadCount(conversationId);
    }

    //将未读变为已读
    public void updateHasRead(String conversationId) {
        messageDao.updateHasRead(conversationId);
    }
}
