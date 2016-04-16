package com.hzih.sslvpn.servlet.clearlog;

import com.hzih.sslvpn.dao.LogDao;
import org.apache.log4j.Logger;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-6
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public class ClearTask extends TimerTask {
    private Logger logger = Logger.getLogger(ClearTask.class);

    private LogDao logDao;

    public ClearTask(LogDao logDao) {
        this.logDao = logDao;
    }

    @Override
    public void run() {
        logDao.clearLogs();
        logger.info("清理用户上下线日志,时间:" + new Date());
    }
}
