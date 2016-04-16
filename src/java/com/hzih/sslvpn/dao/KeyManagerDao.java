package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.KeyManager;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:29
 * To change this template use File | Settings | File Templates.
 */
public interface KeyManagerDao extends BaseDao{
    PageResult listByPage(int pageIndex, int limit);

    public boolean add(KeyManager keyManager)throws Exception;

    public boolean modify(KeyManager keyManager)throws Exception;

    public boolean delete(KeyManager keyManager)throws Exception;

    KeyManager findById(int id) throws Exception;
}
