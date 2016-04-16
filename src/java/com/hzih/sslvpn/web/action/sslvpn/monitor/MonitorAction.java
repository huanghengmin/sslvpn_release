package com.hzih.sslvpn.web.action.sslvpn.monitor;

import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-17
 * Time: 下午12:42
 * To change this template use File | Settings | File Templates.
 */
public class MonitorAction extends ActionSupport {

    public String getMonitorInfo()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int totalCount =0;
        StringBuilder stringBuilder = new StringBuilder();
        getResult(stringBuilder);
        totalCount = totalCount+1;
        StringBuilder json=new StringBuilder("{totalCount:"+totalCount+",root:[");
        json.append(stringBuilder.toString().substring(0,stringBuilder.toString().length()-1));
        json.append("]}");
        try {
            actionBase.actionEnd(response,json.toString(),result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getResult(StringBuilder stringBuilder) {
        stringBuilder.append("{");
        stringBuilder.append("monitor_ip:'"+ MonitorConfigUtil.getAttribute(MonitorConfigUtil.ip)+"',");
        stringBuilder.append("monitor_port:'"+ MonitorConfigUtil.getAttribute(MonitorConfigUtil.port)+"',");
        stringBuilder.append("jsonrpc2_port:'"+ MonitorConfigUtil.getAttribute(MonitorConfigUtil.jsonrpc2_port)+"'");
        stringBuilder.append("},");
    }

    
    public String saveMonitorInfo()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json ="{success:false}";
        String ip = request.getParameter("monitor_ip");
        String port = request.getParameter("monitor_port");
        String jsonrpc2_port = request.getParameter("jsonrpc2_port");
        MonitorConfigUtil.saveConfig(ip,port,jsonrpc2_port);
        json ="{success:true}";
        actionBase.actionEnd(response,json,result);
        return null;
    }
}
