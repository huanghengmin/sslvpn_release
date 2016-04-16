package com.hzih.sslvpn.web.action.sslvpn.server;

import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.action.ActionBase;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-29
 * Time: 下午12:48
 * To change this template use File | Settings | File Templates.
 */
public class ClientControl extends ActionSupport {
    private Logger logger = Logger.getLogger(ClientControl.class);

    private UserDao userDao;

    private PrivateNetDao privateNetDao;

    public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String view_user()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String json ="{success:false}";
        String result =	actionBase.actionBegin(request);
        String username = request.getParameter("username");
        User user = userDao.findByCommonName(username);
        if(null!=user){
//            logger.info(user);
        user.setView_flag(1);
        boolean flag = userDao.modify(user);
        if(flag)
        json ="{success:true}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

     public String kill_user()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json ="{success:false}";
        String username = request.getParameter("username");
        Proc kill_proc = new Proc();
        String kill_command = "sh "+ StringContext.systemPath+"/script/kill_user.sh "+username;
        kill_proc.exec(kill_command);
//        String out = kill_proc.getOutput();
//        logger.info(out);
//        if(!out.contains("ERROR")){
            json ="{success:true}"; 
//        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String disable_user()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json ="{success:false}";
        String username = request.getParameter("username");
//        String serial = request.getParameter("serial");
        if(null!=username){
            User user = userDao.findByCommonName(username);
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
}
