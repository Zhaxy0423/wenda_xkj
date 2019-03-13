package com.xkj.wenda.service;

import com.xkj.wenda.dao.CommentDao;
import com.xkj.wenda.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    SensitiveService sensitiveService;

    //添加评论
    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        //添加成功则返回评论id
        return commentDao.addComment(comment)>0 ? comment.getId() : 0 ;
    }

    //根据实体选出评论
    public List<Comment> selectCommentByEntity(int entityId,int entityType){
        return commentDao.selectCommentByEntity(entityId, entityType);
    }

    //查询评论数目
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId, entityType);
    }

    //更新评论状态
    public boolean deleteComment(int commentId){
        return commentDao.updateStatus(1,commentId)>0;
    }

    public Comment getCommentById(int id) {
        return commentDao.getCommentById(id);
    }

    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }
}
