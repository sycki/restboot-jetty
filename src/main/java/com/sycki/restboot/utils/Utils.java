package com.sycki.restboot.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sycki.restboot.backend.MysqlBackend;
import com.sycki.restboot.server.Config;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyVetoException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by sycki on 2017/8/27.
 */
public class Utils {

    public static String getClientIP(HttpServletRequest httpservletrequest) {
        if (httpservletrequest == null)
            return null;
        String s = httpservletrequest.getHeader("X-Forwarded-For");
        if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
            s = httpservletrequest.getHeader("Proxy-Client-IP");
        if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
            s = httpservletrequest.getHeader("WL-Proxy-Client-IP");
        if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
            s = httpservletrequest.getHeader("HTTP_CLIENT_IP");
        if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
            s = httpservletrequest.getHeader("HTTP_X_FORWARDED_FOR");
        if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
            s = httpservletrequest.getRemoteAddr();
        if ("127.0.0.1".equals(s) || "0:0:0:0:0:0:0:1".equals(s))
            try {
                s = InetAddress.getLocalHost().getHostAddress();
            }
            catch (UnknownHostException unknownhostexception) {
            }
        return s;
    }

    public static boolean check(String... values) {
        for (String value: values)
            if (value == null || "".equals(value))
                return false;

        return true;
    }

    public static SqlSessionFactory getMysqlSession() {
        try {
            Config conf = Config.getInstance();
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setJdbcUrl(conf.get("jdbc.url"));
            cpds.setUser(conf.get("jdbc.username"));
            cpds.setPassword(conf.get("jdbc.password"));
            cpds.setInitialPoolSize(conf.getInt("jdbc.minPoolSize"));
            cpds.setMaxPoolSize(conf.getInt("jdbc.maxPoolSize"));
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("mysql", transactionFactory, cpds);
            Configuration configuration = new Configuration(environment);
            configuration.addMapper(MysqlBackend.class);
            return new SqlSessionFactoryBuilder().build(configuration);
        } catch (PropertyVetoException e) {
            System.err.println("can not connect to mysql: " + e.getMessage());
            System.exit(5);
        }

        return null;
    }

}
