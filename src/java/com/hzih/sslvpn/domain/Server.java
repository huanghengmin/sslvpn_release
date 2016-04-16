package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-5
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    private int id;
    private String local; //服务监听地址
    private int port;
    private String protocol;
    private String server;
    private int traffic_server;
    private int client_to_client;
    private int duplicate_cn;
    private int keep_alive;
    private int keep_alive_interval;
    private String cipher;
    private int comp_lzo;
    private int max_clients;
    private int log_append;
    private int verb;
    private int mute;
    private String dynamic_net;
    private String static_net;
    private String group_default_net;
    private String private_net;
    private int allow_ping_server;
    private String allow_private_net;
    private int client_dns_type;
    private String client_first_dns;
    private String client_second_dns;
    private String default_domain_suffix;
    private int  use_connect_script;
    private int use_disconnect_script;
    private int use_learn_address_script;

    public int getUse_connect_script() {
        return use_connect_script;
    }

    public void setUse_connect_script(int use_connect_script) {
        this.use_connect_script = use_connect_script;
    }

    public int getUse_disconnect_script() {
        return use_disconnect_script;
    }

    public void setUse_disconnect_script(int use_disconnect_script) {
        this.use_disconnect_script = use_disconnect_script;
    }

    public int getUse_learn_address_script() {
        return use_learn_address_script;
    }

    public void setUse_learn_address_script(int use_learn_address_script) {
        this.use_learn_address_script = use_learn_address_script;
    }

    public int getClient_dns_type() {
        return client_dns_type;
    }

    public void setClient_dns_type(int client_dns_type) {
        this.client_dns_type = client_dns_type;
    }

    public String getClient_first_dns() {
        return client_first_dns;
    }

    public void setClient_first_dns(String client_first_dns) {
        this.client_first_dns = client_first_dns;
    }

    public String getClient_second_dns() {
        return client_second_dns;
    }

    public void setClient_second_dns(String client_second_dns) {
        this.client_second_dns = client_second_dns;
    }

    public String getDefault_domain_suffix() {
        return default_domain_suffix;
    }

    public void setDefault_domain_suffix(String default_domain_suffix) {
        this.default_domain_suffix = default_domain_suffix;
    }

    public String getDynamic_net() {
        return dynamic_net;
    }

    public void setDynamic_net(String dynamic_net) {
        this.dynamic_net = dynamic_net;
    }

    public String getStatic_net() {
        return static_net;
    }

    public void setStatic_net(String static_net) {
        this.static_net = static_net;
    }

    public String getGroup_default_net() {
        return group_default_net;
    }

    public void setGroup_default_net(String group_default_net) {
        this.group_default_net = group_default_net;
    }

    public String getPrivate_net() {
        return private_net;
    }

    public void setPrivate_net(String private_net) {
        this.private_net = private_net;
    }

    public int getAllow_ping_server() {
        return allow_ping_server;
    }

    public void setAllow_ping_server(int allow_ping_server) {
        this.allow_ping_server = allow_ping_server;
    }

    public String getAllow_private_net() {
        return allow_private_net;
    }

    public void setAllow_private_net(String allow_private_net) {
        this.allow_private_net = allow_private_net;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getTraffic_server() {
        return traffic_server;
    }

    public void setTraffic_server(int traffic_server) {
        this.traffic_server = traffic_server;
    }

    public int getClient_to_client() {
        return client_to_client;
    }

    public void setClient_to_client(int client_to_client) {
        this.client_to_client = client_to_client;
    }

    public int getDuplicate_cn() {
        return duplicate_cn;
    }

    public void setDuplicate_cn(int duplicate_cn) {
        this.duplicate_cn = duplicate_cn;
    }

    public int getKeep_alive() {
        return keep_alive;
    }

    public void setKeep_alive(int keep_alive) {
        this.keep_alive = keep_alive;
    }

    public int getKeep_alive_interval() {
        return keep_alive_interval;
    }

    public void setKeep_alive_interval(int keep_alive_interval) {
        this.keep_alive_interval = keep_alive_interval;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public int getComp_lzo() {
        return comp_lzo;
    }

    public void setComp_lzo(int comp_lzo) {
        this.comp_lzo = comp_lzo;
    }

    public int getMax_clients() {
        return max_clients;
    }

    public void setMax_clients(int max_clients) {
        this.max_clients = max_clients;
    }

    public int getLog_append() {
        return log_append;
    }

    public void setLog_append(int log_append) {
        this.log_append = log_append;
    }

    public int getVerb() {
        return verb;
    }

    public void setVerb(int verb) {
        this.verb = verb;
    }

    public int getMute() {
        return mute;
    }

    public void setMute(int mute) {
        this.mute = mute;
    }
}
