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
 * Date: 13-9-5
 * Time: 下午10:23
 * To change this template use File | Settings | File Templates.
 */
public class ServerSetAction extends ActionSupport {

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
    private static final Logger logger = Logger.getLogger(ServerSetAction.class);

    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String updateSetConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        Server server =  serverDao.findDefaultServer();
        String dynamic_ip = request.getParameter("dynamic_ip");
        String dynamic_ip_mask = request.getParameter("dynamic_ip_mask");
        String static_ip = request.getParameter("static_ip");
        String static_ip_mask = request.getParameter("static_ip_mask");
//        String group_default_net = request.getParameter("group_default_net");
//        String sub_net = request.getParameter("sub_net");

        String allow_all_access_sub_net = request.getParameter("allow_all_access_sub_net");
        if(allow_all_access_sub_net==null)
            allow_all_access_sub_net = "0";
        server.setServer(dynamic_ip+" "+dynamic_ip_mask);
        server.setDynamic_net(dynamic_ip+" "+dynamic_ip_mask);
        if(static_ip!=null&&static_ip_mask!=null)
        server.setStatic_net(static_ip+" "+static_ip_mask);
//        server.setGroup_default_net(group_default_net);
//        server.setPrivate_net(sub_net);
        server.setAllow_private_net(allow_all_access_sub_net);
        serverDao.update(server);
        Server default_server = serverDao.findDefaultServer();
        List<PrivateNet> nets = privateNetDao.findAll();
        List<Balancer> balancers = balancerDao.findAll();
        VPNConfigUtil.configServer(default_server, StringContext.server_config_file,nets,balancers);
        json = "{success:true}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findSetConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        Server server =  serverDao.findDefaultServer();
        StringBuilder sb= new StringBuilder("{'success':true,'totalCount':1,'root':[");
        if(server!=null){
            sb.append("{");
            String dynamic_net = server.getDynamic_net();
            if(dynamic_net!=null) {
            sb.append("dynamic_ip:'"+dynamic_net.substring(0,dynamic_net.indexOf(" "))+"',");
            sb.append("dynamic_ip_mask:'"+dynamic_net.substring(dynamic_net.indexOf(" ")+1,dynamic_net.length())+"',");
            }
            String static_net = server.getStatic_net();
            if(static_net!=null) {
                sb.append("static_ip:'"+static_net.substring(0,static_net.indexOf(" "))+"',");
                sb.append("static_ip_mask:'"+static_net.substring(static_net.indexOf(" ")+1,static_net.length())+"',");
            }
//            String group_default_net = server.getGroup_default_net();
//            String sub_net = server.getPrivate_net();
            String allow_all_access_sub_net = server.getAllow_private_net();
//            sb.append("group_default_net:'"+group_default_net.replaceAll("\\n", "\\\\n")+"',");
//            sb.append("sub_net:'"+sub_net.replaceAll("\\n", "\\\\n")+"',");
            sb.append("allow_all_access_sub_net:'"+allow_all_access_sub_net+"'");
            sb.append("}");
        }
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }
}
