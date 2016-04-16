package com.hzih.sslvpn.domain;

/**
 * Created by hhm on 2014/12/17.
 */
public class Terminal {
    private RouteUser routeUser;
    private int id;
    private String terminal_name;
    private String terminal_type;
    private String user_name;
    private int terminal_status;
    private String terminal_desc;
    private String ip;
    private String mac;
    private int on_line;

    public RouteUser getRouteUser() {
        return routeUser;
    }

    public void setRouteUser(RouteUser routeUser) {
        this.routeUser = routeUser;
    }

    public Terminal() {
    }

    public Terminal(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerminal_name() {
        return terminal_name;
    }

    public void setTerminal_name(String terminal_name) {
        this.terminal_name = terminal_name;
    }

    public String getTerminal_type() {
        return terminal_type;
    }

    public void setTerminal_type(String terminal_type) {
        this.terminal_type = terminal_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getTerminal_status() {
        return terminal_status;
    }

    public void setTerminal_status(int terminal_status) {
        this.terminal_status = terminal_status;
    }

    public String getTerminal_desc() {
        return terminal_desc;
    }

    public void setTerminal_desc(String terminal_desc) {
        this.terminal_desc = terminal_desc;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getOn_line() {
        return on_line;
    }

    public void setOn_line(int on_line) {
        this.on_line = on_line;
    }
}
