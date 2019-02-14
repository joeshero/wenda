package com.jnu.wenda.dao;

import com.jnu.wenda.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Joe
 */
@Mapper
@Repository
public interface CommentDao {

    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "user_id,content,created_date,entity_id,entity_type,status";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where entity_id=#{arg0} and entity_type=#{arg1} order by created_date desc"})
    List<Comment> getCommentByEntity(int entityId, int entityType);

    @Select({"select count(id) from",TABLE_NAME,"where entity_id=#{arg0} and entity_type=#{arg1}"})
    int getCommentCount(int entityId, int entityType);

    @Update({"update",TABLE_NAME,"comment set status=#{arg0} where id=#{arg1}"})
    int deleteComment(int id, int status);

}
