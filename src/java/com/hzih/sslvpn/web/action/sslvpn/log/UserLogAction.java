package com.hzih.sslvpn.web.action.sslvpn.log;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.LogDao;
import com.hzih.sslvpn.domain.Log;
import com.hzih.sslvpn.utils.JsonUtil;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-23
 * Time: 下午12:31
 * To change this template use File | Settings | File Templates.
 */
public class UserLogAction extends ActionSupport {

    private LogDao logDao;

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

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }

    public String findLogs()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int pageIndex = start/limit+1;
        PageResult pageResult = logDao.listByPage(pageIndex,limit);
        if (pageResult != null) {
            List<Log> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<Log> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    Log log = raUserIterator.next();
               /*     long byte_received = log.getBytes_received();
                    int i = 0;
                    while(byte_received >= 1024){
                        byte_received=byte_received/1024;
                        i++;
                        if(i==4) break;
                    }
                    String unit = array("Bytes","KB","MB","GB","TB");*/

                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',username:'" + JsonUtil.checkNull(log.getCn()) +
                                "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()) +
                                "',subject_dn:'" + JsonUtil.checkNull(log.getSubject_dn()) +
                                "',start_time:'" + JsonUtil.checkNull(log.getStart_time()) +
                                "',end_time:'" + JsonUtil.checkNull(log.getEnd_time()).toString() +
                                "',trusted_ip:'" + JsonUtil.checkNull(log.getTrusted_ip()).toString() +
                                "',trusted_port:'" + JsonUtil.checkNull(log.getTrusted_port()).toString() +
                                "',protocol:'" + JsonUtil.checkNull(log.getProtocol()) +
                                "',remote_ip:'" + JsonUtil.checkNull(log.getRemote_ip()) +
                                "',remote_netmask:'" + JsonUtil.checkNull(log.getRemote_netmask()) +
                                "',bytes_received:'" + JsonUtil.checkNull(log.getBytes_received()) +
                                " Bytes',bytes_sent:'" + JsonUtil.checkNull(log.getBytes_sent()).toString() +
                                " Bytes',status:'" + JsonUtil.checkNull(log.getStatus()) + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',username:'" + JsonUtil.checkNull(log.getCn()) +
                                "',serial_number:'" + JsonUtil.checkNull(log.getSerial_number()) +
                                "',subject_dn:'" + JsonUtil.checkNull(log.getSubject_dn()) +
                                "',start_time:'" + JsonUtil.checkNull(log.getStart_time()) +
                                "',end_time:'" + JsonUtil.checkNull(log.getEnd_time()).toString() +
                                "',trusted_ip:'" + JsonUtil.checkNull(log.getTrusted_ip()).toString() +
                                "',trusted_port:'" + JsonUtil.checkNull(log.getTrusted_port()).toString() +
                                "',protocol:'" + JsonUtil.checkNull(log.getProtocol()) +
                                "',remote_ip:'" + JsonUtil.checkNull(log.getRemote_ip()) +
                                "',remote_netmask:'" + JsonUtil.checkNull(log.getRemote_netmask()) +
                                "',bytes_received:'" + JsonUtil.checkNull(log.getBytes_received()) +
                                " Bytes',bytes_sent:'" + JsonUtil.checkNull(log.getBytes_sent()).toString() +
                                " Bytes',status:'" + JsonUtil.checkNull(log.getStatus()) + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }


}
