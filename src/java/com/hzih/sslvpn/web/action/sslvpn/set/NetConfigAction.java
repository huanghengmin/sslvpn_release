package com.hzih.sslvpn.web.action.sslvpn.set;

import com.hzih.sslvpn.dao.BalancerDao;
import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.dao.ServerDao;
import com.hzih.sslvpn.domain.Balancer;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.Server;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-4
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class NetConfigAction extends ActionSupport {

    private ServerDao serverDao;
    private PrivateNetDao privateNetDao;
    private BalancerDao balancerDao;

    public BalancerDao getBalancerDao() {
        return balancerDao;
    }

    public void setBalancerDao(BalancerDao balancerDao) {
        this.balancerDao = balancerDao;
    }

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(NetConfigAction.class);

    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String updateConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        Server server =  serverDao.findDefaultServer();
        String interface_box = request.getParameter("interface_box");
        String protocol = request.getParameter("protocol");
        String port = request.getParameter("port");
        server.setLocal(interface_box);
        server.setProtocol(protocol);
        server.setPort(Integer.parseInt(port));
        serverDao.update(server);
        Server default_server = serverDao.findDefaultServer();
        List<PrivateNet> nets = privateNetDao.findAll();
        List<Balancer> balancers = balancerDao.findAll();

        VPNConfigUtil.configServer(default_server, StringContext.server_config_file,nets,balancers);
        json = "{success:true}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        Server server =  serverDao.findDefaultServer();
        StringBuilder sb= new StringBuilder("{'success':true,'totalCount':1,'root':[");
        if(server!=null){
            sb.append("{");
            sb.append("interface:'"+server.getLocal()+"',");
            sb.append("protocol:'"+server.getProtocol()+"',");
            sb.append("port:'"+server.getPort()+"'");
            sb.append("}");
        }
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }
}
