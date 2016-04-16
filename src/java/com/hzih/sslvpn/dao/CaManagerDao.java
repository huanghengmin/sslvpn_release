package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.CaManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public interface CaManagerDao extends BaseDao{
    public CaManager findById(int id)throws Exception;

    PageResult listByPage(int pageIndex, int limit);

    public boolean add(CaManager caManager)throws Exception;

    public boolean modify(CaManager caManager)throws Exception;

    public boolean delete(CaManager caManager)throws Exception;

    public boolean modify_check_no()throws Exception;

    boolean modify_check_on(int id) throws Exception;

    List<CaManager> findAllCheck();
}
