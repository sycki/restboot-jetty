package com.sycki.restboot.controller;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloController extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] b = new byte[1024];
        int len = request.getInputStream().read(b);

        JSONObject r = new JSONObject();
        r.put("echo", new String(b, 0, len));
        r.put("length", len);
        response.getOutputStream().write(r.toJSONString().getBytes());
    }
}
