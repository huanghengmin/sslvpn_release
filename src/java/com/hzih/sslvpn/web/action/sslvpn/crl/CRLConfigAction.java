package com.hzih.sslvpn.web.action.sslvpn.crl;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.servlet.crl.CrlTask;
import com.hzih.sslvpn.syslog.SysLogSend;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 12-11-8
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public class CRLConfigAction extends ActionSupport {
    private Logger logger = Logger.getLogger(CRLConfigAction.class);

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    /**
     * 保存配置
     * @return
     * @throws java.io.IOException
     */
    public String save() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String url = request.getParameter("url");
        String second = request.getParameter("second");
        String hour = request.getParameter("hour");
        String day = request.getParameter("day");
        if(second==null||second.length()<=0){
            second = "0";
        }
        if(second==null||second.length()<=0){
            second = "0";
        }
        if(second==null||second.length()<=0){
            second = "0";
        }
        boolean flag = CRLXMLUtils.save(url,second,hour,day);
        if(flag){

            CrlTask crlTask =  SiteContextLoaderServlet.crlTask;
            if(crlTask!=null&&url!=null){
                crlTask.exit();
                crlTask =null;
                long interval = 0;
                if(second!=null&&Integer.parseInt(second)>0){
                    interval += Integer.parseInt(second)*1000*60;
                }
                if(hour!=null&&Integer.parseInt(hour)>0){
                    interval += Integer.parseInt(hour)*1000*60*60;
                }
                if(day!=null&&Integer.parseInt(day)>0){
                    interval += Integer.parseInt(day)*1000*60*60*60;
                }
                crlTask = new CrlTask(interval,url);
                crlTask.start();
            }
            msg = "CRL配置保存成功";
            json = "{success:true,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            SysLogSend.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "CRL配置", msg);

        }else {
            msg = "CRL配置保存失败";
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            SysLogSend.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "CRL配置", msg);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }


    /**
     * 查找
     * @return
     */
    public String find(){
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int totalCount =0;
        StringBuilder sb = new StringBuilder();
        jsonResult(sb);
        totalCount = totalCount+1;
        StringBuilder json=new StringBuilder("{totalCount:"+totalCount+",root:[");
        json.append(sb.toString());
        json.append("]}");
        try {
            actionBase.actionEnd(response,json.toString(),result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回JSON数据格式
     * @param sb
     */
    private void jsonResult(StringBuilder sb) {
        sb.append("{");
        sb.append("url:'"+ isNULL(CRLXMLUtils.getValue(CRLXMLUtils.url))+"',");
        sb.append("second:'"+ isNULL(CRLXMLUtils.getValue(CRLXMLUtils.second))+"',");
        sb.append("hour:'"+ isNULL(CRLXMLUtils.getValue(CRLXMLUtils.hour))+"',");
        sb.append("day:'"+ isNULL(CRLXMLUtils.getValue(CRLXMLUtils.day))+"'");
        sb.append("}");
    }

    public String isNULL(Object o){
       if(o==null){
           return "";
       } else {
           return o.toString();
       }
    }
}
