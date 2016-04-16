package com.hzih.sslvpn.domain;

import java.util.Set;

/**
 * Created by hhm on 2014/12/17.
 */
public class RouteUser {
    private int id;
    private String user_name;
    private Terminal terminal;
    private String user_idcard;
    private String user_province;
    private String user_city;
    private String user_organization;
    private String user_institution;
    private String user_phone;
    private String user_address;
    private String user_email;
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public RouteUser(int id) {
        this.id = id;
    }

    public RouteUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public String getUser_idcard() {
        return user_idcard;
    }

    public void setUser_idcard(String user_idcard) {
        this.user_idcard = user_idcard;
    }

    public String getUser_province() {
        return user_province;
    }

    public void setUser_province(String user_province) {
        this.user_province = user_province;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_organization() {
        return user_organization;
    }

    public void setUser_organization(String user_organization) {
        this.user_organization = user_organization;
    }

    public String getUser_institution() {
        return user_institution;
    }

    public void setUser_institution(String user_institution) {
        this.user_institution = user_institution;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
