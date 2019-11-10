package com.ljs.ssm_student.dao;

import com.ljs.ssm_student.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDAO {

    @Select("select id,username,password from user where username = #{username}")
    User findUserByUserName(String username);

    @Select("select max(id) from student")
    Integer getId();
    @Insert("insert into user (username,password) values (#{username},#{password})")
    int saveUser(User user);

    @Select("select id,username,password from user where username like #{username} limit\n" +
            "#{start} , #{size}")
    List<User> findList(Map<String, Object> queryConditions);

    @Select("select count(id) from user where username like #{username}")
    int getTotal(String username);

    @Update("update user set username=#{username},password = #{password} where id= #{id}")
    int editUser(User user);

    @Delete({"<script>" +
            "delete from user where id in (" +
            "<foreach item='id' collection='ids' separator=','>" +
            "#{id}",
            "</foreach>",
            ")",
            "</script>"})
    int deleteByIds(@Param("ids") Integer[] ids);
}
