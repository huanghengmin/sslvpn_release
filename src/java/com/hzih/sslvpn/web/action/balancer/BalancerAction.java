package com.hzih.sslvpn.web.action.balancer;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.BalancerDao;
import com.hzih.sslvpn.domain.Balancer;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
public class BalancerAction extends ActionSupport {
    private BalancerDao balancerDao;
    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public BalancerDao getBalancerDao() {
        return balancerDao;
    }

    public void setBalancerDao(BalancerDao balancerDao) {
        this.balancerDao = balancerDao;
    }

    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private Balancer balancer;

    public Balancer getBalancer() {
        return balancer;
    }

    public void setBalancer(Balancer balancer) {
        this.balancer = balancer;
    }

    public String add() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存失败'}";
        Balancer old = balancerDao.findByHostAndPort(balancer.getHost(), balancer.getPort());
        if (null != old) {
            json = "{success:false,msg:'负载配置已存在！'}";
        } else {
            balancerDao.add(balancer);
            json = "{success:true,msg:'添加成功！'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String modify() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新失败'}";
        String id = request.getParameter("id");
        Balancer old = balancerDao.findById(Integer.parseInt(id));
        if (null != old) {
                Balancer net = balancerDao.findByHostAndPort(balancer.getHost(), balancer.getPort());
                if (null != net) {
                    json = "{success:false,msg:'数据已存在，无法更新！" + balancer.getHost() + ","+balancer.getPort()+"'}";
                } else {
                    old.setHost(balancer.getHost());
                    old.setPort(balancer.getPort());
                    balancerDao.modify(old);
                    json = "{success:true,msg:'更新成功'}";
                }
        } else {
                json = "{success:true,msg:'更新数据未找到！'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        String id = request.getParameter("id");
        balancerDao.delete(new Balancer(Integer.parseInt(id)));
//        List<PrivateNet> nets = balancerDao.findAll();
//        Server server = serverDao.findDefaultServer();
//        VPNConfigUtil.configServer(server, StringContext.server_config_file,nets);
        json = "{success:true,msg:'删除成功'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int pageIndex = start / limit + 1;
        PageResult pageResult = balancerDao.listByPage(pageIndex, limit);
        if (pageResult != null) {
            List<Balancer> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<Balancer> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    Balancer log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',host:'" + log.getHost() +
                                "',port:'" + log.getPort() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',host:'" + log.getHost() +
                                "',port:'" + log.getPort() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }
}
