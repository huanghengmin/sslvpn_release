package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.domain.PrivateNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class PrivateNetDaoImpl extends MyDaoSupport implements PrivateNetDao {
    @Override
    public void setEntityClass() {
        this.entityClass = PrivateNet.class;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from PrivateNet s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(PrivateNet net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(PrivateNet net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(PrivateNet net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(net);
        flag = true;
        return flag;
    }

    @Override
    public PrivateNet findByNet(String net) throws Exception {
        String hql="from PrivateNet p where p.net ='"+net+"'";
        List<PrivateNet> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }

    @Override
    public PrivateNet findById(int id) throws Exception {
        String hql="from PrivateNet p where p.id ="+id;
        List<PrivateNet> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }
}
