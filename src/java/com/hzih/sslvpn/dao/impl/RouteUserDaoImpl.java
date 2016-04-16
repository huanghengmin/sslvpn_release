package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.RouteUserDao;
import com.hzih.sslvpn.domain.RouteUser;

import java.util.List;

/**
 * Created by hhm on 2014/12/17.
 */
public class RouteUserDaoImpl extends MyDaoSupport implements RouteUserDao {
    @Override
    public void setEntityClass() {
        this.entityClass = RouteUser.class;
    }

    @Override
    public boolean add(RouteUser routeUser) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(routeUser);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(RouteUser routeUser) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(routeUser);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(RouteUser routeUser) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(routeUser);
        flag = true;
        return flag;
    }

    @Override
    public RouteUser findById(int id) throws Exception {
        String hql="from RouteUser r where r.id ="+id;
        List<RouteUser> routeUserList  = super.getHibernateTemplate().find(hql);
        if(routeUserList.size()>0&&routeUserList!=null){
            return routeUserList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public RouteUser findByUserName(String name) throws Exception {
        String hql="from RouteUser r where r.user_name ='"+name+"'";
        List<RouteUser> routeUserList  = super.getHibernateTemplate().find(hql);
        if(routeUserList.size()>0&&routeUserList!=null){
            return routeUserList.get(0);
        }else {
            return null;
        }
    }
}
