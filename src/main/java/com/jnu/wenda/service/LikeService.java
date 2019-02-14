package com.jnu.wenda.service;

import com.jnu.wenda.utils.RedisKeyUtil;
import com.jnu.wenda.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Joe
 */
@Service
public class LikeService {

    @Autowired
    private RedisUtil redisUtil;


    //获取赞同数
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return redisUtil.sGetSetSize(likeKey);
    }

    //1:喜欢 -1：不喜欢 0：无动于衷
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (redisUtil.sHasKey(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        return redisUtil.sHasKey(disLikeKey, userId) ? -1 : 0;
    }

    public long like(int userId, int entityType, int entityId) {
        //获取专属key
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);

        redisUtil.sSet(likeKey, String.valueOf(userId));
        //将踩删掉
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        redisUtil.setRemove(disLikeKey, String.valueOf(userId));

        return redisUtil.sGetSetSize(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId) {
        //获取专属key
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);

        redisUtil.sSet(dislikeKey, String.valueOf(userId));
        //将赞删掉
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        redisUtil.setRemove(likeKey, String.valueOf(userId));

        return redisUtil.sGetSetSize(likeKey);
    }
}
