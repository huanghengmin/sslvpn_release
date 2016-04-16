package com.hzih.sslvpn.dao;

import com.hzih.sslvpn.domain.UserRouteUser;

/**
 * Created by hhm on 2014/12/17.
 */
public interface UserRouteUserDao  {
    public boolean add(UserRouteUser userRouteUser)throws Exception;

    public boolean modify(UserRouteUser userRouteUser)throws Exception;

    public boolean delete(UserRouteUser userRouteUser)throws Exception;

    public UserRouteUser findById(int id)throws Exception;

    UserRouteUser findByObj(int id, int id1);
}
