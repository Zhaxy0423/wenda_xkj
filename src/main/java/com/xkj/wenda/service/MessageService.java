package com.xkj.wenda.service;

import com.xkj.wenda.dao.MessageDao;
import com.xkj.wenda.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    @Autowired
    private  SensitiveService sensitiveService;

    /**
     * 改变已读状态
     * @param conversationId
     */
    public void updateMessageReadStatus(String conversationId) {
        messageDao.updateMessagesReadStatus(conversationId);
    }

    /**
     * 添加消息
     * @param message
     * @return
     */
    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));//过滤
        return messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }

    /**
     * 根据某个会话id得到所有消息（发送方与接收方）
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }

    /**
     * 根据某个用户id得到他的所有消息
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId, offset, limit);
    }

    /**
     * 根据某个用户得到他未读消息数量
     * @param userId
     * @param conversationId
     * @return
     */
    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnreadCount(userId, conversationId);
    }

}
