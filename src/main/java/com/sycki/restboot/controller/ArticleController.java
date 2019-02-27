package com.sycki.restboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.sycki.restboot.backend.MysqlBackend;
import com.sycki.restboot.pojo.Article;
import com.sycki.restboot.utils.Utils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sycki on 2017/9/9.
 */
public class ArticleController extends HttpServlet {
    Logger LOG = LogManager.getLogger();
    SqlSessionFactory sqlSessionFactory = Utils.getMysqlSession();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MysqlBackend mysqlBackend = session.getMapper(MysqlBackend.class);

            Article article = mysqlBackend.selectArticleByName(request.getParameter("title"));
            String r = JSONObject.toJSONString(article);
            response.getOutputStream().write(r.getBytes());
        }
    }

}
