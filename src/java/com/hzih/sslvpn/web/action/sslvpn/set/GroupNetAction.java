package com.hzih.sslvpn.web.action.sslvpn.set;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupNetDao;
import com.hzih.sslvpn.domain.GroupNet;
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
 * Time: 下午1:35
 * To change this template use File | Settings | File Templates.
 */
public class GroupNetAction extends ActionSupport {
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

    private GroupNetDao groupNetDao;
    private GroupNet groupNet;

    public GroupNet getGroupNet() {
        return groupNet;
    }

    public void setGroupNet(GroupNet groupNet) {
        this.groupNet = groupNet;
    }

    public GroupNetDao getGroupNetDao() {
        return groupNetDao;
    }

    public void setGroupNetDao(GroupNetDao groupNetDao) {
        this.groupNetDao = groupNetDao;
    }

    public String add()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存失败'}";
        GroupNet old = groupNetDao.findByNet(groupNet.getNet());
        if(null!=old){
            json = "{success:false,msg:'网络已存在!'}";
        }else {
            groupNetDao.add(groupNet);
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
        GroupNet old = groupNetDao.findById(Integer.parseInt(id));
        if(null!=old){
            if(old.getNet().equals(groupNet.getNet())){
                old.setNet_mask(groupNet.getNet_mask());
                groupNetDao.modify(old);
                json = "{success:true,msg:'更新成功'}";
            }else {
               GroupNet net =  groupNetDao.findByNet(groupNet.getNet());
                if(null!=net){
                    json = "{success:false,msg:'网络已存在不允许更新到指定网络"+groupNet.getNet()+"'}";
                } else {
                    old.setNet(groupNet.getNet());
                    old.setNet_mask(groupNet.getNet_mask());
                    groupNetDao.modify(old);
                    json = "{success:true,msg:'更新成功'}";
                }
            }

        }else {
            if(old.getId()==Integer.parseInt(id)){
                old.setNet(groupNet.getNet());
                old.setNet_mask(groupNet.getNet_mask());
                groupNetDao.modify(old);
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
        groupNetDao.delete(new GroupNet(Integer.parseInt(id)));
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
        PageResult pageResult =  groupNetDao.listByPage(pageIndex,limit);
        if(pageResult!=null){
            List<GroupNet> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<GroupNet> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    GroupNet log = raUserIterator.next();
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
