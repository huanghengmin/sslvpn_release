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
 * Time: 下午11:23
 * To change this template use File | Settings | File Templates.
 */
public class AdvancedAction extends ActionSupport {

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
    private static final Logger logger = Logger.getLogger(AdvancedAction.class);

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
        String client_to_client = request.getParameter("client_to_client");
        String allow_num_connecting = request.getParameter("allow_num_connecting");
        String refresh_connecting = request.getParameter("refresh_connecting");
        String refresh_num_connecting = request.getParameter("refresh_num_connecting");
        String support_lzo = request.getParameter("support_lzo");
        String traffic = request.getParameter("traffic");
        String allowed_ping = request.getParameter("allowed_ping");

        String dns_type = request.getParameter("dns_type");
        String default_domain_suffix = request.getParameter("default_domain_suffix");
        String  first=null;
        String second=null;

        if(dns_type.equals("2")){
            first = request.getParameter("first_dns");
            second = request.getParameter("second_dns");
        }
        server.setClient_dns_type(Integer.parseInt(dns_type));
        if(first!=null)
        server.setClient_first_dns(first);
        if(second!=null)
        server.setClient_second_dns(second);
        if(default_domain_suffix!=null&&default_domain_suffix.length()>0)
        server.setDefault_domain_suffix(default_domain_suffix);

        server.setClient_to_client(Integer.parseInt(client_to_client));

        if(null!=allow_num_connecting&&allow_num_connecting.equals("on"))
        server.setDuplicate_cn(1);
        else
        server.setDuplicate_cn(0);

        if(null!=support_lzo&&support_lzo.equals("on")){
            server.setComp_lzo(1);
        }else {
            server.setComp_lzo(0);
        }

        server.setTraffic_server(Integer.parseInt(traffic));
        server.setAllow_ping_server(Integer.parseInt(allowed_ping));
        server.setKeep_alive_interval(Integer.parseInt(refresh_connecting));
        server.setKeep_alive(Integer.parseInt(refresh_num_connecting));
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
            sb.append("client_to_client:'"+server.getClient_to_client()+"',");
            if(server.getDuplicate_cn()==1)
            sb.append("allow_num_connecting:'on',");
            else
            sb.append("allow_num_connecting:'off',");

            if(server.getComp_lzo()==1)
                sb.append("support_lzo:'on',");
            else
                sb.append("support_lzo:'off',");

            sb.append("dns_type:'"+server.getClient_dns_type()+"',");
            sb.append("default_domain_suffix:'"+server.getDefault_domain_suffix()+"',");
            sb.append("first:'"+server.getClient_first_dns()+"',");
            sb.append("second:'"+server.getClient_second_dns()+"',");

            sb.append("traffic:'"+server.getTraffic_server()+"',");
            sb.append("allowed_ping:'"+server.getAllow_ping_server()+"',");
            sb.append("refresh_num_connecting:'"+server.getKeep_alive()+"',");
            sb.append("refresh_connecting:'"+server.getKeep_alive_interval()+"'");
            sb.append("}");
        }
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }
}
