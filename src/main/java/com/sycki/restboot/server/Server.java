package com.sycki.restboot.server;

import com.sycki.restboot.router.RouterV1;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.log.Log;

public class Server {

    /**
     * 在IDE中运行需设置以下环境变量：
     * RESTBOOT_HOME=${projet_home}/src/main/resources
     * LOG4J_CONFIGURATION_FILE=${projet_home}/src/main/resources/config/log4j2.properties
     */
    public static void main(String[] args) throws Exception {
        // add check point for config file
        Config conf = Config.getInstance()
                .addOption("server.port")
                .load();

        // redirect log to log4j2
        Log.setLog(new LogAdaptor());

        // create server
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();

        // set up connectors for each port into server
        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(conf.getInt("server.port"));
        server.setConnectors(new Connector[] { httpConnector });

        // set up routers for the server
        server.setHandler(new RouterV1().build());

        // start service for all added port
        server.setStopAtShutdown(true);
        server.setStopTimeout(5000);
        server.start();
        server.join();
    }

}
