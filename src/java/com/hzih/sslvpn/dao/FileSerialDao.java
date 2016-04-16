package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:02
 * To change this template use File | Settings | File Templates.
 */
public interface FileSerialDao extends BaseDao {

    public void update_serial(String update_flag,int update_num)throws Exception;
    
    public int get_serial(String get_flag)throws Exception;
}
