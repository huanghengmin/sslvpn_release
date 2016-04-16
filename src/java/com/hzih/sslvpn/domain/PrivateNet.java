package com.hzih.sslvpn.domain;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public class PrivateNet {
    private Set<Groups> groupsSet;

    public Set<Groups> getGroupsSet() {
        return groupsSet;
    }

    public void setGroupsSet(Set<Groups> groupsSet) {
        this.groupsSet = groupsSet;
    }

    public PrivateNet(int id) {
        this.id = id;
    }

    public PrivateNet() {
    }

    public PrivateNet(String net_mask, String net) {
        this.net_mask = net_mask;
        this.net = net;
    }

    private int id;
    private String net;
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    private String net_mask;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getNet_mask() {
        return net_mask;
    }

    public void setNet_mask(String net_mask) {
        this.net_mask = net_mask;
    }
}
