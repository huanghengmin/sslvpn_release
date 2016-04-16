package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.sslvpn.domain.PkcsServer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-17
 * Time: 下午1:25
 * To change this template use File | Settings | File Templates.
 */
public interface PkcsServerDao extends BaseDao{

    public PkcsServer findPkcsServer()throws Exception;

    boolean updatePkcsServer(PkcsServer pkcsServer) throws Exception;
}
