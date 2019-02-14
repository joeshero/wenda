package com.jnu.wenda.service;

import com.jnu.wenda.dao.QuestionDao;
import com.jnu.wenda.model.Question;
import com.jnu.wenda.model.UserAndQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe
 */
@Service
public class QuestionService {


    @Autowired
    protected SensitiveService sensitiveService;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserService userService;

    //增加问题
    public int addQuestion(Question question) {
        //过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        return questionDao.addQuestion(question);
    }

    //查询问题
    public Question selectById(int id) {
        Question question = questionDao.selectById(id);
        question.setTitle(HtmlUtils.htmlUnescape(question.getTitle()));
        question.setContent(HtmlUtils.htmlUnescape(question.getContent()));
        return question;
    }

    //查询所有
    public List<Question> selectAllQuestions(int offset, int limit) {
        List<Question> questions = questionDao.selectAllQuestions(offset,limit);
        htmlUnescape(questions);
        return questions;
    }

    //删除问题
    public void deleteById(int id) {
        questionDao.deleteById(id);
    }

    //范围查询 0为查询所有
    public List<Question> selectLatestQuestions(int userId, int offset, int limit) {
        if (userId == 0) {
            return selectAllQuestions(offset,limit);
        }

        List<Question> questions = questionDao.selectLatestQuestions(userId, offset, limit);
        htmlUnescape(questions);
        return questions;
    }


    //获取问题
    public List<UserAndQuestion> getQuestions(int userId, int offset, int limit) {
        List<Question> questions = selectLatestQuestions(userId, offset, limit);
        htmlUnescape(questions);
        List<UserAndQuestion> vos = new ArrayList<>();
        for (Question question : questions) {
            simpleContent(question);
            UserAndQuestion vo = new UserAndQuestion();
            vo.setQuestion(question);
            vo.setUser(userService.selectById(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    //省略问题内容
    public void simpleContent(Question question) {
        String content = question.getContent();
        if (content.length() >= 100) {
            question.setContent(content.substring(0,100)+"......");
        }
    }


    public void updateCommentCount(int questionId,int commentCount){
        questionDao.updateCommentCount(questionId,commentCount);
    }

    //去转义
    public void htmlUnescape(List<Question> questions) {
        for (Question q : questions) {
            q.setContent(HtmlUtils.htmlUnescape(q.getContent()));
            q.setTitle(HtmlUtils.htmlUnescape(q.getTitle()));
        }
    }



}
