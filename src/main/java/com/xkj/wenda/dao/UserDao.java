package com.xkj.wenda.dao;

import com.xkj.wenda.model.User;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 数据库CRUD操作
 */
@Repository
//mybatis注解
@Mapper
public interface UserDao {
    /*
    方便修改替换
     */
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";//前后加空格，以免拼接时出错
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //增
    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    //根据用户id查询
    @Select({" select ",SELECT_FIELDS ," from ", TABLE_NAME," where id = #{id} "})
    User selectById(int id);

    //根据用户名查询
    @Select({" select ",SELECT_FIELDS ," from ", TABLE_NAME," where name = #{name} "})
    User selectByName(String name);

    //更新用户密码
    @Update({"update",TABLE_NAME,"set password = #{password} where id=#{id}"})
    void updatePassword(User user);

    //删除用户
    @Delete({"delete from ",TABLE_NAME,"where id=#{id}"})
    void deleteUser(int id);
}
