package com.jnu.wenda.dao;

import com.jnu.wenda.model.Message;
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
public interface MessageDao {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id,to_id,content,has_read,conversation_id,create_date";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createDate})"})
    int addMessage(Message message);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where conversation_id=#{arg0} order by create_date desc limit #{arg1},#{arg2}"})
    List<Message> getConversationDetail(String conversationId, int offset, int limit);


    @Select({"select", INSERT_FIELDS, "from", TABLE_NAME, "where create_date in (SELECT max(create_date) FROM wenda.message group by conversation_id having from_id=#{arg0} or to_id=#{arg0}) order by create_date desc limit #{arg1},#{arg2}"})
    List<Message> getMessage(int userId,
                       int offset,
                       int limit);

    @Select({"select count(conversation_id) from", TABLE_NAME, "where conversation_id=#{conversationId}"})
    int getMessageCount(String conversationId);

    @Select({"select count(has_read) from", TABLE_NAME, "where conversation_id=#{conversationId} and has_read=0"})
    int getUnreadCount(String conversationId);

    @Update({"update",TABLE_NAME,"set has_read=1 where conversation_id=#{conversationId}"})
    void updateHasRead(String conversationId);

}
