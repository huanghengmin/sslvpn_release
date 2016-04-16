package com.hzih.sslvpn.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupPrivateNetsDao;
import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.domain.GroupPrivateNets;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.service.GroupPrivateNetsService;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */
public class GroupPrivateNetsServiceImpl implements GroupPrivateNetsService{
    private PrivateNetDao privateNetDao;
    private GroupPrivateNetsDao groupPrivateNetsDao;

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    public GroupPrivateNetsDao getGroupPrivateNetsDao() {
        return groupPrivateNetsDao;
    }

    public void setGroupPrivateNetsDao(GroupPrivateNetsDao groupPrivateNetsDao) {
        this.groupPrivateNetsDao = groupPrivateNetsDao;
    }

    @Override
    public PageResult findCaPermissionsByOtherRoleId(int roleId, int start, int limit) {
        return groupPrivateNetsDao.findCaPermissionsByOtherRoleId(roleId,start,limit);
    }

    @Override
    public String  getPerminssionsByRoleId(int roleId, int start, int limit) throws Exception {
        PageResult ps = groupPrivateNetsDao.getPerminssionsByRoleId(roleId,start,limit);
        StringBuilder json = new StringBuilder();
        if(ps!=null){
            List<GroupPrivateNets> list = ps.getResults();
            int count =  ps.getAllResultsAmount();
            if(list!=null){
                json.append("{success:true,total:" + count + ",rows:[");
                Iterator<GroupPrivateNets> rolePermissionIterator = list.iterator();
                while (rolePermissionIterator.hasNext()){
                    GroupPrivateNets caRolePermission = rolePermissionIterator.next();
                    if(caRolePermission!=null){
                        PrivateNet log = privateNetDao.findById(caRolePermission.getPrivate_net_id());
                        if(rolePermissionIterator.hasNext()){
                            json.append("{");
                            json.append("id:'").append(log.getId());
                            json.append("',net:'" ).append( log.getNet());
                            json.append("',net_mask:'" ).append( log.getNet_mask()).append("'");
                            json.append("},");
                        }else {
                            json.append("{");
                            json.append("id:'").append(log.getId());
                            json.append("',net:'" ).append( log.getNet());
                            json.append("',net_mask:'" ).append( log.getNet_mask()).append("'");
                            json.append("}");
                        }
                    }
                }
            }
            json.append("]}");
        }
        return json.toString();
    }

    @Override
    public void addPermissionsToRoleId(String pIds, int roleId) throws Exception {
        groupPrivateNetsDao.addPermissionsToRoleId(pIds,roleId);
    }

    @Override
    public void addPermissionToRoleId(int pId, int roleId) throws Exception {
        groupPrivateNetsDao.addPermissionToRoleId(pId,roleId);
    }

    @Override
    public boolean delAllCaPermissionsByRoleId(int roleId) throws Exception {
        return groupPrivateNetsDao.delAllCaPermissionsByRoleId(roleId);
    }

    @Override
    public void delByRoleIdAndPermissionId(int i, int i1)throws Exception {
        groupPrivateNetsDao.delByRoleIdAndPermissionId(i,i1);
    }

    @Override
    public void delByPermissionId(int i) throws Exception {
        groupPrivateNetsDao.delByPermissionId(i);
    }
}
