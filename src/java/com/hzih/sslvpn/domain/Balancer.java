package com.hzih.sslvpn.domain;

/**
 * Created by Administrator on 15-9-17.
 */
public class Balancer {
    private int id;
    private String host;
    private int port;

    public Balancer() {
    }

    public Balancer(int id) {
        this.id = id;
    }

    public Balancer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
