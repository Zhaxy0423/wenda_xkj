package com.xkj.wenda.async.handler;


import com.xkj.wenda.Utils.MailSender;
import com.xkj.wenda.async.EventHandler;
import com.xkj.wenda.async.EventModel;
import com.xkj.wenda.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        // 判断用户是否登录异常
        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录ip异常", "mails/login_exception.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}


















