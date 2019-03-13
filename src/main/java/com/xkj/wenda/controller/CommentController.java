package com.xkj.wenda.controller;

import com.xkj.wenda.Utils.WendaUtil;
import com.xkj.wenda.model.Comment;
import com.xkj.wenda.model.EntityType;
import com.xkj.wenda.model.HostHolder;
import com.xkj.wenda.service.CommentService;
import com.xkj.wenda.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/addComment"},method ={RequestMethod.POST} )
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            if(hostHolder.getUser()!=null){
                comment.setUserId(hostHolder.getUser().getId());
            }else {
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            comment.setStatus(0);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);
        }catch (Exception e) {
            logger.error("评论失败" + e.getMessage());
        }
        return "redirect:/question/" + String.valueOf(questionId);
    }
}
