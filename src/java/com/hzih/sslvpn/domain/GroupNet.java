package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public class GroupNet {
    public GroupNet(int id) {
        this.id = id;
    }

    public GroupNet() {
    }

    public GroupNet(String net, String net_mask) {
        this.net = net;
        this.net_mask = net_mask;
    }

    private int id;
    private String net;
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
