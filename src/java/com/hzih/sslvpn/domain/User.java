package com.hzih.sslvpn.domain;

import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-2
 * Time: 下午8:02
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private Set<Groups> groupsSet;

    public Set<Groups> getGroupsSet() {
        return groupsSet;
    }

    public void setGroupsSet(Set<Groups> groupsSet) {
        this.groupsSet = groupsSet;
    }

    private int id;
    private String cn;
    private String id_card;
    private int deny_access = 0;
    private int dynamic_ip = 1;
    private String static_ip;
    private int allow_all_subnet = 1;
    private int allow_all_client = 0;
    private Date create_time;
    private int quota_cycle = 60;
    private Long quota_bytes = 300000l;
    private int active = 1;
    private String email;
    private String phone;
    private String address;
    private String serial_number;
    private String type;
    private int revoked;
    private int enabled = 1;
    private String real_address;
    private long byte_received;
    private long byte_send;
    private Date connected_since;
    private String virtual_address;
    private Date last_ref;
    private long count_bytes_cycle;
    private long max_bytes;
    private String net_id;
    private String terminal_id;
    private String province;
    private String status;
    private String revoke_status;
    private int view_flag;
    private String issueCa;
    private Date create_Date;
    private Date end_Date;
    private String employeeCode;
    private String orgCode;
    private String orgName;
    private String city;
    private String organization;
    private String institutions;
    private String description;

    private Set<PrivateNet> user_subNets;

    private Set<RouteUser> routeUsers;

    private Set<UserGps> userGpses;

    public Set<UserGps> getUserGpses() {
        return userGpses;
    }

    public void setUserGpses(Set<UserGps> userGpses) {
        this.userGpses = userGpses;
    }

    public Set<RouteUser> getRouteUsers() {
        return routeUsers;
    }

    public void setRouteUsers(Set<RouteUser> routeUsers) {
        this.routeUsers = routeUsers;
    }

    @Override
    public String toString() {
        return "User{" +
                "cn='" + cn + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }


    public String getRevoke_status() {
        return revoke_status;
    }

    public void setRevoke_status(String revoke_status) {
        this.revoke_status = revoke_status;
    }

    public String getNet_id() {
        return net_id;
    }

    public void setNet_id(String net_id) {
        this.net_id = net_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public int getView_flag() {
        return view_flag;
    }

    public void setView_flag(int view_flag) {
        this.view_flag = view_flag;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIssueCa() {
        return issueCa;
    }

    public void setIssueCa(String issueCa) {
        this.issueCa = issueCa;
    }

    public Date getCreate_Date() {
        return create_Date;
    }

    public void setCreate_Date(Date create_Date) {
        this.create_Date = create_Date;
    }

    public Date getEnd_Date() {
        return end_Date;
    }

    public void setEnd_Date(Date end_Date) {
        this.end_Date = end_Date;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getInstitutions() {
        return institutions;
    }

    public void setInstitutions(String institutions) {
        this.institutions = institutions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDeny_access() {
        return deny_access;
    }

    public void setDeny_access(int deny_access) {
        this.deny_access = deny_access;
    }

    public int getDynamic_ip() {
        return dynamic_ip;
    }

    public void setDynamic_ip(int dynamic_ip) {
        this.dynamic_ip = dynamic_ip;
    }

    public String getStatic_ip() {
        return static_ip;
    }

    public void setStatic_ip(String static_ip) {
        this.static_ip = static_ip;
    }

    public int getAllow_all_subnet() {
        return allow_all_subnet;
    }

    public void setAllow_all_subnet(int allow_all_subnet) {
        this.allow_all_subnet = allow_all_subnet;
    }

    public int getAllow_all_client() {
        return allow_all_client;
    }

    public void setAllow_all_client(int allow_all_client) {
        this.allow_all_client = allow_all_client;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getQuota_cycle() {
        return quota_cycle;
    }

    public void setQuota_cycle(int quota_cycle) {
        this.quota_cycle = quota_cycle;
    }

    public Long getQuota_bytes() {
        return quota_bytes;
    }

    public void setQuota_bytes(Long quota_bytes) {
        this.quota_bytes = quota_bytes;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRevoked() {
        return revoked;
    }

    public void setRevoked(int revoked) {
        this.revoked = revoked;
    }

    public Set<PrivateNet> getUser_subNets() {
        return user_subNets;
    }

    public void setUser_subNets(Set<PrivateNet> user_subNets) {
        this.user_subNets = user_subNets;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getReal_address() {
        return real_address;
    }

    public void setReal_address(String real_address) {
        this.real_address = real_address;
    }

    public long getByte_received() {
        return byte_received;
    }

    public void setByte_received(long byte_received) {
        this.byte_received = byte_received;
    }

    public long getByte_send() {
        return byte_send;
    }

    public void setByte_send(long byte_send) {
        this.byte_send = byte_send;
    }

    public Date getConnected_since() {
        return connected_since;
    }

    public void setConnected_since(Date connected_since) {
        this.connected_since = connected_since;
    }

    public String getVirtual_address() {
        return virtual_address;
    }

    public void setVirtual_address(String virtual_address) {
        this.virtual_address = virtual_address;
    }

    public Date getLast_ref() {
        return last_ref;
    }

    public void setLast_ref(Date last_ref) {
        this.last_ref = last_ref;
    }

    public long getCount_bytes_cycle() {
        return count_bytes_cycle;
    }

    public void setCount_bytes_cycle(long count_bytes_cycle) {
        this.count_bytes_cycle = count_bytes_cycle;
    }

    public long getMax_bytes() {
        return max_bytes;
    }

    public void setMax_bytes(long max_bytes) {
        this.max_bytes = max_bytes;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
