package com.ljs.ssm_student.dao;

import com.ljs.ssm_student.entity.Grade;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GradeDAO {
    @Select("select id,name,remark from grade where name = #{name}")
    Grade findGradeByGradeName(String name);

    @Insert("insert into grade (name,remark) values (#{name},#{remark})")
    int saveGrade(Grade grade);

    @Select("select id,name,remark from grade where name like #{name} limit\n" +
            "#{start} , #{size}")
    List<Grade> findList(Map<String, Object> queryConditions);

    @Select("select id,name,remark from grade")
    List<Grade> findAll();

    @Select("select count(id) from grade where name like #{name}")
    int getTotal(String name);

    @Update("update grade set name=#{name},remark = #{remark} where id= #{id}")
    int editGrade(Grade grade);

    @Delete({"<script>" +
            "delete from grade where id in (" +
            "<foreach item='id' collection='ids' separator=','>" +
            "#{id}",
            "</foreach>",
            ")",
            "</script>"})
    int deleteByIds(@Param("ids") Integer[] ids);


    @Select({
            "<script>" +
            "select count(id) from clazz where gradeId in (",
            "<foreach item='id' collection='ids' separator=','>" +
                    "#{id}",
            "</foreach>",
            ")",
            "</script>"})
    int gradeIsAir(@Param("ids") Integer[] ids);
}
