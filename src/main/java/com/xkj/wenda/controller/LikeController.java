package com.xkj.wenda.controller;


import com.xkj.wenda.Utils.WendaUtil;
import com.xkj.wenda.async.EventModel;
import com.xkj.wenda.async.EventProducer;
import com.xkj.wenda.async.EventType;
import com.xkj.wenda.model.Comment;
import com.xkj.wenda.model.EntityType;
import com.xkj.wenda.model.HostHolder;
import com.xkj.wenda.service.CommentService;
import com.xkj.wenda.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 点击赞按钮后触发
     * @param commentId
     * @return
     */
    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);//999表示没登录
        }

        Comment comment = commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setEntityOwnerId(comment.getUserId()).setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }


}















