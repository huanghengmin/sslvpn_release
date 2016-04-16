package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class ServerManager {

    public ServerManager() {
    }

    public ServerManager(String server_name, String server_file) {
        this.server_name = server_name;
        this.server_file = server_file;
    }

    private int id;
    private String server_name;
    private String server_file;
    private String cn;
    private String notBefore;
    private String notAfter;

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    public String getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getServer_file() {
        return server_file;
    }

    public void setServer_file(String server_file) {
        this.server_file = server_file;
    }
}
