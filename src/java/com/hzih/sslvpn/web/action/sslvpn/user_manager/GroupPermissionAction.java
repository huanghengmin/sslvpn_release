package com.hzih.sslvpn.web.action.sslvpn.user_manager;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupDao;
import com.hzih.sslvpn.domain.Groups;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.utils.JsonUtil;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

public class GroupPermissionAction extends ActionSupport {

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

    private GroupDao groupDao;

    private Groups group;

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }

    public String add()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存失败'}";
        Groups old = groupDao.findByName(group.getGroup_name());
        if(null!=old){
            json = "{success:false,msg:'用户组已存在!'}";
        }else {
            groupDao.add(group);
            json = "{success:true,msg:'添加用户组成功!'}";
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
        Groups old = groupDao.findById(Integer.parseInt(id));
        if(null!=old){
             old.setGroup_name(group.getGroup_name());
             groupDao.modify(old);
             json = "{success:true,msg:'更新成功'}";
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
        Groups old = groupDao.findById(Integer.parseInt(id));
        groupDao.delete(old);
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
        PageResult pageResult =  groupDao.listByPage(null,pageIndex,limit);
        if(pageResult!=null){
            List<Groups> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<Groups> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    Groups log = raUserIterator.next();
                    if(raUserIterator.hasNext()){
                        json += "{" +
                                "id:'"+log.getId()+
                                "',deny_access:'" + log.getDeny_access() +
                                "',group_name:'" + log.getGroup_name() +
                                "',assign_nets:'" + JsonUtil.checkNull(log.getAssign_nets()).toString() +
                                "',dynamic_ip_range:'" + JsonUtil.checkNull(log.getDynamic_ip_range()).toString() +
                                "',allow_group_ids:'" + JsonUtil.checkNull(log.getAllow_group_ids()).toString() +"'" +
                                "},";
                    }else {
                        json += "{" +
                                "id:'"+log.getId()+
                                "',deny_access:'" + log.getDeny_access() +
                                "',group_name:'" + log.getGroup_name() +
                                "',assign_nets:'" + JsonUtil.checkNull(log.getAssign_nets()).toString() +
                                "',dynamic_ip_range:'" + JsonUtil.checkNull(log.getDynamic_ip_range()).toString() +
                                "',allow_group_ids:'" + JsonUtil.checkNull(log.getAllow_group_ids()).toString() +"'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }
    
    
    public String findUserByGroup()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String id = request.getParameter("id");
        Groups old = groupDao.findById(Integer.parseInt(id));
        List<User> users = old.getUsers();
        if(users!=null){
            String  json= "{success:true,total:" + users.size() + ",rows:[";
            Iterator<User> userIterator = users.iterator();
            while (userIterator.hasNext()){
                User log = userIterator.next();
                if(userIterator.hasNext()){
                    json += "{" +
                            "id:'"+log.getId()+
                            "',username:'" + log.getCn() +
                            "',id_card:'" + JsonUtil.checkNull(log.getId_card()).toString() +
//                            "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()).toString() +
                            "',deny_access:'" + log.getDeny_access() +
                            "',dynamic_ip:'" + log.getDynamic_ip() +
                            "',static_ip:'" + JsonUtil.checkNull(log.getStatic_ip()).toString() +
                            "',allow_all_subnet:'" + JsonUtil.checkNull(log.getAllow_all_subnet()).toString() +
                            "',allow_all_client:'" + JsonUtil.checkNull(log.getAllow_all_client()).toString() +
                            "',create_time:'" + log.getCreate_time() +
                            "',count_bytes_cycle:'" + log.getQuota_cycle() +
                            "',max_bytes:'" + log.getQuota_bytes() +
                            "',active:'" + log.getActive() +
                            "',email:'" + JsonUtil.checkNull(log.getEmail()).toString() +
                            "',phone:'" + JsonUtil.checkNull(log.getPhone()).toString() +
                            "',address:'" + JsonUtil.checkNull(log.getAddress()).toString() +
                            "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()).toString() +
                            "',type:'" + JsonUtil.checkNull(log.getType()).toString() +
//                            "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
                            "',revoked:'" + log.getRevoked() +
                            "',enabled:'" + log.getEnabled() +
                            "',real_address:'" + JsonUtil.checkNull(log.getReal_address()).toString() +
                            "',byte_received:'" + log.getByte_received() +
                            "',byte_send:'" + log.getByte_send() +
                            "',connected_since:'" + log.getConnected_since() +
                            "',virtual_address:'" + JsonUtil.checkNull(log.getVirtual_address()).toString() +
                            "',last_ref:'" + log.getLast_ref() +"'" +
                            "},";
                }else {
                    json += "{" +
                            "id:'"+log.getId()+
                            "',username:'" + log.getCn() +
                            "',id_card:'" + JsonUtil.checkNull(log.getId_card()).toString() +
//                            "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()).toString() +
                            "',deny_access:'" + log.getDeny_access() +
                            "',dynamic_ip:'" + log.getDynamic_ip() +
                            "',static_ip:'" + JsonUtil.checkNull(log.getStatic_ip()).toString() +
                            "',allow_all_subnet:'" + JsonUtil.checkNull(log.getAllow_all_subnet()).toString() +
                            "',allow_all_client:'" + JsonUtil.checkNull(log.getAllow_all_client()).toString() +
                            "',create_time:'" + log.getCreate_time() +
                            "',count_bytes_cycle:'" + log.getQuota_cycle() +
                            "',max_bytes:'" + log.getQuota_bytes() +
                            "',active:'" + log.getActive() +
                            "',email:'" + JsonUtil.checkNull(log.getEmail()).toString() +
                            "',phone:'" + JsonUtil.checkNull(log.getPhone()).toString() +
                            "',address:'" + JsonUtil.checkNull(log.getAddress()).toString() +
                            "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()).toString() +
                            "',type:'" + JsonUtil.checkNull(log.getType()).toString() +
//                            "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
                            "',revoked:'" + log.getRevoked() +
                            "',enabled:'" + log.getEnabled() +
                            "',real_address:'" + JsonUtil.checkNull(log.getReal_address()).toString() +
                            "',byte_received:'" + log.getByte_received() +
                            "',byte_send:'" + log.getByte_send() +
                            "',connected_since:'" + log.getConnected_since() +
                            "',virtual_address:'" + JsonUtil.checkNull(log.getVirtual_address()).toString() +
                            "',last_ref:'" + log.getLast_ref() +"'" +
                            "}";
                }
            }
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }
    
    
    public String addUserToGroup()throws Exception{
          return null;
    }
    
    public String removeUserByGroup()throws Exception{
         return null;
    }

}
