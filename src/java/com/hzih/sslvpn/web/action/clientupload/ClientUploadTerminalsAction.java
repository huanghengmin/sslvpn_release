package com.hzih.sslvpn.web.action.clientupload;

import com.hzih.sslvpn.dao.RouteUserDao;
import com.hzih.sslvpn.dao.TerminalDao;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.dao.UserRouteUserDao;
import com.hzih.sslvpn.domain.*;
import com.hzih.sslvpn.service.LogService;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hhm on 2014/12/16.
 */
public class ClientUploadTerminalsAction extends ActionSupport {
    private Logger logger = Logger.getLogger(ClientGpsUploadAction.class);

    private LogService logService;

    private UserDao userDao;

    private TerminalDao terminalDao;

    private RouteUserDao routeUserDao;


    private UserRouteUserDao userRouteUserDao;

    public UserRouteUserDao getUserRouteUserDao() {
        return userRouteUserDao;
    }

    public void setUserRouteUserDao(UserRouteUserDao userRouteUserDao) {
        this.userRouteUserDao = userRouteUserDao;
    }

    public RouteUserDao getRouteUserDao() {
        return routeUserDao;
    }

    public void setRouteUserDao(RouteUserDao routeUserDao) {
        this.routeUserDao = routeUserDao;
    }

    public TerminalDao getTerminalDao() {
        return terminalDao;
    }

    public void setTerminalDao(TerminalDao terminalDao) {
        this.terminalDao = terminalDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String uploadTerminals() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String msg = "保存接入终端类型信息失败";
        String json = "{success:false,msg:'" + msg + "'}";
        /**
         * 路由器终端用户信息
         */
        String data = request.getParameter("data");
        if (data != null && data.length() > 0) {
            JSONObject jsonObject = JSONObject.fromObject(data);
            String cn = jsonObject.get("cn").toString();
            String datetime = jsonObject.get("datetime").toString();
            try {
                User user = userDao.findByCommonName(cn);
                if (user != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("userLists");
                    if (jsonArray != null) {
                        if (jsonArray != null && jsonArray.size() > 0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (object != null) {
                                    String user_name = object.get("user_name").toString();
                                    String user_idCard = object.get("user_idCard").toString();
                                    String user_province = object.get("user_province").toString();
                                    String user_city = object.get("user_city").toString();
                                    String user_organization = object.get("user_organization").toString();
                                    String user_institution = object.get("user_institution").toString();
                                    String user_phone = object.get("user_phone").toString();
                                    String user_address = object.get("user_address").toString();
                                    String user_email = object.get("user_email").toString();

                                    RouteUser routeUser_f = null;
                                    try {
                                        routeUser_f = routeUserDao.findByUserName(user_name);
                                    } catch (Exception e) {

                                    }
                                    if (routeUser_f != null) {
                                        routeUser_f.setUser_name(user_name);
                                        routeUser_f.setUser_idcard(user_idCard);
                                        routeUser_f.setUser_province(user_province);
                                        routeUser_f.setUser_city(user_city);
                                        routeUser_f.setUser_organization(user_organization);
                                        routeUser_f.setUser_institution(user_institution);
                                        routeUser_f.setUser_phone(user_phone);
                                        routeUser_f.setUser_address(user_address);
                                        routeUser_f.setUser_email(user_email);
                                        routeUserDao.modify(routeUser_f);
                                    } else {
                                        RouteUser routeUser = new RouteUser();
                                        routeUser.setUser_name(user_name);
                                        routeUser.setUser_idcard(user_idCard);
                                        routeUser.setUser_province(user_province);
                                        routeUser.setUser_city(user_city);
                                        routeUser.setUser_organization(user_organization);
                                        routeUser.setUser_institution(user_institution);
                                        routeUser.setUser_phone(user_phone);
                                        routeUser.setUser_address(user_address);
                                        routeUser.setUser_email(user_email);
                                        routeUserDao.add(routeUser);

                                        try {
                                            routeUser_f = routeUserDao.findByUserName(user_name);
                                        } catch (Exception e) {

                                        }
                                    }
                                    UserRouteUser u = null;
                                    try {
                                        u = userRouteUserDao.findByObj(user.getId(), routeUser_f.getId());
                                    } catch (Exception e) {

                                    }
                                    if (u == null && routeUser_f != null && user != null) {
                                        UserRouteUser userRouteUser = new UserRouteUser();
                                        userRouteUser.setUser_id(user.getId());
                                        userRouteUser.setRoute_user_id(routeUser_f.getId());
                                        userRouteUserDao.modify(userRouteUser);
                                    }

                                    JSONObject j_obj = object.getJSONObject("terminal");
                                    if (j_obj != null) {
                                        String terminal_name = j_obj.get("terminal_name").toString();
                                        String terminal_type = j_obj.get("terminal_type").toString();
                                        String t_user_name = j_obj.get("user_name").toString();
                                        String status = j_obj.get("status").toString();
                                        String desc = j_obj.get("desc").toString();
                                        String ip = j_obj.get("ip").toString();
                                        String mac = j_obj.get("mac").toString();
                                        String on_line = j_obj.get("on_line").toString();

                                        Terminal terminal_f = null;
                                        try {
                                            terminal_f = terminalDao.findByName(t_user_name);
                                        } catch (Exception e) {

                                        }
                                        if (terminal_f == null) {
                                            Terminal terminal = new Terminal();
                                            terminal.setUser_name(t_user_name);
                                            terminal.setTerminal_name(terminal_name);
                                            terminal.setTerminal_type(terminal_type);
                                            terminal.setTerminal_status(Integer.parseInt(status));
                                            terminal.setTerminal_desc(desc);
                                            terminal.setIp(ip);
                                            terminal.setMac(mac);
                                            terminal.setOn_line(Integer.parseInt(on_line));
                                            terminal.setRouteUser(routeUser_f);
//                                terminal.setId(routeUser_f.getId());
                                            terminalDao.add(terminal);
                                        } else {
                                            terminal_f.setUser_name(t_user_name);
                                            terminal_f.setTerminal_name(terminal_name);
                                            terminal_f.setTerminal_type(terminal_type);
                                            terminal_f.setTerminal_status(Integer.parseInt(status));
                                            terminal_f.setTerminal_desc(desc);
                                            terminal_f.setIp(ip);
                                            terminal_f.setMac(mac);
                                            terminal_f.setOn_line(Integer.parseInt(on_line));
                                            terminal_f.setRouteUser(routeUser_f);
//                                terminal_f.setId(routeUser_f.getId());
                                            terminalDao.modify(terminal_f);
                                        }
                                    }
                                }
                            }
                        }
                        msg = "保存接入终端类型信息成功,时间:" + datetime;
                        json = "{success:true,msg:'" + msg + "'}";
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    } else {
                        msg = "上报终端列表数据为空,时间:" + datetime;
                        json = "{success:false,msg:'" + msg + "'}";
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        writer.write(json);
        writer.flush();
        writer.close();

        /**
         * 路由器接入终端用户信息，包含设备绑定信息
         */
        /**
         * 终端用户数据格式
         String u_t_msg = "{'cn':'','idCard':'','province':'','city':'city','organization':'','institution':'','phone':'','address':'','email':'','userLists':[{'user_name':'','user_idCard':'','user_province':'','user_city':'','user_organization':'','user_institution':'','user_phone':'','user_address':'','user_email':'',terminal:{'terminal_name':'','terminal_type':'','user_name':'','status':'','desc':'','ip':'','mac':'','on_line':''}},{......}],'datetime':''}";
         */
        return null;
    }

}
