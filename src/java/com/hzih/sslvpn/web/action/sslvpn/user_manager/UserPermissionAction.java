package com.hzih.sslvpn.web.action.sslvpn.user_manager;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.*;
import com.hzih.sslvpn.domain.*;
import com.hzih.sslvpn.utils.JsonUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.action.ActionBase;
//import com.hzih.sslvpn.web.action.sslvpn.ra.RaConfigXml;
//import com.hzih.sslvpn.web.action.sslvpn.ra.RaControlClient;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午6:58
 * To change this template use File | Settings | File Templates.
 */
public class UserPermissionAction extends ActionSupport {
    private Logger logger = Logger.getLogger(UserPermissionAction.class);
    private UserDao userDao;
    private StaticIpDao staticIpDao;
    private RouteUserDao routeUserDao;

    public RouteUserDao getRouteUserDao() {
        return routeUserDao;
    }

    public void setRouteUserDao(RouteUserDao routeUserDao) {
        this.routeUserDao = routeUserDao;
    }

    public StaticIpDao getStaticIpDao() {
        return staticIpDao;
    }

    public void setStaticIpDao(StaticIpDao staticIpDao) {
        this.staticIpDao = staticIpDao;
    }

    private ServerDao serverDao;

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    private UserPrivateNetsDao userPrivateNetsDao;

    private PrivateNetDao privateNetDao;

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    public UserPrivateNetsDao getUserPrivateNetsDao() {
        return userPrivateNetsDao;
    }

    public void setUserPrivateNetsDao(UserPrivateNetsDao userPrivateNetsDao) {
        this.userPrivateNetsDao = userPrivateNetsDao;
    }

    private User user;
    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String add() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'添加用户失败'}";
        User old = userDao.checkCn(user.getCn() + "_" + user.getId_card());
        if (null != old) {
            json = "{success:false,msg:'用户名已存在!'}";
        } else {
            userDao.add(user);
            String u_name = user.getCn();
            User old_user = userDao.findByCommonName(u_name);
            List<PrivateNet> nets = privateNetDao.findAll();
            VPNConfigUtil.configUser(old_user, StringContext.user_manager_path, nets);
            json = "{success:true,msg:'添加用户成功!'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String modify() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新用户失败'}";
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        if (null != old) {
            old.setCn(user.getCn());
            old.setId_card(user.getId_card());
            old.setEmail(user.getEmail());
            old.setPhone(user.getPhone());
            old.setAddress(user.getAddress());
            if (user.getNet_id() != null)
                old.setNet_id(user.getNet_id());
            if (user.getTerminal_id() != null)
                old.setTerminal_id(user.getTerminal_id());
            userDao.modify(old);
            List<PrivateNet> nets = privateNetDao.findAll();
            VPNConfigUtil.configUser(old, StringContext.user_manager_path, nets);
            json = "{success:true,msg:'更新用户成功'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        userDao.delete(old);
        File file = new File(StringContext.user_manager_path + "/" + old.getCn());
        if (file.exists()) {
            file.delete();
        }
        json = "{success:true,msg:'删除成功'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String cleanThreeYards() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'清除用户三码绑定状态失败'}";
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        old.setNet_id("");
        old.setTerminal_id("");
        userDao.modify(old);
        json = "{success:true,msg:'清除用户三码绑定状态成功'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String cn = request.getParameter("cn");
        String email = request.getParameter("email");
        String id_card = request.getParameter("id_card");
        String status = request.getParameter("status");
        int enable = -1;
        if (null != status && !status.equals("")) {
            enable = Integer.parseInt(status);
        }
        PageResult pageResult = userDao.findByPages(cn, email, id_card, enable, start, limit);
        if (pageResult != null) {
            List<User> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<User> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    User log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',username:'" + log.getCn() +
                                "',id_card:'" + JsonUtil.checkNull(log.getId_card()) +
//                                "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()) +
                                "',deny_access:'" + log.getDeny_access() +
                                "',dynamic_ip:'" + log.getDynamic_ip() +
                                "',static_ip:'" + JsonUtil.checkNull(log.getStatic_ip()) +
                                "',allow_all_subnet:'" + JsonUtil.checkNull(log.getAllow_all_subnet()) +
                                "',allow_all_client:'" + JsonUtil.checkNull(log.getAllow_all_client()) +
                                "',create_time:'" + log.getCreate_time() +
                                "',count_bytes_cycle:'" + log.getQuota_bytes() +
                                "',max_bytes:'" + log.getQuota_cycle() +
                                "',active:'" + log.getActive() +
                                "',email:'" + JsonUtil.checkNull(log.getEmail()) +
                                "',phone:'" + JsonUtil.checkNull(log.getPhone()) +
                                "',address:'" + JsonUtil.checkNull(log.getAddress()) +
                                "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()) +
                                "',type:'" + JsonUtil.checkNull(log.getType()) +
//                                "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
                                "',revoked:'" + log.getRevoked() +
                                "',enabled:'" + log.getEnabled() +
                                "',real_address:'" + JsonUtil.checkNull(log.getReal_address()) +
                                "',byte_received:'" + log.getByte_received() +
                                "',byte_send:'" + log.getByte_send() +
                                "',connected_since:'" + log.getConnected_since() +
                                "',virtual_address:'" + JsonUtil.checkNull(log.getVirtual_address()) +

                                "',net_id:'" + JsonUtil.checkNull(log.getNet_id()) +
                                "',terminal_id:'" + JsonUtil.checkNull(log.getTerminal_id()) +

                                "',last_ref:'" + log.getLast_ref() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',username:'" + log.getCn() +
                                "',id_card:'" + JsonUtil.checkNull(log.getId_card()) +
//                                "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()) +
                                "',deny_access:'" + log.getDeny_access() +
                                "',dynamic_ip:'" + log.getDynamic_ip() +
                                "',static_ip:'" + JsonUtil.checkNull(log.getStatic_ip()) +
                                "',allow_all_subnet:'" + JsonUtil.checkNull(log.getAllow_all_subnet()) +
                                "',allow_all_client:'" + JsonUtil.checkNull(log.getAllow_all_client()) +
                                "',create_time:'" + log.getCreate_time() +
                                "',count_bytes_cycle:'" + log.getQuota_bytes() +
                                "',max_bytes:'" + log.getQuota_cycle() +
                                "',active:'" + log.getActive() +
                                "',email:'" + JsonUtil.checkNull(log.getEmail()) +
                                "',phone:'" + JsonUtil.checkNull(log.getPhone()) +
                                "',address:'" + JsonUtil.checkNull(log.getAddress()) +
                                "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()) +
                                "',type:'" + JsonUtil.checkNull(log.getType()) +
//                                "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
                                "',revoked:'" + log.getRevoked() +
                                "',enabled:'" + log.getEnabled() +
                                "',real_address:'" + JsonUtil.checkNull(log.getReal_address()) +
                                "',byte_received:'" + log.getByte_received() +
                                "',byte_send:'" + log.getByte_send() +
                                "',connected_since:'" + log.getConnected_since() +
                                "',virtual_address:'" + JsonUtil.checkNull(log.getVirtual_address()) +

                                "',net_id:'" + JsonUtil.checkNull(log.getNet_id()) +
                                "',terminal_id:'" + JsonUtil.checkNull(log.getTerminal_id()) +

                                "',last_ref:'" + log.getLast_ref() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String findRouteUsers() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        User u = userDao.findById(Integer.parseInt(id));
        if (u != null) {
            Set<RouteUser> routeUsers = u.getRouteUsers();
            if (routeUsers != null && routeUsers.size() > 0) {
                String json = "{success:true,total:" + routeUsers.size() + ",rows:[";
                Iterator<RouteUser> raUserIterator = routeUsers.iterator();
                while (raUserIterator.hasNext()) {
                    RouteUser log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',user_name:'" + log.getUser_name() +
                                "',user_idcard:'" + log.getUser_idcard() +
                                "',user_province:'" + log.getUser_province() +
                                "',user_city:'" + log.getUser_city() +
                                "',user_organization:'" + log.getUser_organization() +
                                "',user_institution:'" + log.getUser_institution() +
                                "',user_phone:'" + log.getUser_phone() +
                                "',user_address:'" + log.getUser_address() +
                                "',user_email:'" + log.getUser_email() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',user_name:'" + log.getUser_name() +
                                "',user_idcard:'" + log.getUser_idcard() +
                                "',user_province:'" + log.getUser_province() +
                                "',user_city:'" + log.getUser_city() +
                                "',user_organization:'" + log.getUser_organization() +
                                "',user_institution:'" + log.getUser_institution() +
                                "',user_phone:'" + log.getUser_phone() +
                                "',user_address:'" + log.getUser_address() +
                                "',user_email:'" + log.getUser_email() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);

            }
        }
        return null;
    }

    public String findTerminalByRouteUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        RouteUser u = routeUserDao.findById(Integer.parseInt(id));
        if (u != null) {
            Terminal terminal = u.getTerminal();
            String json = "{success:true,total:" + 1 + ",rows:[";
            json += "{" +
                    "id:'" + terminal.getId() +
                    "',terminal_name:'" + terminal.getTerminal_name() +
                    "',terminal_type:'" + terminal.getTerminal_type() +
                    "',user_name:'" + terminal.getUser_name() +
                    "',terminal_status:'" + terminal.getTerminal_status() +
                    "',terminal_desc:'" + terminal.getTerminal_desc() +
                    "',ip:'" + terminal.getIp() +
                    "',mac:'" + terminal.getMac() +
                    "',on_line:'" + terminal.getOn_line() + "'" +
                    "}";
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }

    public String disable() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'禁用用户失败'}";
        String id = request.getParameter("id");
        /*User user1 =  userDao.findById(Integer.parseInt(id));
        String raIp = RaConfigXml.getAttribute(RaConfigXml.ra_ip);
        if(raIp!=null&&raIp.length()>0) {
            RaControlClient vpn = new RaControlClient();
            vpn.init(raIp);
            boolean flag = vpn.vpnblock(user1.getSerial_number(), user1.getCn());
            if (flag) {
                json = "{success:true,msg:'禁用用户成功'}";
                logger.info("禁止用户" + user1.getCn() + "访问成功!");
            } else {
                json = "{success:true,msg:'禁用用户失败'}";
                logger.info("禁止用户" + user1.getCn() + "访问失败!");
            }
        }else {
            json = "{success:true,msg:'禁用用户失败,未找到配置'}";
            logger.info("禁止用户" + user1.getCn() + "访问失败，未找到配置!");
        }*/
        boolean old = userDao.disableUser(Integer.parseInt(id));
        if (old) {
            User oldUser = userDao.findById(Integer.parseInt(id));
            if (null != oldUser) {
//            User user = userDao.findByCommonName(oldUser.getSerial_number());
                oldUser.setEnabled(0);
                userDao.disableUser(oldUser.getId());
//            userDao.update(user);

                Proc kill_proc = new Proc();
                String kill_command = "sh " + StringContext.systemPath + "/script/kill_user.sh " + oldUser.getCn();
                kill_proc.exec(kill_command);

                List<PrivateNet> nets = privateNetDao.findAll();
                VPNConfigUtil.configUser(oldUser, StringContext.user_manager_path, nets);
                json = "{success:true,msg:'禁用用户成功'}";
                logger.info("禁止用户" + oldUser.getCn() + "访问成功!");
            }
//        List<PrivateNet> nets =  privateNetDao.findAll();
//        VPNConfigUtil.configUser(oldUser, StringContext.user_manager_path,nets);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String disable_ByCn() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json ="{success:false}";
        String username = request.getParameter("username");
//        String serial = request.getParameter("serial");
        if(null!=username){
            User user = userDao.findByCommonName(username);
            /*String raIp = RaConfigXml.getAttribute(RaConfigXml.ra_ip);
            if(raIp!=null&&raIp.length()>0) {
                RaControlClient vpn = new RaControlClient();
                vpn.init(raIp);
                boolean flag = vpn.vpnblock(user.getSerial_number(), user.getCn());
                if (flag) {
                    json = "{success:true,msg:'禁用用户成功'}";
                    logger.info("禁止用户" + user.getCn() + "访问成功!");
                } else {
                    json = "{success:true,msg:'禁用用户失败'}";
                    logger.info("禁止用户" + user.getCn() + "访问失败!");
                }
            }else {
                json = "{success:true,msg:'禁用用户失败,未找到配置'}";
                logger.info("禁止用户" + user.getCn() + "访问失败，未找到配置!");
            }*/
            if(null!=user){
                user.setEnabled(0);
                userDao.disableUser(user.getId());
//            userDao.update(user);
                Proc kill_proc = new Proc();
                String kill_command = "sh "+ StringContext.systemPath+"/script/kill_user.sh "+username;
                kill_proc.exec(kill_command);

                List<PrivateNet> nets =  privateNetDao.findAll();
                VPNConfigUtil.configUser(user, StringContext.user_manager_path,nets);
                json="success:true";
                logger.info("禁止用户"+user.getCn()+"访问成功!");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String enable() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'启用用户失败'}";
        String id = request.getParameter("id");
        boolean old = userDao.enableUser(Integer.parseInt(id));
        if (old)
            json = "{success:true,msg:'启用用户成功'}";
        User oldUser = userDao.findById(Integer.parseInt(id));
        List<PrivateNet> nets = privateNetDao.findAll();
        VPNConfigUtil.configUser(oldUser, StringContext.user_manager_path, nets);
        logger.info("启用用户" + oldUser.getCn() + "访问成功!");
        /*User user1 =  userDao.findById(Integer.parseInt(id));
        String raIp = RaConfigXml.getAttribute(RaConfigXml.ra_ip);
        if(raIp!=null&&raIp.length()>0) {
            RaControlClient vpn = new RaControlClient();
            vpn.init(raIp);
            boolean flag = vpn.vpnnoblock(user1.getSerial_number(), user1.getCn());
            if (flag) {
                json = "{success:true,msg:'启用用户成功'}";
                logger.info("启用用户" + user1.getCn() + "访问成功!");
            } else {
                json = "{success:true,msg:'禁用用户失败'}";
                logger.info("启用用户" + user1.getCn() + "访问失败!");
            }
        }else {
            json = "{success:true,msg:'启用用户失败,未找到配置'}";
            logger.info("启用用户" + user1.getCn() + "访问失败，未找到配置!");
        }*/
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String addNetsToUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'添加子网失败'}";
        String userId = request.getParameter("userId");
        String[] pIds = request.getParameterValues("pIds");
        if (pIds != null) {
            try {
                for (String uId : pIds) {
                    userPrivateNetsDao.addPrivateNetToUser(Integer.parseInt(userId), Integer.parseInt(uId));
                }
                User old = userDao.findById(Integer.parseInt(userId));
                List<PrivateNet> nets = privateNetDao.findAll();
                VPNConfigUtil.configUser(old, StringContext.user_manager_path, nets);
                json = "{success:true,msg:'添加子网成功'}";
            } catch (Exception e) {
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String removeNetForUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'移除子网失败'}";
        String userId = request.getParameter("userId");
        String pId = request.getParameter("pId");
        userPrivateNetsDao.removePrivateNetToUser(Integer.parseInt(userId), Integer.parseInt(pId));
        User old = userDao.findById(Integer.parseInt(userId));
        List<PrivateNet> nets = privateNetDao.findAll();
        VPNConfigUtil.configUser(old, StringContext.user_manager_path, nets);
        json = "{success:true,msg:'移除子网成功'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findOtherUserIdNets() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        PageResult pageResult = userPrivateNetsDao.findOtherUserIdPrivateNets(Integer.parseInt(id), start, limit);
        if (pageResult != null) {
            List<PrivateNet> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<PrivateNet> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    PrivateNet log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String findUserNets() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        if (null != old) {
            Set<PrivateNet> nets = old.getUser_subNets();
            if (nets != null) {
                String json = "{success:true,total:" + nets.size() + ",rows:[";
                Iterator<PrivateNet> raUserIterator = nets.iterator();
                while (raUserIterator.hasNext()) {
                    PrivateNet log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String updateUserPermission() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新用户权限失败'}";
        String id = request.getParameter("id");
        String client_to_client = request.getParameter("allow_client_to_client");
        String allow_all_nets = request.getParameter("allow_all_nets");
        String dynamic_ip = request.getParameter("dynamic_ip");
        String static_ip = request.getParameter("static_ip");
        User old = userDao.findById(Integer.parseInt(id));
        int dynamic = Integer.parseInt(dynamic_ip);
        if (dynamic == 0) {
            Server server = serverDao.findDefaultServer();
            String start_static_ip = static_ip.substring(0, static_ip.lastIndexOf("."));
            String end = static_ip.substring(static_ip.lastIndexOf(".") + 1, static_ip.length());
//            if (server.getStatic_net().startsWith(start_static_ip)) {
            StaticIp staticIp = staticIpDao.findById(Integer.parseInt(end));
            if (null != staticIp) {
                if (null != old)
                    if (null != client_to_client && client_to_client.equals("on"))
                        old.setAllow_all_client(1);
                    else
                        old.setAllow_all_client(0);
                if (null != allow_all_nets && allow_all_nets.equals("on"))
                    old.setAllow_all_subnet(1);
                else
                    old.setAllow_all_subnet(0);
                old.setDynamic_ip(Integer.parseInt(dynamic_ip));
                old.setStatic_ip(static_ip);
                userDao.update(old);

                List<PrivateNet> nets = privateNetDao.findAll();
                VPNConfigUtil.configUser(old, StringContext.user_manager_path, nets);
                json = "{success:true,msg:'更新用户权限成功'}";
            } else {
                json = "{success:false,msg:'更新用户权限失败!,请确认IP未位是4的倍数+1'}";
            }
//            } else {
//                json = "{success:false,msg:'更新用户权限失败!,请确定IP在静态IP组:" + server.getStatic_net() + ",且IP未位是4的倍数+1'}";
//            }
        } else {
            if (null != old)
                if (null != client_to_client && client_to_client.equals("on"))
                    old.setAllow_all_client(1);
                else
                    old.setAllow_all_client(0);
            if (null != allow_all_nets && allow_all_nets.equals("on"))
                old.setAllow_all_subnet(1);
            else
                old.setAllow_all_subnet(0);
            old.setDynamic_ip(Integer.parseInt(dynamic_ip));
            old.setStatic_ip("");
            userDao.update(old);

            List<PrivateNet> nets = privateNetDao.findAll();
            VPNConfigUtil.configUser(old, StringContext.user_manager_path, nets);
            json = "{success:true,msg:'更新用户权限成功'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findUserPermission() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        String json = "{success:true,totalCount:" + 1 + ",root:[";
        if (null != old) {
            json += "{";
            if (old.getAllow_all_client() == 1)
                json += "allow_client_to_client:'on',";
            else
                json += "allow_client_to_client:'off',";
            if (old.getAllow_all_subnet() == 1) {
                json += "allow_all_nets:'on',";
            } else {
                json += "allow_all_nets:'off',";
            }
            json += "dynamic_ip:'" + JsonUtil.checkNull(old.getDynamic_ip()) + "',";

            json += "static_ip:'" + JsonUtil.checkNull(old.getStatic_ip()) + "'" + "}";
        }
        json += "]}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

}
