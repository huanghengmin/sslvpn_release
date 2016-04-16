package com.hzih.sslvpn.web.action.sslvpn.server;

import com.hzih.sslvpn.dao.ServerDao;
import com.hzih.sslvpn.domain.Server;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.iptool.IPPoolUtil;
import com.hzih.sslvpn.web.action.ActionBase;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-6
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class ServerStatusAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(ServerStatusAction.class);

    private ServerDao serverDao;

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public LogService getLogService() {
        return logService;
    }

    public String findConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Server server = serverDao.findDefaultServer();
        StringBuilder sb = new StringBuilder("{'success':true,'totalCount':1,'root':[");
        if (server != null) {
            sb.append("{");
            sb.append("protocol:'" + server.getProtocol() + "',");
            sb.append("port:'" + server.getPort() + "',");
            if (null != server.getLocal() && server.getLocal().equals("all_interface"))
                sb.append("interface:'所有接口'");
            else
                sb.append("interface:'" + server.getLocal() + "'");
            sb.append("}");
        }
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String openServer() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);


        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn start");
            Thread.sleep(1000 * 2);

            proc.exec("service openvpn status");
            String msg_on = proc.getOutput();
            if (msg_on.contains("is running")) {
                msg = "1";
                String dys_net = null;
                Server server = serverDao.findDefaultServer();
                if (server != null) {
                    if (server.getDynamic_net() != null) {
                        String dynamic_net = server.getDynamic_net();
                        if (dynamic_net.contains(" ")) {
                            String[] dys = dynamic_net.split(" ");
                            if (dys.length == 2) {
                                int mask = IPPoolUtil.getNetMask(dys[1]);
                                dys_net = dys[0] + "/" + mask;
                            }
                        }
                    }
                    String command = StringContext.systemPath + "/script/firewall_start.sh " +
                            dys_net + " " +
                            server.getProtocol() + " " +
                            server.getPort();
                    new Proc().exec(command);
                }
            } else {
                msg = "0";
            }
        } catch (Exception e) {
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String closeServer() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn stop");
            Thread.sleep(1000 * 2);
            proc.exec("service openvpn status");
            String msg_on = proc.getOutput();
            if (msg_on.contains("is running")) {
                msg = "1";
                String command = StringContext.systemPath + "/script/firewall_stop.sh";
                new Proc().exec(command);
            } else {
                msg = "0";
            }
        } catch (Exception e) {
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String checkServerStatus() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn status");
            String msg_on = proc.getOutput();
            if (msg_on.contains("is running")) {
                msg = "1";
            } else {
                msg = "0";
            }
        } catch (Exception e) {
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String reloadServer() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn restart");
            Thread.sleep(1000 * 2);
            try {
                proc.exec("service openvpn status");
                String msg_on = proc.getOutput();
                if (msg_on.contains("is running")) {
                    msg = "1";
                    String dys_net = null;
                    Server server = serverDao.findDefaultServer();
                    if (server != null) {
                        if (server.getDynamic_net() != null) {
                            String dynamic_net = server.getDynamic_net();
                            if (dynamic_net.contains(" ")) {
                                String[] dys = dynamic_net.split(" ");
                                if (dys.length == 2) {
                                    int mask = IPPoolUtil.getNetMask(dys[1]);
                                    dys_net = dys[0] + "/" + mask;
                                }
                            }
                        }
                        String command = StringContext.systemPath + "/script/firewall_start.sh " +
                                dys_net + " " +
                                server.getProtocol() + " " +
                                server.getPort();
                        new Proc().exec(command);
                    }
                } else {
                    msg = "0";
                }
            } catch (Exception e) {
                msg = "0";
            }
        } catch (Exception e) {
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
