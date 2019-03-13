package com.xkj.wenda.interceptor;

import com.xkj.wenda.dao.LoginTicketDao;
import com.xkj.wenda.dao.UserDao;
import com.xkj.wenda.model.HostHolder;
import com.xkj.wenda.model.LoginTicket;
import com.xkj.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketDao loginTicketDao;
    @Autowired
    UserDao userDao;
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if(httpServletRequest.getCookies()!=null){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        /*
        如果ticket真实有效，把其关联的用户信息取出来
         */
        if(ticket!=null){
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if(loginTicket==null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
                return true;
            }
            User user = userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        /*
        在模板渲染之前将user加进去，放在渲染上下文
         */
        if(modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
