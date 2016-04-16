package com.hzih.sslvpn.web.action.config;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 12-11-8
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public class ServicesAction extends ActionSupport {
    private final static String deviceId = "VPN111234";

    private Logger logger = Logger.getLogger(ServicesAction.class);

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    /**
     * 保存配置
     *
     * @return
     * @throws IOException
     */
    public String save() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "服务器信息保存失败";
        String json = "{success:false,msg:'" + msg + "'}";
        String timeServerIp = request.getParameter("timeServerIp");
        String timeServerPort = request.getParameter("timeServerPort");

        String caServerIp = request.getParameter("caServerIp");
        String caServerPort = request.getParameter("caServerPort");

        String vpnConnectIp = request.getParameter("vpnConnectIp");
        String vpnConnectPort = request.getParameter("vpnConnectPort");

        String terminalMonitorServerIp = request.getParameter("terminalMonitorServerIp");
        String terminalMonitorServerPort = request.getParameter("terminalMonitorServerPort");

        String centerServerIp = request.getParameter("centerServerIp");
        String centerServerPort = request.getParameter("centerServerPort");

        String timeServer = null;
        if (timeServerIp != null && timeServerPort != null) {
            timeServer = timeServerIp + ":" + timeServerPort;
        }

        String caServer = null;
        if (caServerIp != null && caServerPort != null) {
            caServer = caServerIp + ":" + caServerPort;
        }

        String vpnConnect = null;
        if (vpnConnectIp != null && vpnConnectPort != null) {
            vpnConnect = vpnConnectIp + ":" + vpnConnectPort;
        }

        String terminalMonitorServer = null;
        if (terminalMonitorServerIp != null && terminalMonitorServerPort != null) {
            terminalMonitorServer = terminalMonitorServerIp + ":" + terminalMonitorServerPort;
        }

        String centerServer = null;
        if (centerServerIp != null && centerServerPort != null) {
            centerServer = centerServerIp + ":" + centerServerPort;
        }

        Properties pros = new Properties();
        try {
            FileInputStream ins = new FileInputStream(StringContext.config_properties);
            pros.load(ins);
            ins.close();
            OutputStream fos = new FileOutputStream(StringContext.config_properties);
            pros.setProperty("deviceId", deviceId);
            pros.setProperty("timeServer", timeServer);
            pros.setProperty("caServer", caServer);
            pros.setProperty("vpnConnect", vpnConnect);
            pros.setProperty("terminalMonitorServer", terminalMonitorServer);
            pros.setProperty("centerServer", centerServer);

            pros.store(fos, "Update values:" + timeServer +
                            "," + caServer +
                            "," + vpnConnect +
                            "," + terminalMonitorServer +
                            "," + centerServer
            );
            fos.close();
            msg = "服务器信息保存成功";
            json = "{success:true,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);

        } catch (IOException e) {
            logger.error("加载配置文件config.properties错误", e);
            OutputStream fos = new FileOutputStream(StringContext.config_properties);
            pros.setProperty("deviceId", deviceId);
            pros.setProperty("timeServer", timeServer);
            pros.setProperty("caServer", caServer);
            pros.setProperty("vpnConnect", vpnConnect);
            pros.setProperty("terminalMonitorServer", terminalMonitorServer);
            pros.setProperty("centerServer", centerServer);
            pros.store(fos, "Update values:" + timeServer +
                            "," + caServer +
                            "," + vpnConnect +
                            "," + terminalMonitorServer +
                            "," + centerServer);
            fos.close();
            msg = "服务器信息保存成功";
            json = "{success:true,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    /**
     * 查找
     *
     * @return
     */
    public String find() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int totalCount = 0;
        StringBuilder sb = new StringBuilder();
        jsonResult(sb);
        totalCount = totalCount + 1;
        StringBuilder json = new StringBuilder("{totalCount:" + totalCount + ",root:[");
        json.append(sb.toString());
        json.append("]}");
        try {
            actionBase.actionEnd(response, json.toString(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回JSON数据格式
     *
     * @param sb
     */
    private void jsonResult(StringBuilder sb) {
        sb.append("{");

        Properties pros = new Properties();
        try {
            FileInputStream ins = new FileInputStream(StringContext.config_properties);
            pros.load(ins);

            String timeServer = pros.getProperty("timeServer");
            String caServer = pros.getProperty("caServer");
            String vpnConnect = pros.getProperty("vpnConnect");
            String terminalMonitorServer = pros.getProperty("terminalMonitorServer");
            String centerServer = pros.getProperty("centerServer");

            sb.append("timeServerIp:'" + timeServer.split(":")[0] + "',");
            sb.append("timeServerPort:'" + timeServer.split(":")[1] + "',");

            sb.append("caServerIp:'" + caServer.split(":")[0] + "',");
            sb.append("caServerPort:'" + caServer.split(":")[1] + "',");

            sb.append("vpnConnectIp:'" + vpnConnect.split(":")[0] + "',");
            sb.append("vpnConnectPort:'" + vpnConnect.split(":")[1] + "',");

            sb.append("terminalMonitorServerIp:'" + terminalMonitorServer.split(":")[0] + "',");
            sb.append("terminalMonitorServerPort:'" + terminalMonitorServer.split(":")[1] + "',");

            sb.append("centerServerIp:'" + centerServer.split(":")[0] + "',");
            sb.append("centerServerPort:'" + centerServer.split(":")[1] + "'");

            ins.close();

        } catch (Exception e) {
            logger.error("加载配置文件config.properties错误", e);
            e.printStackTrace();
        }


        sb.append("}");
    }
}
