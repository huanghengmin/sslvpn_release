package com.hzih.sslvpn.domain;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-13
 * Time: 上午11:32
 * To change this template use File | Settings | File Templates.
 */
public class UserPrivateNets implements Serializable {
    private int user_id;
    private int private_net_id;

    public UserPrivateNets() {
    }

    public UserPrivateNets(int user_id, int private_net_id) {
        this.user_id = user_id;
        this.private_net_id = private_net_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPrivate_net_id() {
        return private_net_id;
    }

    public void setPrivate_net_id(int private_net_id) {
        this.private_net_id = private_net_id;
    }
}
