package com.hzih.sslvpn.web.servlet;

import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
//import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.update.Version;
import com.hzih.sslvpn.web.action.update.VersionUtils;
import org.apache.log4j.Logger;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-3
 * Time: 下午5:43
 * To change this template use File | Settings | File Templates.
 */
public class DownLoadAndroid extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(DownLoadAndroid.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        ActionBase actionBase = new ActionBase();
//        String result = actionBase.actionBegin(request);
        response.setCharacterEncoding("utf-8");
        String msg = null;
        String path = null;
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent, ";");
            st.nextToken();
            String userBrowser = st.nextToken();
            Version av = null;
            String android_version = StringContext.systemPath + "/client/android" + "/version.xml";
            File android_info = new File(android_version);
            if (android_info.exists()) {
                av = VersionUtils.readInfo(android_info);
                String name = av.getName();
                path = StringContext.systemPath + "/client/android" + "/" + name;
            }
//            path = StringContext.systemPath + "/client/android/SSLVPN.apk";
            File source = new File(path);
            String name = source.getName();
            FileUtil.downType(response, name, userBrowser);
            response = FileUtil.copy(source, response);
            msg = "下载Android SSLVPN 客户端成功";
//            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户日志审计","用户日志下载成功 ");
        } catch (Exception e) {
            e.printStackTrace();
            msg = "下载Android SSLVPN 客户端失败";
            logger.error("下载Android SSLVPN 客户端失败", e);
//            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "用户日志审计","用户日志下载失败 ");
        }
        String json = "{success:true,msg:'" + msg + "'}";
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
//        actionBase.actionEnd(response, json, result);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
