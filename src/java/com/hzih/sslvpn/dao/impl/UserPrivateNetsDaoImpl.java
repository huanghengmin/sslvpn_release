package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.UserPrivateNetsDao;
import com.hzih.sslvpn.domain.UserPrivateNets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-13
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
public class UserPrivateNetsDaoImpl extends MyDaoSupport implements UserPrivateNetsDao{

    @Override
    public void setEntityClass() {
        this.entityClass = UserPrivateNets.class;
    }

    @Override
    public PageResult findOtherUserIdPrivateNets(int user_id, int start, int limit) {
        final String hql = "from PrivateNet u2 where u2.id not in (select u.id from PrivateNet u,UserPrivateNets r where u.id=r.private_net_id and r.user_id="+user_id+") order by u2.id asc";
        int pageIndex = start/limit+1;
        List paramsList = new ArrayList();
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),pageIndex, limit);
        return ps;
    }

    @Override
    public void addPrivateNetToUser(int i, int i1) {
        super.getHibernateTemplate().save(new UserPrivateNets(i,i1));
    }

    @Override
    public void removePrivateNetToUser(int i, int i1) {
        super.getHibernateTemplate().delete(new UserPrivateNets(i,i1));
    }
}
