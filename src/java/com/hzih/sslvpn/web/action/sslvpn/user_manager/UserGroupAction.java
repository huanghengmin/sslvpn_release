package com.hzih.sslvpn.web.action.sslvpn.user_manager;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.Groups;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.service.UserGroupService;
import com.hzih.sslvpn.utils.JsonUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 14-4-16
 * Time: 上午9:13
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupAction extends ActionSupport {

    private Logger logger = Logger.getLogger(UserGroupAction.class);
    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private UserGroupService userGroupService;
    private UserDao userDao;
    private PrivateNetDao privateNetDao;

    public UserGroupService getUserGroupService() {
        return userGroupService;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 查找
     * @return
     * @throws Exception
     */
    public String findByPages() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String roleId = request.getParameter("roleId");
        String json = userGroupService.getUsersByRoleId(Integer.parseInt(roleId),start,limit);
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String findByOtherRoleId()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String roleId = request.getParameter("roleId");
        StringBuilder json = new StringBuilder();
        PageResult ps = userGroupService.findCaUserByOtherRoleId(Integer.parseInt(roleId),start,limit);
        if(ps!=null){
            List<User> list = ps.getResults();
            int count =  ps.getAllResultsAmount();
            if(list!=null){
                json.append("{success:true,total:" + count + ",rows:[");
                Iterator<User> caUserIterator = list.iterator();
                while (caUserIterator.hasNext()){
                    User log = caUserIterator.next();
                    if(log!=null){
                        if(caUserIterator.hasNext()){
                        json .append( "{" +
                                "id:'" + log.getId() +
                                "',username:'" + log.getCn() +
                                "',id_card:'" + JsonUtil.checkNull(log.getId_card()).toString() +
//                                "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()).toString() +
                                "',deny_access:'" + log.getDeny_access() +
                                "',dynamic_ip:'" + log.getDynamic_ip() +
                                "',static_ip:'" + JsonUtil.checkNull(log.getStatic_ip()).toString() +
                                "',allow_all_subnet:'" + JsonUtil.checkNull(log.getAllow_all_subnet()).toString() +
                                "',allow_all_client:'" + JsonUtil.checkNull(log.getAllow_all_client()).toString() +
                                "',create_time:'" + log.getCreate_time() +
                                "',count_bytes_cycle:'" + log.getQuota_bytes() +
                                "',max_bytes:'" + log.getQuota_cycle() +
                                "',active:'" + log.getActive() +
                                "',email:'" + JsonUtil.checkNull(log.getEmail()).toString() +
                                "',phone:'" + JsonUtil.checkNull(log.getPhone()).toString() +
                                "',address:'" + JsonUtil.checkNull(log.getAddress()).toString() +
                                "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()).toString() +
                                "',type:'" + JsonUtil.checkNull(log.getType()).toString() +
//                                "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
                                "',revoked:'" + log.getRevoked() +
                                "',enabled:'" + log.getEnabled() +
                                "',real_address:'" + JsonUtil.checkNull(log.getReal_address()).toString() +
                                "',byte_received:'" + log.getByte_received() +
                                "',byte_send:'" + log.getByte_send() +
                                "',connected_since:'" + log.getConnected_since() +
                                "',virtual_address:'" + JsonUtil.checkNull(log.getVirtual_address()).toString() +
                                "',last_ref:'" + log.getLast_ref() + "'" +
                                "},");
                    } else {
                        json.append("{" +
                                "id:'" + log.getId() +
                                "',username:'" + log.getCn() +
                                "',id_card:'" + JsonUtil.checkNull(log.getId_card()).toString() +
//                                "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()).toString() +
                                "',deny_access:'" + log.getDeny_access() +
                                "',dynamic_ip:'" + log.getDynamic_ip() +
                                "',static_ip:'" + JsonUtil.checkNull(log.getStatic_ip()).toString() +
                                "',allow_all_subnet:'" + JsonUtil.checkNull(log.getAllow_all_subnet()).toString() +
                                "',allow_all_client:'" + JsonUtil.checkNull(log.getAllow_all_client()).toString() +
                                "',create_time:'" + log.getCreate_time() +
                                "',count_bytes_cycle:'" + log.getQuota_bytes() +
                                "',max_bytes:'" + log.getQuota_cycle() +
                                "',active:'" + log.getActive() +
                                "',email:'" + JsonUtil.checkNull(log.getEmail()).toString() +
                                "',phone:'" + JsonUtil.checkNull(log.getPhone()).toString() +
                                "',address:'" + JsonUtil.checkNull(log.getAddress()).toString() +
                                "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()).toString() +
                                "',type:'" + JsonUtil.checkNull(log.getType()).toString() +
//                                "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
                                "',revoked:'" + log.getRevoked() +
                                "',enabled:'" + log.getEnabled() +
                                "',real_address:'" + JsonUtil.checkNull(log.getReal_address()).toString() +
                                "',byte_received:'" + log.getByte_received() +
                                "',byte_send:'" + log.getByte_send() +
                                "',connected_since:'" + log.getConnected_since() +
                                "',virtual_address:'" + JsonUtil.checkNull(log.getVirtual_address()).toString() +
                                "',last_ref:'" + log.getLast_ref() + "'" +
                                "}");
                    }
                    }
                }
                json.append("]}");
            }
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String addUserToRoleId()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'添加失败'}";
        String roleId = request.getParameter("roleId");
        String[] uIds = request.getParameterValues("uIds");
        if(uIds!=null){
            for (String uId:uIds){
                try{
                    userGroupService.addUserToRoleId(Integer.parseInt(uId),Integer.parseInt(roleId));

                    User u =  userDao.findById(Integer.parseInt(uId));
                    Set<Groups> gp = null;
                    try {
                        gp = u.getGroupsSet();
                    }   catch (Exception e){

                    }
                    Groups temp = null;
                    if(gp!=null&&gp.size()>0){
                        temp = gp.iterator().next();
                    }
                    List<PrivateNet> nets =  privateNetDao.findAll();
                    if(temp!=null)  {
                        VPNConfigUtil.configUser(u, StringContext.user_manager_path, nets,temp);
                    }else {
                        VPNConfigUtil.configUser(u, StringContext.user_manager_path, nets);
                    }

                    json = "{success:true,msg:'添加成功'}";
                }catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String removeRoleIdUser()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String roleId = request.getParameter("roleId");
        String userId = request.getParameter("userId");
        String json = "{success:false,msg:'移除失败'}";
        try{
            userGroupService.delByRoleIdAndUserId(Integer.parseInt(roleId),Integer.parseInt(userId));
            User u =  userDao.findById(Integer.parseInt(userId));
            Set<Groups> gp = null;
            try {
                gp = u.getGroupsSet();
            }   catch (Exception e){

            }
            Groups temp = null;
            if(gp!=null&&gp.size()>0){
                temp = gp.iterator().next();
            }
            List<PrivateNet> nets =  privateNetDao.findAll();
            if(temp!=null)  {
                VPNConfigUtil.configUser(u, StringContext.user_manager_path, nets,temp);
            }else {
                VPNConfigUtil.configUser(u, StringContext.user_manager_path, nets);
            }
            json = "{success:true,msg:'移除成功'}";
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

}
