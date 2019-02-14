package com.jnu.wenda.controller;

import com.jnu.wenda.model.Comment;
import com.jnu.wenda.model.EntityType;
import com.jnu.wenda.model.HostHolder;
import com.jnu.wenda.service.CommentService;
import com.jnu.wenda.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by Joe
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private QuestionService questionService;

    @PostMapping("/addComment")
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                //如果登录
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                //未登录要先登录
                return "redirect:/reglogin";
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);
        } catch (Exception e) {
            logger.error("导入评论失败" + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }


}
