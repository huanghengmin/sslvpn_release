package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.CaManager;
import com.hzih.sslvpn.dao.CaManagerDao;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class CaManagerDaoImpl extends MyDaoSupport implements CaManagerDao{
    @Override
    public CaManager findById(int id) throws Exception {
        String hql = new String("from CaManager where id ="+id);
        List list = null;
        try {
            list = getHibernateTemplate().find(hql);
        } catch (Exception e) {
            logger.error("caManager查找出错!",e);
        }
        CaManager caManager = (CaManager) list.get(0);
        return caManager;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from CaManager s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(CaManager caManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(caManager);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(CaManager caManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(caManager);
        flag = true;
        return flag;
    }
    

    @Override
    public boolean delete(CaManager caManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(caManager);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify_check_no() throws Exception {
        String s ="update CaManager caManager set caManager.status = 0";
        Session session = super.getSession();
        session.beginTransaction();
        Query query = session.createQuery(s);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean modify_check_on(int id) throws Exception {
        String s ="update CaManager caManager set caManager.status = 1 where caManager.id="+id;
        Session session = super.getSession();
        session.beginTransaction();
        Query query = session.createQuery(s);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public List<CaManager> findAllCheck() {
        String hql = " from CaManager s where status=1";
        List<CaManager> list = null;
        try {
            list = getHibernateTemplate().find(hql);
            if(null!=list)
                return list;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = CaManager.class;
    }
}
