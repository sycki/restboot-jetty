package com.sycki.restboot.server;

import com.sycki.restboot.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

public class LogFilter implements Filter {
    Logger log = LogManager.getLogger();
    String logF = "%s %s from %s:%d - %d %sB in %dms";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("start request logger filter");
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long elapsedTime = System.currentTimeMillis() - startTime;

        String method = request.getMethod();
        String ip = Utils.getClientIP(request);
        int port = request.getRemotePort();
        int status = response.getStatus();
        String size = response.getHeader("Content-Length");
        if (size == null) {
            ServletOutputStream out = response.getOutputStream();
            Field len;
            try {
                len = out.getClass().getDeclaredField("_written");
                len.setAccessible(true);
                size = Long.toString(len.getLong(out));
                response.setHeader("Content-Length", size);
            } catch (NoSuchFieldException e) {
                size = "0";
                log.warn("get field [_written] " + e.getMessage());
            } catch (IllegalAccessException e) {
                size = "0";
                log.warn("get value [_written] " + e.getMessage());
            }
        }
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if(queryString != null)
            url += "?" + queryString;

        if (status >= 200 && status < 300) {
            log.info(String.format(logF, method, url, ip, port, status, size, elapsedTime));
        } else if (status >= 300 && status < 400) {
            log.warn(String.format(logF, method, url, ip, port, status, size, elapsedTime));
        } else if (status >= 400) {
            log.error(String.format(logF, method, url, ip, port, status, size, elapsedTime));
        }
    }

    @Override
    public void destroy() {
        log.info("stop response logger filter");
    }
}
