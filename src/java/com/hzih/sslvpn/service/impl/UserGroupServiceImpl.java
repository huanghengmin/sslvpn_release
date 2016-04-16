package com.hzih.sslvpn.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.dao.UserGroupDao;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.domain.UserGroup;
import com.hzih.sslvpn.service.UserGroupService;
import com.hzih.sslvpn.utils.JsonUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午10:58
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupServiceImpl implements UserGroupService {
    private UserGroupDao userGroupDao;
    private UserDao userDao;

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String getUsersByRoleId(int roleId, int start, int limit) throws Exception {
        PageResult ps = userGroupDao.getUsersByRoleId(roleId,start,limit);
        StringBuilder json = new StringBuilder();
        if(ps!=null){
            List<UserGroup> list = ps.getResults();
            int count =  ps.getAllResultsAmount();
            if(list!=null){
                json.append("{success:true,total:" + count + ",rows:[");
                Iterator<UserGroup> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    UserGroup caUserRole = raUserIterator.next();
                    if(caUserRole!=null){
                        User log = userDao.findById(caUserRole.getUser_id());
                        if(raUserIterator.hasNext()){
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
            }
            json.append("]}");
        }
        return json.toString();
    }

    @Override
    public void addUsersToRoleId(String uIds, int roleId) throws Exception {
        userGroupDao.addUsersToRoleId(uIds,roleId);
    }

    @Override
    public void addUserToRoleId(int uId, int roleId) throws Exception {
        userGroupDao.addUserToRoleId(uId,roleId);
    }

    @Override
    public PageResult findCaUserByOtherRoleId(int roleId, int start, int limit) {
        return userGroupDao.findCaUserByOtherRoleId(roleId,start,limit);
    }

    @Override
    public boolean delAllByRoleId(int roleId) throws Exception {
        return userGroupDao.delAllByRoleId(roleId);
    }

    @Override
    public void delByRoleIdAndUserId(int i, int i1)throws Exception {
        userGroupDao.delByRoleIdAndUserId(i,i1);
    }

    @Override
    public void delByUserId(int i) throws Exception {
        userGroupDao.delByUserId(i);
    }

    @Override
    public List<UserGroup> findByUserId(int id) throws Exception {
        return userGroupDao.findByUserId(id);
    }
}
