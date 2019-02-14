package com.jnu.wenda.controller;

import com.jnu.wenda.model.*;
import com.jnu.wenda.service.CommentService;
import com.jnu.wenda.service.LikeService;
import com.jnu.wenda.service.QuestionService;
import com.jnu.wenda.service.UserService;
import com.jnu.wenda.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;



    @PostMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {

        String msg = "";
        //未登录，返回
        if (hostHolder.getUser() == null) {
            msg = WendaUtil.getJSONString(999);
            return msg;
        }
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            question.setUserId(hostHolder.getUser().getId());
            int tag = questionService.addQuestion(question);
            if (tag > 0) {
                msg = WendaUtil.getJSONString(tag);
            }else{
                msg = WendaUtil.getJSONString(1, "失败");
            }

        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }finally {
            return msg;
        }
    }

    @GetMapping("/question/{qid}")
    public String questionDetail(Model model,@PathVariable("qid") int qid) {
        //登录用户
        model.addAttribute("user", hostHolder.getUser());
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
       //获取相关问题所有评论
        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        //获取每条评论相关用户
        List<UserAndComment> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            UserAndComment vo = new UserAndComment();
            User user = userService.selectById(comment.getUserId());
            long likedCount = likeService.getLikeCount(EntityType.ENTITY_QUESTION, comment.getEntityId());
            int status = likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, comment.getEntityId());
            //评论
            vo.setComment(comment);
            //用户
            vo.setUser(user);
            //总赞同数
            vo.setLiked(likedCount);
            //用户赞状态
            vo.setStatus(status);
            vos.add(vo);
        }



        //问题发布者
        model.addAttribute("vos", vos);
        return "detail";
    }
}
