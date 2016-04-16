package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午2:58
 * To change this template use File | Settings | File Templates.
 */
public class FileSerial {
    private int id;
    private int ca_serial;
    private int server_serial;
    private int key_serial;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCa_serial() {
        return ca_serial;
    }

    public void setCa_serial(int ca_serial) {
        this.ca_serial = ca_serial;
    }

    public int getServer_serial() {
        return server_serial;
    }

    public void setServer_serial(int server_serial) {
        this.server_serial = server_serial;
    }

    public int getKey_serial() {
        return key_serial;
    }

    public void setKey_serial(int key_serial) {
        this.key_serial = key_serial;
    }
}
