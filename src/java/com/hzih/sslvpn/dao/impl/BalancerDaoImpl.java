package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.BalancerDao;
import com.hzih.sslvpn.domain.Balancer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class BalancerDaoImpl extends MyDaoSupport implements BalancerDao {
    @Override
    public void setEntityClass() {
        this.entityClass = Balancer.class;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from Balancer s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(Balancer net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(Balancer net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(Balancer net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(net);
        flag = true;
        return flag;
    }

    @Override
    public Balancer findByHostAndPort(String host,int port) throws Exception {
        String hql=" from Balancer p where p.host ='"+host+"' and p.port ="+port;
        List<Balancer> balancerList  = super.getHibernateTemplate().find(hql);
        if(balancerList.size()>0&&balancerList!=null){
            return balancerList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Balancer findById(int id) throws Exception {
        String hql=" from Balancer p where p.id ="+id;
        List<Balancer> balancerList  = super.getHibernateTemplate().find(hql);
        if(balancerList.size()>0&&balancerList!=null){
            return balancerList.get(0);
        }else {
            return null;
        }
    }
}
