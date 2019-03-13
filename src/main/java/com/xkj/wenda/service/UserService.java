package com.xkj.wenda.service;

import com.xkj.wenda.Utils.WendaUtil;
import com.xkj.wenda.dao.LoginTicketDao;
import com.xkj.wenda.dao.UserDao;
import com.xkj.wenda.model.LoginTicket;
import com.xkj.wenda.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    LoginTicketDao loginTicketDao;

    public User getUser(int id) {
        return userDao.selectById(id);
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.length(username)<3 || StringUtils.length(username)>16){
            map.put("msg","用户名长度应在3-16之间");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        if(StringUtils.length(password)<3 || StringUtils.length(password)>16){
            map.put("msg","密码长度应在3-16之间");
            return map;
        }
        User user = userDao.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }
        user = new User();
        user.setName(username);
        //salt加密
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDao.addUser(user);
        return map;
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", " 用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }
        /*
        下发ticket表示用户登陆成功，记录用户与ticket关联
         */
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    public String addLoginTicket(int userId){
        LoginTicket loginTicket= new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600*24*100+now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    /**
     * 退出登录
     * @param ticket
     */
    public void logout(String ticket){

        loginTicketDao.updateStatus(ticket,1);
    }

    public User selectByName(String name) {

        return userDao.selectByName(name);
    }
}
