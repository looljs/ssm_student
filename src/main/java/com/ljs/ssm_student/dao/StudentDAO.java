package com.ljs.ssm_student.dao;

import com.ljs.ssm_student.entity.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudentDAO {

    @Select("select id,clazzId,sn,username,password,sex,photo,remark from student where username = #{username}")
    Student findStudentByStudentName(String username);

    @Insert("insert into student (clazzId,username,password,sex,photo,remark,sn) values (#{clazzId},#{username},#{password},#{sex},#{photo},#{remark},#{sn})")
    int saveStudent(Student Student);

    @Select({
            "<script>" +
                    "select id,clazzId,sn,username,password,sex,photo,remark from student where username like #{username}",
            "<if test=' clazzId != null '>",
            "and clazzId = #{clazzId}",
            "</if>",
            "limit #{start} , #{size}",
            "</script>"
    })
    List<Student> findList(Map<String, Object> queryConditions);

    @Select("select count(id) from student where username like #{username}")
    int getTotal(String username);

    @Update("update student set clazzId=#{clazzId},username=#{username},password=#{password},sex=#{sex},photo=#{photo},remark = #{remark} where id= #{id}")
    int editStudent(Student Student);

    @Delete({"<script>" +
            "delete from student where id in (" +
            "<foreach item='id' collection='ids' separator=','>" +
            "#{id}",
            "</foreach>",
            ")",
            "</script>"})
    int deleteByIds(@Param("ids") Integer[] ids);

}
