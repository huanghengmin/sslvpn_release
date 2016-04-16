package com.hzih.sslvpn.web.action.sslvpn.control;

import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.dao.UserPrivateNetsDao;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.servlet.crl.CrlTimingUpdate;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.action.sslvpn.crl.CRLXMLUtils;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.cert.CRL;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hhm
 * Date: 12-11-29
 * Time: 上午10:36
 * To change this template use File | Settings | File Templates.
 */
public class ControlAction extends ActionSupport {

    private Logger logger = Logger.getLogger(ControlAction.class);

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private UserPrivateNetsDao userPrivateNetsDao;

    private PrivateNetDao privateNetDao;

    public UserPrivateNetsDao getUserPrivateNetsDao() {
        return userPrivateNetsDao;
    }

    public void setUserPrivateNetsDao(UserPrivateNetsDao userPrivateNetsDao) {
        this.userPrivateNetsDao = userPrivateNetsDao;
    }

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    /**
     * enable user
     * @return
     * @throws Exception
     */
    public String enable() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String json = "{success:false}";
//        String username = request.getParameter("username");
        String serial = request.getParameter("serial");
        if (null != serial) {
            User user = userDao.findBySerialNumber(serial);
            logger.info(user);
            if (null != user) {
                user.setEnabled(1);
                userDao.enableUser(user.getId());

                List<PrivateNet> nets = privateNetDao.findAll();
                VPNConfigUtil.configUser(user, StringContext.user_manager_path, nets);
                json = "success:true";
                logger.info("启用用户" + user.getCn() + "访问成功!");
            }
        }
        writer.write(json);
        writer.close();
        return null;
    }

    /**
     * disable user
     * @return
     * @throws Exception
     */
    public String disable() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String json = "{success:false}";
//        String username = request.getParameter("username");
        String serial = request.getParameter("serial");
        String revoked = request.getParameter("revoked");
        if (null != serial) {
            User user = userDao.findBySerialNumber(serial);
            if (null != user) {
                user.setEnabled(0);
                userDao.disableUser(user.getId());
                //            userDao.update(user);

                Proc kill_proc = new Proc();
                String kill_command = "sh " + StringContext.systemPath + "/script/kill_user.sh " + user.getCn();
                kill_proc.exec(kill_command);

                List<PrivateNet> nets = privateNetDao.findAll();
                VPNConfigUtil.configUser(user, StringContext.user_manager_path, nets);
                if (null != revoked && revoked.equals("true")) {
                    new Thread() {
                        @Override
                        public void run() {
                            CrlTimingUpdate crlTimingUpdate = new CrlTimingUpdate();
                            crlTimingUpdate.down_crl(CRLXMLUtils.getValue(CRLXMLUtils.url));
                        }
                    }.start();
                }
                json = "success:true";
                logger.info("禁止用户" + user.getCn() + "访问成功!");
            }
        }
        writer.write(json);
        writer.close();
        return null;
    }

    public String remoteDisable() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String json = "not found";
        String serial = request.getParameter("serial");
        if (null != serial) {
            User user = userDao.findBySerialNumber(serial);
            if (null != user) {
                try {
                    user.setEnabled(0);
                    userDao.disableUser(user.getId());
                    //userDao.update(user);
                    Proc kill_proc = new Proc();
                    String kill_command = "sh " + StringContext.systemPath + "/script/kill_user.sh " + user.getCn();
                    kill_proc.exec(kill_command);
                    List<PrivateNet> nets = privateNetDao.findAll();
                    VPNConfigUtil.configUser(user, StringContext.user_manager_path, nets);
                    json = "ok";
                    logger.info("禁止用户" + user.getCn() + "访问成功!");
                } catch (Exception e) {
                    json = "failed";
                }
            }
        } else {
            json = "not found";
        }
        writer.write(json);
        writer.close();
        return null;
    }

    public String kill_disable() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String json = "{success:false}";
//        String username = request.getParameter("username");
        String serial = request.getParameter("serial");
//        String revoked = request.getParameter("revoked");
        if (null != serial) {
            User user = userDao.findBySerialNumber(serial);

            if (null != user) {
                user.setEnabled(0);
                userDao.disableUser(user.getId());
//            userDao.update(user);

                Proc kill_proc = new Proc();
                String kill_command = "sh " + StringContext.systemPath + "/script/kill_user.sh " + user.getCn();
                kill_proc.exec(kill_command);

//                List<PrivateNet> nets =  privateNetDao.findAll();
//                VPNConfigUtil.configUser(user, StringContext.user_manager_path,nets);
//                if(null!=revoked&&revoked.equals("true")){
//                    new Thread()
//                    {
//                        @Override
//                        public void run() {
//                            CrlTimingUpdate crlTimingUpdate = new CrlTimingUpdate();
//                            crlTimingUpdate.down_crl();
//                        }
//                    }.start();
//                }

                json = "success:true";
//                logger.info("禁止用户"+user.getCn()+"访问成功!");
            }
        }
        writer.write(json);
        writer.close();
        return null;
    }

}
