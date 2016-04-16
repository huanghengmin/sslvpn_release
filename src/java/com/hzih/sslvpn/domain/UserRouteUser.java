package com.hzih.sslvpn.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by hhm on 2014/12/17.
 */
public class UserRouteUser implements Serializable {
    private int user_id;
    private int route_user_id;

    public UserRouteUser() {
    }

    public UserRouteUser(int user_id, int route_user_id) {
        this.user_id = user_id;
        this.route_user_id = route_user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRoute_user_id() {
        return route_user_id;
    }

    public void setRoute_user_id(int route_user_id) {
        this.route_user_id = route_user_id;
    }
}
