package com.xkj.wenda.dao;

import com.xkj.wenda.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";//前后加空格，以免拼接时出错
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId}, #{content}, #{createdDate}, #{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    //根据实体选出评论
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

    //评论记录数
    @Select({"select count(*) from ",TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);

    //删除评论时更新状态
    @Update({"update ",TABLE_NAME, " set status=#{status} where id = #{id}"})
    int updateStatus(@Param("status") int status,@Param("id") int id);

    //根据id查询评论
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Comment getCommentById(int id);

    @Select({"select count(*) from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);
}
