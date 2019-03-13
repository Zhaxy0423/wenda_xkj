package com.xkj.wenda.service;

import com.xkj.wenda.dao.QuestionDao;
import com.xkj.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    @Autowired
    SensitiveService sensitiveService;

    public Question selectById(int id){
        return questionDao.selectById(id);
    }

    //提交问题之前需要添加敏感词过滤
    public int addQuestion(Question question){
        //过滤html语句
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDao.addQuestion(question)>0 ? question.getId() : 0;
    }

    public List<Question> getLatestQuestion(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return questionDao.updateCommentCount(id,count);
    }


}
