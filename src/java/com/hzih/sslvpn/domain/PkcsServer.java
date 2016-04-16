package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-17
 * Time: 下午1:22
 * To change this template use File | Settings | File Templates.
 */
public class PkcsServer {
    private int id;
    private String pkcs_crt;
    private String pkcs_name;
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

    public String getPkcs_crt() {
        return pkcs_crt;
    }

    public void setPkcs_crt(String pkcs_crt) {
        this.pkcs_crt = pkcs_crt;
    }

    public String getPkcs_name() {
        return pkcs_name;
    }

    public void setPkcs_name(String pkcs_name) {
        this.pkcs_name = pkcs_name;
    }
}
