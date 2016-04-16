package com.hzih.sslvpn.utils;

import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.User;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-19
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
public class VPNKeepRefuse extends Thread {

    private Logger logger = Logger.getLogger(VPNKeepRefuse.class);

    private User user;

    private PrivateNetDao privateNetDao;

    public VPNKeepRefuse(User user, PrivateNetDao privateNetDao) {
        this.user = user;
        this.privateNetDao = privateNetDao;
    }

    @Override
    public void run() {
        user.setEnabled(0);
        List<PrivateNet> nets = privateNetDao.findAll();
        try {
            VPNConfigUtil.configUser(user, StringContext.user_manager_path, nets);
            Thread.sleep(1000 * 10);
            user.setEnabled(1);
            VPNConfigUtil.configUser(user, StringContext.user_manager_path, nets);
        } catch (Exception e) {
            logger.info("保持拒绝用户连接成功" + user.getCn());
        }
    }
}
