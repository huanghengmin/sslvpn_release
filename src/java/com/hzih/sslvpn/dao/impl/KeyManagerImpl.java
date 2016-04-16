package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.KeyManagerDao;
import com.hzih.sslvpn.domain.KeyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */
public class KeyManagerImpl extends MyDaoSupport implements KeyManagerDao{

    @Override
    public KeyManager findById(int id) throws Exception {
        String hql = new String("from KeyManager where id ="+id);
        List list = null;
        try {
            list = getHibernateTemplate().find(hql);
        } catch (Exception e) {
            logger.error("KeyManager!",e);
        }
        KeyManager keyManager = (KeyManager) list.get(0);
        return keyManager;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from KeyManager s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(KeyManager keyManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(keyManager);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(KeyManager keyManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().update(keyManager);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(KeyManager keyManager) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(keyManager);
        flag = true;
        return flag;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = KeyManager.class;
    }
}
