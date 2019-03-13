package com.xkj.wenda.controller;

import com.xkj.wenda.async.EventModel;
import com.xkj.wenda.async.EventProducer;
import com.xkj.wenda.async.EventType;
import com.xkj.wenda.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path ={ "/reglogin"},method = RequestMethod.GET)
    public String reg(Model model,
                      @RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }

    /**
     * 用户注册：POST提交表单数据到服务器
     * @param model
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(path ={ "/reg/"},method = RequestMethod.POST)
    public String reg(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                      @RequestParam(value = "next",required = false) String next,
                      HttpServletResponse response){
        try{
            Map<String,String> map = userService.register(username, password);
            /*
            包含ticket，则下发到客户端
             */
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie( cookie );
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+ next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch(Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }

    /**
     * 用户登录
     * @param model
     * @param username
     * @param password
     * @param rememberme
     * @param response
     * @return
     */
    @RequestMapping(path ={ "/login/"},method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value="rememberme") boolean rememberme,
                        HttpServletResponse response){
        /*如果登入该用户具有ticket，则生成cookie给浏览器，并跳回原网页。若没有，则转到登录界面。*/
        try{
            Map<String,String> map = userService.login(username, password);
            /*
            包含ticket则下发到客户端
             */
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);

                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("username",username).setExt("email","zhaxy0423@163.com"));
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+ next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

        }catch(Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path ={ "/logout"},method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return  "redirect:/";
    }
}
