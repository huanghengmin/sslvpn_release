package com.hzih.sslvpn.web.action.sslvpn.user_manager;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupDao;
import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.domain.Groups;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.service.GroupPrivateNetsService;
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
public class GroupPrivateNetsAction extends ActionSupport {
    private Logger logger = Logger.getLogger(GroupPrivateNetsAction.class);
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

    private GroupPrivateNetsService groupPrivateNetsService;

    private PrivateNetDao privateNetDao;

    private GroupDao groupDao;

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public GroupPrivateNetsService getGroupPrivateNetsService() {
        return groupPrivateNetsService;
    }

    public void setGroupPrivateNetsService(GroupPrivateNetsService groupPrivateNetsService) {
        this.groupPrivateNetsService = groupPrivateNetsService;
    }

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
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
        String json = groupPrivateNetsService.getPerminssionsByRoleId(Integer.parseInt(roleId),start,limit);
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
        PageResult ps = groupPrivateNetsService.findCaPermissionsByOtherRoleId(Integer.parseInt(roleId),start,limit);
        if(ps!=null){
            List<PrivateNet> list = ps.getResults();
            int count =  ps.getAllResultsAmount();
            if(list!=null){
                json.append("{success:true,total:" + count + ",rows:[");
                Iterator<PrivateNet> rolePermissionIterator = list.iterator();
                while (rolePermissionIterator.hasNext()){
                    PrivateNet log = rolePermissionIterator.next();
                    if(log!=null){
                        if(rolePermissionIterator.hasNext()){
                            json.append("{");
                            json.append("id:'").append(log.getId());
                            json.append("',net:'").append( log.getNet());
                            json.append("',net_mask:'" ).append( log.getNet_mask()).append("'");
                            json.append("},");
                        }else {
                            json.append("{");
                            json.append("id:'").append(log.getId());
                            json.append("',net:'").append(log.getNet());
                            json.append("',net_mask:'" ).append( log.getNet_mask()).append("'");
                            json.append("}");
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
    public String addPermissionsToRoleId()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String json = "{success:false,msg:'添加失败'}";
        String result = actionBase.actionBegin(request);
        String roleId = request.getParameter("roleId");
        String[] pIds = request.getParameterValues("pIds");

        if(pIds!=null){
            for (String uId:pIds){
                try{
                    groupPrivateNetsService.addPermissionToRoleId(Integer.parseInt(uId),Integer.parseInt(roleId));
                    Groups gp =  groupDao.findById(Integer.parseInt(roleId));


                    List<PrivateNet> nets =  privateNetDao.findAll();
                    if(gp!=null)  {

                        Set<User> users =null;
                        try {
                            users = gp.getUserSet();
                        }catch (Exception e){

                        }
                        if(users!=null&&users.size()>0){
                            for (User user : users){
                                VPNConfigUtil.configUser(user, StringContext.user_manager_path, nets,gp);
                            }
                        }
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



    public String removeRoleIdPermisson()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String roleId = request.getParameter("roleId");
        String pId = request.getParameter("pId");
        String json = "{success:false,msg:'移除失败'}";
        try{
            groupPrivateNetsService.delByRoleIdAndPermissionId(Integer.parseInt(pId),Integer.parseInt(roleId));
            Groups gp =  groupDao.findById(Integer.parseInt(roleId));


            List<PrivateNet> nets =  privateNetDao.findAll();
            if(gp!=null)  {

                Set<User> users =null;
                try {
                    users = gp.getUserSet();
                }catch (Exception e){

                }
                if(users!=null&&users.size()>0){
                    for (User user : users){
                        VPNConfigUtil.configUser(user, StringContext.user_manager_path, nets,gp);
                    }
                }
            }

            json = "{success:false,msg:'移除成功'}";
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
