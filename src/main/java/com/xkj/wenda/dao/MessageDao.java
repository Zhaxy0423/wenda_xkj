package com.xkj.wenda.dao;

import com.xkj.wenda.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    String HAS_READER_TRUE = "1";

    //更新已读/未读状态
    @Update({"update ", TABLE_NAME, " set has_read=", HAS_READER_TRUE, " where conversation_id=#{conversationId}"})
    void updateMessagesReadStatus(String conversationId);

    //发送消息
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    //得到某个用户的所有消息并将消息进行分页
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    //查询未读消息数目
    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId} "})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);


    //消息列表获取
    //先按时间查找，再只显示每个对话的最新一条，按时间排序
    @Select({"select ", INSERT_FIELDS, " , count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt  GROUP BY conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
}
