package com.sycki.restboot.backend;

import com.sycki.restboot.pojo.Article;
import org.apache.ibatis.annotations.*;

/**
 * create database test;
 * create table test.article (title varchar(255), content text);
 * insert into test.article values ("restboot", "hello world!");
 */
@Mapper
public interface MysqlBackend {
    @Select("select * from article where title = #{title}")
    Article selectArticleByName(@Param("title") String title);
}
