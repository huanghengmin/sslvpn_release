package com.hzih.sslvpn.dao;

import com.hzih.sslvpn.domain.RouteUser;

/**
 * Created by hhm on 2014/12/17.
 */
public interface RouteUserDao {
    public boolean add(RouteUser routeUser)throws Exception;

    public boolean modify(RouteUser routeUser)throws Exception;

    public boolean delete(RouteUser routeUser)throws Exception;

    public RouteUser findById(int id)throws Exception;

    public RouteUser findByUserName(String name)throws Exception;
}
