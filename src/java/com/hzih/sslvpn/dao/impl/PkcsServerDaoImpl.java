package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.PkcsServerDao;
import com.hzih.sslvpn.domain.PkcsServer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-17
 * Time: 下午1:26
 * To change this template use File | Settings | File Templates.
 */
public class PkcsServerDaoImpl extends MyDaoSupport implements PkcsServerDao{

    @Override
    public void setEntityClass() {
        this.entityClass = PkcsServer.class;
    }

    @Override
    public PkcsServer findPkcsServer() throws Exception {
        String hql="from PkcsServer p where p.id = 1";
        List<PkcsServer> pkcsServers  = super.getHibernateTemplate().find(hql);
        if(pkcsServers.size()>0&&pkcsServers!=null){
            return pkcsServers.get(0);
        }else {
            return null;
        }
    }

    @Override
    public boolean updatePkcsServer(PkcsServer pkcsServer) throws Exception {
        super.getHibernateTemplate().update(pkcsServer);
        return true;
    }
}
