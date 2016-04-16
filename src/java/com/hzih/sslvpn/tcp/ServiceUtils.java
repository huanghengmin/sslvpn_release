package com.hzih.sslvpn.tcp;

import com.hzih.sslvpn.utils.StringContext;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by Administrator on 15-6-17.
 */
public class ServiceUtils {
    private final static Logger logger = Logger.getLogger(ServiceUtils.class);
    //设备ID号
    public String deviceId;
    //时间服务器
    public String timeServer;
    //证书服务器配置端口
    public String caServer;
    //虚拟接入服务端口
    public String vpnConnect;
    //终端监控服务器地址
    public String terminalMonitorServer;
    //人像识别业务处理服务器地址
    public String centerServer;

    private ServiceUtils(){

    }

    public static ServiceUtils getService() {
        Properties pros = new Properties();
        try {
            FileInputStream ins = new FileInputStream(StringContext.config_properties);
            pros.load(ins);
            ServiceUtils service = new ServiceUtils();
            service.deviceId = pros.getProperty("deviceId");
            service.timeServer = pros.getProperty("timeServer");
            service.caServer = pros.getProperty("caServer");
            service.vpnConnect = pros.getProperty("vpnConnect");
            service.terminalMonitorServer = pros.getProperty("terminalMonitorServer");
            service.centerServer = pros.getProperty("centerServer");
            return service;
        } catch (IOException e) {
            logger.error("加载配置文件config.properties错误", e);
            return null;
        }
    }
}
