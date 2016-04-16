package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.ServerManager;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
public interface ServerManagerDao extends BaseDao{
    PageResult listByPage(int pageIndex, int limit);

    public boolean add(ServerManager serverManager)throws Exception;

    public boolean modify(ServerManager serverManager)throws Exception;

    public boolean delete(ServerManager serverManager)throws Exception;

    ServerManager findById(int id) throws Exception;
}
