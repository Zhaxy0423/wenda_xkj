package com.xkj.wenda.controller;

import com.xkj.wenda.Utils.WendaUtil;
import com.xkj.wenda.model.*;
import com.xkj.wenda.service.CommentService;
import com.xkj.wenda.service.LikeService;
import com.xkj.wenda.service.QuestionService;
import com.xkj.wenda.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;


    @RequestMapping(value = "/question/add",method ={RequestMethod.POST} )
    @ResponseBody
    public String addQuestion(@Param("title") String title,
                              @Param("content") String content){
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            /*匿名用户id设置*/
            if(hostHolder.getUser()==null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question)>0){
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("添加问题失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value = "/question/{qid}",method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid){
        Question question = questionService.selectById(qid);
        model.addAttribute("question",question);
        //评论单元需要展示的内容
        List<Comment> commentList = commentService.selectCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for(Comment comment : commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }
}
