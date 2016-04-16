package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.UserRouteUserDao;
import com.hzih.sslvpn.domain.UserRouteUser;

import java.util.List;

/**
 * Created by hhm on 2014/12/17.
 */
public class UserRouteUserDaoImpl extends MyDaoSupport implements UserRouteUserDao {
    @Override
    public void setEntityClass() {
        this.entityClass = UserRouteUser.class;
    }

    @Override
    public boolean add(UserRouteUser userRouteUser) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(userRouteUser);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(UserRouteUser userRouteUser) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(userRouteUser);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(UserRouteUser userRouteUser) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(userRouteUser);
        flag = true;
        return flag;
    }

    @Override
    public UserRouteUser findById(int id) throws Exception {
        String hql="from UserRouteUser u where u.user_id ="+id;
        List<UserRouteUser> userRouteUsers  = super.getHibernateTemplate().find(hql);
        if(userRouteUsers.size()>0&&userRouteUsers!=null){
            return userRouteUsers.get(0);
        }else {
            return null;
        }
    }


    @Override
    public UserRouteUser findByObj(int uid,int urId){
        String hql="from UserRouteUser u where u.user_id ="+uid+" and u.route_user_id = "+urId;
        List<UserRouteUser> userRouteUsers  = super.getHibernateTemplate().find(hql);
        if(userRouteUsers.size()>0&&userRouteUsers!=null){
            return userRouteUsers.get(0);
        }else {
            return null;
        }
    }
}
