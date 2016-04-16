package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.PrivateNet;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public interface PrivateNetDao extends BaseDao{
    PageResult listByPage(int pageIndex, int limit);

    public boolean add(PrivateNet net)throws Exception;

    public boolean modify(PrivateNet net)throws Exception;

    public boolean delete(PrivateNet net)throws Exception;
    
    public PrivateNet findByNet(String net)throws Exception;

    public PrivateNet findById(int id)throws Exception;

}
