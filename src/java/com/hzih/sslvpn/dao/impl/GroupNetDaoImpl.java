package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupNetDao;
import com.hzih.sslvpn.domain.GroupNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午1:27
 * To change this template use File | Settings | File Templates.
 */
public class GroupNetDaoImpl extends MyDaoSupport implements GroupNetDao{

        @Override
        public void setEntityClass() {
            this.entityClass = GroupNet.class;
        }

        @Override
        public PageResult listByPage(int pageIndex, int limit) {
            String hql = " from GroupNet s where 1=1";
            List paramsList = new ArrayList();

            String countHql = "select count(*) " + hql;

            PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                    pageIndex, limit);
            return ps;
        }

        @Override
        public boolean add(GroupNet net) throws Exception {
            boolean flag =false;
            super.getHibernateTemplate().save(net);
            flag = true;
            return flag;
        }

        @Override
        public boolean modify(GroupNet net) throws Exception {
            boolean flag =false;
            super.getHibernateTemplate().saveOrUpdate(net);
            flag = true;
            return flag;
        }

        @Override
        public boolean delete(GroupNet net) throws Exception {
            boolean flag =false;
            super.getHibernateTemplate().delete(net);
            flag = true;
            return flag;
        }

    @Override
    public GroupNet findByNet(String net) throws Exception {
        String hql="from GroupNet p where p.net ='"+net+"'";
        List<GroupNet> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }

    @Override
    public GroupNet findById(int id) throws Exception {
        String hql="from GroupNet p where p.id ="+id;
        List<GroupNet> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }
}
