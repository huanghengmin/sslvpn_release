package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午9:51
 * To change this template use File | Settings | File Templates.
 */
public interface GroupPrivateNetsDao extends BaseDao {
    //得到角色对应的资源
    public PageResult getPerminssionsByRoleId(int groupId, int start, int limit)throws Exception;
    //添加资源到角色
    public void addPermissionsToRoleId(String pIds, int groupId)throws Exception;

    //删除所有groupId 关联
    public boolean delAllCaPermissionsByRoleId(int groupId)throws Exception;

    public PageResult findCaPermissionsByOtherRoleId(int groupId, int start, int limit);

    public void addPermissionToRoleId(int pId, int groupId)throws Exception;

    void delByRoleIdAndPermissionId(int i, int i1)throws Exception;

    void delByPermissionId(int i)throws Exception;
}
