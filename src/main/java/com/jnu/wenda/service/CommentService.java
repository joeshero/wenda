package com.jnu.wenda.service;

/**
 * Created by Joe
 */

import com.jnu.wenda.dao.CommentDao;
import com.jnu.wenda.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId, int entityType) {
        return commentDao.getCommentByEntity(entityId, entityType);
    }


    public int addComment(Comment comment) {
        //过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment) > 0 ? comment.getId() : 0;
    }


    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId) {
        return commentDao.deleteComment(commentId, 1) > 0;
    }


}
