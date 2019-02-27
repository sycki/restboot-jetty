package com.sycki.restboot.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by sycki on 2017/8/25.
 * 全局配置
 */
public class Config {
    private Logger LOG = LogManager.getLogger();
    private String RESTBOOT_HOME = System.getenv("RESTBOOT_HOME");
    private String configfile = RESTBOOT_HOME + "/config/application.properties";
    private Properties pro = new Properties();
    private HashSet<String> options = new HashSet<String>();

    private static class ConfigInstance {
        private static Config instance = new Config();
    }

    public static Config getInstance() {
        return ConfigInstance.instance;
    }

    public Config addOption(String key){
        this.options.add(key);
        return this;
    }

    public Config load(String file) {
        InputStream in = getStream(file);
        if(in == null)
            return null;
        try {
            this.pro.load(in);
        } catch (IOException e) {
            LOG.error(String.format("Load config file %s failed: %s", file, e.getMessage()));
            return null;
        } finally {
        	try {
				in.close();
			} catch (IOException e) {
				LOG.error(String.format("Failed close InputStream: %s.", file), e);
			}
        }

        int errCount = 0;
        for(String opt: options) {
            if (pro.getProperty(opt) == null) {
                errCount++;
                LOG.error(String.format("Option [%s] is must specified!", opt));
            }
        }
        if(errCount > 0) {
            pro.clear();
            System.exit(1);
        }

        LOG.info(String.format("Loaded config file: [%s]", file));
        configfile = file;
        return this;
    }

    public Config load(){
        return load(configfile);
    }

    @SuppressWarnings("resource")
	private InputStream getStream(String file){

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        }catch(FileNotFoundException e){
            LOG.warn(String.format("Not found config file: %s, Try search classpath", file));
            inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(file);
            if(inputStream == null) {
                LOG.warn("Not found config file in classpath!");
                System.exit(1);
            }
        }
        return inputStream;
    }

    public String get(String key) {
        return pro.getProperty(key);
    }

    public int getInt(String key) {
        int v = -1;
        String o = this.pro.getProperty(key);
        try {
            v = Integer.valueOf(o);
        } catch (NumberFormatException e) {
            LOG.error(String.format("getInt(): Parse Integer [%s:%s], %s", key, o,e.getMessage()));
        }
        return v;
    }

    public long getLong(String key) {
        long v = -1L;
        String o = this.pro.getProperty(key);
        try {
            v = Long.valueOf(o);
        } catch (NumberFormatException e) {
            LOG.error(String.format("getLong(): Parse Integer [%s:%s], %s", key, o,e.getMessage()));
        }
        return v;
    }

}
