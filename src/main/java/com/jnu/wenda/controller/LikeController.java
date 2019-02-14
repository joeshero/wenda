package com.jnu.wenda.controller;

import com.jnu.wenda.model.EntityType;
import com.jnu.wenda.model.HostHolder;
import com.jnu.wenda.model.User;
import com.jnu.wenda.service.LikeService;
import com.jnu.wenda.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Joe
 */
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }
        User user = hostHolder.getUser();
        long likeCount = likeService.like(user.getId(), EntityType.ENTITY_QUESTION, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @PostMapping("/dislike")
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }
        User user = hostHolder.getUser();
        long likeCount = likeService.disLike(user.getId(), EntityType.ENTITY_QUESTION, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }




}
