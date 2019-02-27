package com.sycki.restboot.server;

import org.apache.logging.log4j.LogManager;
import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.Logger;

public class LogAdaptor extends AbstractLogger {
    String name;
    org.apache.logging.log4j.Logger log;

    public LogAdaptor() {
        this("default");
    }

    public LogAdaptor(String name) {
        this.name = name;
        log = LogManager.getLogger(name);
    }

    @Override
    protected Logger newLogger(String s) {
        return new LogAdaptor(s);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void warn(String s, Object... objects) {
        log.warn(s, objects);
    }

    @Override
    public void warn(Throwable throwable) {
        log.warn(throwable);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    @Override
    public void info(String s, Object... objects) {
        log.info(s, objects);
    }

    @Override
    public void info(Throwable throwable) {
        log.info(throwable);
    }

    @Override
    public void info(String s, Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void setDebugEnabled(boolean b) {

    }

    @Override
    public void debug(String s, Object... objects) {
        log.debug(s, objects);
    }

    @Override
    public void debug(Throwable throwable) {
        log.debug(throwable);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    public void ignore(Throwable throwable) {
        log.trace(throwable);
    }
}
