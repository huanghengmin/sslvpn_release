package com.hzih.sslvpn.web.action.sslvpn.set;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.BalancerDao;
import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.dao.ServerDao;
import com.hzih.sslvpn.domain.Balancer;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.Server;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
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
public class PrivateNetAction extends ActionSupport {
    private ServerDao serverDao;
    private BalancerDao balancerDao;

    public BalancerDao getBalancerDao() {
        return balancerDao;
    }

    public void setBalancerDao(BalancerDao balancerDao) {
        this.balancerDao = balancerDao;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
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

    private PrivateNet privateNet;

    public PrivateNet getPrivateNet() {
        return privateNet;
    }

    public void setPrivateNet(PrivateNet privateNet) {
        this.privateNet = privateNet;
    }

    private PrivateNetDao privateNetDao;

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    public String add()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存失败'}";
        PrivateNet old = privateNetDao.findByNet(privateNet.getNet());
        if(null!=old){
            json = "{success:false,msg:'网络已存在!'}";
        }else {
            privateNetDao.add(privateNet);
            List<PrivateNet> nets = privateNetDao.findAll();
            List<Balancer> balancers = balancerDao.findAll();
            Server server = serverDao.findDefaultServer();
            VPNConfigUtil.configServer(server, StringContext.server_config_file,nets,balancers);
            json = "{success:true,msg:'添加成功!'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String modify()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新失败'}";
        String id = request.getParameter("id");
        PrivateNet old = privateNetDao.findById(Integer.parseInt(id));
        if(null!=old){
            if(old.getNet().equals(privateNet.getNet())){
                old.setNet_mask(privateNet.getNet_mask());
                privateNetDao.modify(old);
                List<PrivateNet> nets = privateNetDao.findAll();
                Server server = serverDao.findDefaultServer();
                List<Balancer> balancers = balancerDao.findAll();
                VPNConfigUtil.configServer(server, StringContext.server_config_file,nets,balancers);
                json = "{success:true,msg:'更新成功'}";
            }else {
                PrivateNet net =  privateNetDao.findByNet(privateNet.getNet());
                if(null!=net){
                    json = "{success:false,msg:'网络已存在不允许更新到指定网络"+privateNet.getNet()+"'}";
                } else {
                    old.setNet(privateNet.getNet());
                    old.setNet_mask(privateNet.getNet_mask());
                    privateNetDao.modify(old);
                    List<PrivateNet> nets = privateNetDao.findAll();
                    Server server = serverDao.findDefaultServer();
                    List<Balancer> balancers = balancerDao.findAll();
                    VPNConfigUtil.configServer(server, StringContext.server_config_file,nets,balancers);
                    json = "{success:true,msg:'更新成功'}";
                }
            }

        }else {
            if(old.getId()==Integer.parseInt(id)){
                old.setNet(privateNet.getNet());
                old.setNet_mask(privateNet.getNet_mask());
                privateNetDao.modify(old);
                List<PrivateNet> nets = privateNetDao.findAll();
                Server server = serverDao.findDefaultServer();
                List<Balancer> balancers = balancerDao.findAll();
                VPNConfigUtil.configServer(server, StringContext.server_config_file,nets,balancers);
                json = "{success:true,msg:'更新成功'}";
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        String id = request.getParameter("id");
        privateNetDao.delete(new PrivateNet(Integer.parseInt(id)));
        List<PrivateNet> nets = privateNetDao.findAll();
        Server server = serverDao.findDefaultServer();
        List<Balancer> balancers = balancerDao.findAll();
        VPNConfigUtil.configServer(server, StringContext.server_config_file,nets,balancers);
        json = "{success:true,msg:'删除成功'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }
    
    public String find()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int pageIndex = start/limit+1;
        PageResult pageResult =  privateNetDao.listByPage(pageIndex,limit);
        if(pageResult!=null){
            List<PrivateNet> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<PrivateNet> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    PrivateNet log = raUserIterator.next();
                    if(raUserIterator.hasNext()){
                        json += "{" +
                                "id:'"+log.getId()+
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() +"'" +
                                "},";
                    }else {
                        json += "{" +
                                "id:'"+log.getId()+
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() +"'" +
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
