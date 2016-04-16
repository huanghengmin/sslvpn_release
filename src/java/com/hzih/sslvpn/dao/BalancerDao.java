package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.Balancer;

import java.util.Date;

public interface BalancerDao extends BaseDao {
	PageResult listByPage(int pageIndex, int limit);

	public boolean add(Balancer balancer)throws Exception;

	public boolean modify(Balancer balancer)throws Exception;

	public boolean delete(Balancer balancer)throws Exception;

	public Balancer findByHostAndPort(String host,int port)throws Exception;

	public Balancer findById(int id)throws Exception;

}
