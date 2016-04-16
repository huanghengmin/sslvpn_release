package com.hzih.sslvpn.service;

import cn.collin.commons.domain.PageResult;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午10:56
 * To change this template use File | Settings | File Templates.
 */
public interface GroupPrivateNetsService {
    //找出非roleId的用户
    public PageResult findCaPermissionsByOtherRoleId(int roleId, int start, int limit);

    //得到角色对应的资源
    public String  getPerminssionsByRoleId(int roleId, int start, int limit)throws Exception;

    //添加资源到角色
    public void addPermissionsToRoleId(String pIds, int roleId)throws Exception;

    public void addPermissionToRoleId(int pId, int roleId)throws Exception;

    //删除所有roleId 关联
    public boolean delAllCaPermissionsByRoleId(int roleId)throws Exception;

    void delByRoleIdAndPermissionId(int i, int i1)throws Exception;

    void delByPermissionId(int i)throws Exception;
}
