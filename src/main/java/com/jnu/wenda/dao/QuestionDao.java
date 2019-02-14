package com.jnu.wenda.dao;

import com.jnu.wenda.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Joe
 */
@Mapper
@Repository
public interface QuestionDao {

    String TABLE_NAME = "question";
    String INSERT_FIELDS = "title,content,created_date,user_id,comment_count";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") " +
            "values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    Question selectById(int id);

    @Delete({"delete from", TABLE_NAME, "where id=#{id}"})
    void deleteById(int id);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where user_id=#{arg0} order by id desc limit #{arg1},#{arg2}"})
    List<Question> selectLatestQuestions(int userId,
                                         int offset,
                                         int limit);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"order by id desc limit #{arg0},#{arg1}"})
    public List<Question> selectAllQuestions(int offset, int limit);

    @Update({"update",TABLE_NAME,"set comment_count=#{arg1} where id=#{arg0}"})
    public void updateCommentCount(int questionId,int commentCount);

}
