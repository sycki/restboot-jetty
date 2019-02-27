package com.sycki.restboot.router;

import com.sycki.restboot.controller.ArticleController;
import com.sycki.restboot.controller.HelloController;
import com.sycki.restboot.server.LogFilter;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class RouterV1 {

    public ServletContextHandler build() {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/v1");

        // setup request logger
        handler.addFilter(LogFilter.class, "/*", null);

        // mapping controller and path
        handler.addServlet(ArticleController.class, "/article");
        handler.addServlet(HelloController.class, "/echo");
        return handler;
    }
}
