package com.hzih.sslvpn.utils;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-7
 * Time: 上午10:56
 * To change this template use File | Settings | File Templates.
 */
public class StringContext {
    public final static String systemPath = System.getProperty("vpn.home");
    public final static String INTERFACE = "/etc/network/interfaces";//linux下IP信息存储文件
    public final static String IFSTATE = "/etc/network/run/ifstate"; //linux下DNS信息

    public final static String localLogPath = systemPath + "/logs";   //日志文件目录
    public final static String webPath = systemPath + "/tomcat/webapps"; //war服务文件存储目录
    public final static String tempPath = systemPath + "/tomcat/temp"; //缓存目录

    public final static String hotConfig = systemPath + "/data/backup/hotconfig.xml";//双机热备备份文件

    public final static String serverLogPath = systemPath + "/server_logs";  //服务日志目录
    public final static String server_sslPath = StringContext.systemPath + "/ssl";
    public final static String user_manager_path = StringContext.systemPath + "/user_manager";
    public final static String static_key_path = StringContext.systemPath + "/static_key";
    public final static String pool_path = StringContext.systemPath + "/pool";
    public final static String pool_file = pool_path + "/ipp.txt";
    public final static String script_path = StringContext.systemPath + "/script";

    public final static String server_config_file = "/etc/openvpn/server.conf";
//    public final static String server_config_file = systemPath + "/server.conf";

    public final static String dh_file = server_sslPath + "/dh/dh1024.pem";
    public final static String ta_key_file = systemPath +"/static_key/ta.key";
    public final static String ca_file = server_sslPath + "/ca/ca.pem";
    public final static String server_file = server_sslPath + "/server/server.pem";
    public final static String key_file = server_sslPath + "/key/key.pem";

    public final static String android_config_file = systemPath + "/client_config/VPN_phone.ovpn";
    public final static String windows_config_file = systemPath + "/client_config/VPN_windows.ovpn";

    public static final String ldap_xml  =  StringContext.systemPath + "/config/ldap.xml";
    public static final String strategy_xml  =  StringContext.systemPath + "/config/strategy.xml";

    public static final String config_properties  =  StringContext.systemPath + "/config/config.properties";

    public static String crl_file = server_sslPath + "/crl/crl.pem";
    public static final String crl = StringContext.systemPath +"ssl/crl/crl.pem";
    public static final String crl_xml  =  StringContext.systemPath + "/config/crl.xml";
    /**
     * 日志服务器配置文件
     */
    public static final String syslog_xml =StringContext.systemPath + "/config/syslog.xml";

    public static final String SECURITY_CONFIG = StringContext.systemPath + "/tomcat/conf/server.xml";

}
