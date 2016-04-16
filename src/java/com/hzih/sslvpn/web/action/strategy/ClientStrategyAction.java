package com.hzih.sslvpn.web.action.strategy;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.syslog.SysLogSend;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by hhm on 2014/12/11.
 */
public class ClientStrategyAction extends ActionSupport {
    private Logger logger = Logger.getLogger(ClientStrategyAction.class);

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
        String gps = request.getParameter("gps");
        String gps_interval = request.getParameter("gps_interval");
        String view = request.getParameter("view");
        String view_interval = request.getParameter("view_interval");
        String terminal = request.getParameter("terminal");
        String terminal_interval = request.getParameter("terminal_interval");
        String threeyards = request.getParameter("threeyards");
        String strategy_interval = request.getParameter("strategy_interval");
        if(gps==null)
            gps="0";
        if(gps_interval==null)
            gps_interval = "0";
        if(view==null)
            view="0";
        if(view_interval==null)
            view_interval="0";
        if(terminal==null)
            terminal="0";
        if(terminal_interval==null)
            terminal_interval="0";
        if(threeyards==null)
            threeyards = "0";

        boolean flag = StrategyXMLUtils.save(gps,gps_interval,view,view_interval,terminal,terminal_interval,threeyards,strategy_interval);
        if(flag){
            msg = "客户端策略配置保存成功";
            json = "{success:true,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            SysLogSend.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "LDAP配置", msg);

        }else {
            msg = "LDAP配置保存失败";
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            SysLogSend.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "LDAP配置", msg);
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
        /**
         *
         */
        sb.append("gps:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.gps))+"',");
        sb.append("gps_interval:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.gps_interval))+"',");
        sb.append("terminal:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.terminal))+"',");
        sb.append("terminal_interval:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.terminal_interval))+"',");
        sb.append("threeyards:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.threeyards))+"',");
        sb.append("strategy_interval:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.strategy_interval))+"',");
        sb.append("view:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.view))+"',");
        sb.append("view_interval:'"+ isNULL(StrategyXMLUtils.getValue(StrategyXMLUtils.view_interval))+"'");
        sb.append("}");
    }

    public String isNULL(Object o){
        if(o==null){
            return "";
        } else {
            return o.toString();
        }
    }


    /**
     * 查找
     * @return
     */
    public String findStrategy()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        int totalCount =0;
        StringBuilder sb = new StringBuilder();
        jsonResult(sb);
        totalCount = totalCount+1;
        StringBuilder json=new StringBuilder("{totalCount:"+totalCount+",root:[");
        json.append(sb.toString());
        json.append("]}");
        writer.write(json.toString());
        writer.flush();
        writer.close();
        return null;
    }

}
