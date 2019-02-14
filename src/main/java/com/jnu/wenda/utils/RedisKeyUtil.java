package com.jnu.wenda.utils;

/**
 * Created by Joe
 */
public class RedisKeyUtil {

    //分隔符
    private static String SPLIT = ":";
    //业务
    private static String BIZ_LIKE = "like";

    private static String BIZ_DISLIKE = "dislike";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
