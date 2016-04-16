package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class KeyManager {
    public KeyManager() {
    }

    public KeyManager(String key_name, String key_file) {

        this.key_name = key_name;
        this.key_file = key_file;
    }

    private int id;
    private String key_name;
    private String key_file;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public String getKey_file() {
        return key_file;
    }

    public void setKey_file(String key_file) {
        this.key_file = key_file;
    }
}
