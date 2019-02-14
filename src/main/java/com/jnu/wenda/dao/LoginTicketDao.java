package com.jnu.wenda.dao;

import com.jnu.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by Joe
 */
@Mapper
@Repository
public interface LoginTicketDao {

    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = "user_id,ticket,expired,status";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") " +
            "values(#{userId},#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket ticket);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update", TABLE_NAME, "set status=#{arg0} where ticket=#{arg1}"})
    void updateStatus(int status, String ticket);


}
