package com.hzih.sslvpn.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 14-4-16
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
public class GroupPrivateNets  implements Serializable{
    private int group_id;
    private int private_net_id;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getPrivate_net_id() {
        return private_net_id;
    }

    public void setPrivate_net_id(int private_net_id) {
        this.private_net_id = private_net_id;
    }

    public GroupPrivateNets() {
    }

    public GroupPrivateNets(int group_id, int private_net_id) {

        this.group_id = group_id;
        this.private_net_id = private_net_id;
    }
}
