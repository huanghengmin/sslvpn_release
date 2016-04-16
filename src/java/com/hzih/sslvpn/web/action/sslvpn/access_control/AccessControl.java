package com.hzih.sslvpn.web.action.sslvpn.access_control;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.syslog.SysLogSend;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 下午7:56
 * To change this template use File | Settings | File Templates.
 */
public class AccessControl extends ActionSupport {

    private Logger logger = Logger.getLogger(AccessControl.class);

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String saveConfig() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String status = request.getParameter("status");
        String control_url = request.getParameter("control_url");
        String bs_proxy_port = request.getParameter("bs_proxy_port");
        String bs_proxy_ip = request.getParameter("bs_proxy_ip");
        String proxy_port = request.getParameter("proxy_port");
        boolean flag = AccessControlXML.saveConfig(status, control_url, proxy_port, bs_proxy_ip, bs_proxy_port);
        if (flag) {
            msg = "保存访问控制信息成功";
            json = "{success:true,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            SysLogSend.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "访问控制", msg);
        } else {
            msg = "保存访问控制信息失败";
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            SysLogSend.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "访问控制", msg);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String selectControl() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int totalCount = 0;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            getData(stringBuilder);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        totalCount = totalCount + 1;
        StringBuilder json = new StringBuilder("{totalCount:" + totalCount + ",root:[");
        json.append(stringBuilder);
        json.append("]}");
        try {
            actionBase.actionEnd(response, json.toString(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getData(StringBuilder stringBuilder) {
        stringBuilder.append("{");
        if (AccessControlXML.getAttribute(AccessControlXML.status) != null)
            stringBuilder.append("status:'" + AccessControlXML.getAttribute(AccessControlXML.status) + "',");
        else
            stringBuilder.append("status:'',");

        if (AccessControlXML.getAttribute(AccessControlXML.proxy_port) != null)
            stringBuilder.append("proxy_port:'" + AccessControlXML.getAttribute(AccessControlXML.proxy_port) + "',");
        else
            stringBuilder.append("proxy_port:'',");

        if (AccessControlXML.getAttribute(AccessControlXML.bs_proxy_ip) != null)
            stringBuilder.append("bs_proxy_ip:'" + AccessControlXML.getAttribute(AccessControlXML.bs_proxy_ip) + "',");
        else
            stringBuilder.append("bs_proxy_ip:'',");

        if (AccessControlXML.getAttribute(AccessControlXML.bs_proxy_port) != null)
            stringBuilder.append("bs_proxy_port:'" + AccessControlXML.getAttribute(AccessControlXML.bs_proxy_port) + "',");
        else
            stringBuilder.append("bs_proxy_port:'',");

        if (AccessControlXML.getAttribute(AccessControlXML.control_url) != null)
            stringBuilder.append("control_url:'" + AccessControlXML.getAttribute(AccessControlXML.control_url) + "'");
        else
            stringBuilder.append("control_url:''");
        stringBuilder.append("}");
    }
}
