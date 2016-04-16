package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class CaManager {

    public CaManager() {
    }

    public CaManager(String ca_name, String ca_file) {
        this.ca_name = ca_name;
        this.ca_file = ca_file;
    }

    private int id;
    private String ca_name;
    private String ca_file;

    private int status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCa_name() {
        return ca_name;
    }

    public void setCa_name(String ca_name) {
        this.ca_name = ca_name;
    }

    public String getCa_file() {
        return ca_file;
    }

    public void setCa_file(String ca_file) {
        this.ca_file = ca_file;
    }
}
