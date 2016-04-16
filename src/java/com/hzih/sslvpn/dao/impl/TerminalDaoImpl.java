package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.TerminalDao;
import com.hzih.sslvpn.domain.Terminal;

import java.util.List;

/**
 * Created by hhm on 2014/12/17.
 */
public class TerminalDaoImpl extends MyDaoSupport implements TerminalDao {
    @Override
    public void setEntityClass() {
        this.entityClass = Terminal.class;
    }

    @Override
    public boolean add(Terminal terminal) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(terminal);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(Terminal terminal) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(terminal);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(Terminal terminal) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(terminal);
        flag = true;
        return flag;
    }

    @Override
    public Terminal findById(int id) throws Exception {
        String hql="from Terminal terminal where terminal.id ="+id;
        List<Terminal> terminalList  = super.getHibernateTemplate().find(hql);
        if(terminalList.size()>0&&terminalList!=null){
            return terminalList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Terminal findByName(String username) {
        String hql="from Terminal terminal where terminal.user_name ='"+username+"'";
        List<Terminal> terminalList  = super.getHibernateTemplate().find(hql);
        if(terminalList.size()>0&&terminalList!=null){
            return terminalList.get(0);
        }else {
            return null;
        }
    }
}
