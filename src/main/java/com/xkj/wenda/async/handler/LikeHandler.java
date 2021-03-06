package com.xkj.wenda.async.handler;

import com.xkj.wenda.Utils.WendaUtil;
import com.xkj.wenda.async.EventHandler;
import com.xkj.wenda.async.EventModel;
import com.xkj.wenda.async.EventType;
import com.xkj.wenda.model.Message;
import com.xkj.wenda.model.User;
import com.xkj.wenda.service.MessageService;
import com.xkj.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));
        // 这里的会话ID肯定是系统管理员跟通知用户之间的会话
        message.setConversationId(WendaUtil.SYSTEM_USERID + "_" + model.getEntityOwnerId());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }//只关注like事件
}


















