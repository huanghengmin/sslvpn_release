package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-13
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
public interface UserPrivateNetsDao extends BaseDao{
    PageResult findOtherUserIdPrivateNets(int user_id, int start, int limit);

    void addPrivateNetToUser(int i, int i1);

    void removePrivateNetToUser(int i, int i1);
}
