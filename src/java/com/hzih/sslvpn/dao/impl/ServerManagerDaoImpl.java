package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.ServerManagerDao;
import com.hzih.sslvpn.domain.ServerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class ServerManagerDaoImpl extends MyDaoSupport implements ServerManagerDao{

    @Override
    public ServerManager findById(int id) throws Exception {
        String hql = new String("from ServerManager where id ="+id);
        List list = null;
        try {
            list = getHibernateTemplate().find(hql);
        } catch (Exception e) {
            logger.error("ServerManager!",e);
        }
        ServerManager serverManager = (ServerManager) list.get(0);
        return serverManager;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = ServerManager.class;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from ServerManager s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(ServerManager serverManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(serverManager);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(ServerManager serverManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(serverManager);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(ServerManager serverManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(serverManager);
        flag = true;
        return flag;
    }
}
