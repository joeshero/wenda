package com.jnu.wenda.dao;

import com.jnu.wenda.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by Joe
 */
@Mapper
@Repository
public interface UserDao {

    String TABLE_NAME = "user";

    @Insert({"insert into",TABLE_NAME,"(name,password,salt,head_url) " +
            "values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select * from", TABLE_NAME, "where id=#{id}"})
    User selectById(int id);

    @Update({"update",TABLE_NAME,"set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from", TABLE_NAME, "where id=#{id}"})
    void deleteUser(int id);

    @Select({"select * from", TABLE_NAME, "where name=#{name}"})
    User selectByName(String name);


}
