package com.xkj.wenda.dao;

import com.xkj.wenda.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionDao {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id,comment_count ";//前后加空格，以免拼接时出错
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title}, #{content}, #{createdDate}, #{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME, " where id = #{id}"})
    Question selectById(int id);

    /**
     * QuestionDao.xml实现sql语句
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    @Update({"update ",TABLE_NAME, " set comment_count =#{commentCount } where id = #{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
