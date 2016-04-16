package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.GroupNet;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午1:23
 * To change this template use File | Settings | File Templates.
 */
public interface GroupNetDao  extends BaseDao {
    PageResult listByPage(int pageIndex, int limit);

    public boolean add(GroupNet net)throws Exception;

    public boolean modify(GroupNet net)throws Exception;

    public boolean delete(GroupNet net)throws Exception;

    public GroupNet findByNet(String net)throws Exception;

    public GroupNet findById(int id)throws Exception;
}
