package com.ljs.ssm_student.dao;


import com.ljs.ssm_student.entity.Clazz;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClazzDAO {

    @Select("select id,gradeId,name,remark from clazz where name = #{name}")
    Clazz findClazzByClazzName(String name);

    @Select("select id,gradeId,name,remark from clazz")
    List<Clazz> findAll();

    @Insert("insert into clazz (gradeId,name,remark) values (#{gradeId},#{name},#{remark})")
    int saveClazz(Clazz clazz);

    @Select({
            "<script>" +
                    "select id,gradeId,name,remark from clazz where name like #{name}",
                     "<if test=' gradeId != null '>",
                        "and gradeId = #{gradeId}",
                    "</if>",
                    "limit #{start} , #{size}",
            "</script>"
    })
    List<Clazz> findList(Map<String, Object> queryConditions);

    @Select("select count(id) from clazz where name like #{name}")
    int getTotal(String name);

    @Update("update clazz set gradeId=#{gradeId},name=#{name},remark = #{remark} where id= #{id}")
    int editClazz(Clazz clazz);

    @Delete({"<script>" +
            "delete from clazz where id in (" +
            "<foreach item='id' collection='ids' separator=','>" +
            "#{id}",
            "</foreach>",
            ")",
            "</script>"})
    int deleteByIds(@Param("ids") Integer[] ids);

    @Select({
            "<script>" +
                    "select count(id) from student where clazzId in (",
            "<foreach item='id' collection='ids' separator=','>" +
                    "#{id}",
            "</foreach>",
            ")",
            "</script>"})
    int gradeIsAir(@Param("ids") Integer[] ids);

}
