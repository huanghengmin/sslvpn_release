package com.hzih.sslvpn.web.action.update;

import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 15-3-10.
 */
public class UpgradeVersionAction extends ActionSupport {

    public String check() throws Exception {
        /**
         * 返回三个客户端文件名和版本信息
         */
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String os = request.getParameter("os");
        String version = request.getParameter("version");
        String json = null;
        boolean flag = false;
        if (os != null && version != null) {
            if (os.equals("android")) {
                String android_version = StringContext.systemPath + "/client/android" + "/version.xml";
                File android_info = new File(android_version);
                if (android_info.exists()) {
                    Version v = VersionUtils.readInfo(android_info);
                    if (v != null) {
                        String vs = v.getVersion();
                        int diff = VersionUtils.compareVersion(vs, version);
                        if (diff != 0 && diff != -2 && diff != -1) {
                            //需要更新
                            String msg = "检测到新版本,是否更新?";
                            json = "{url:'UpgradeVersionAction_upgrade.action',name:'" + v.getName() + "',flag:true,msg:'" + msg + "'}";
                        } else {
                            json = "{url:'',name:'',flag:false,msg:''}";
                        }
                    }
                }
            } else if (os.equals("win32")) {
                String win32_version = StringContext.systemPath + "/client/windows/x86" + "/version.xml";
                File win32_info = new File(win32_version);
                if (win32_info.exists()) {
                    Version v = VersionUtils.readInfo(win32_info);
                    if (v != null) {
                        String vs = v.getVersion();
                        int diff = VersionUtils.compareVersion(vs, version);
                        if (diff != 0 && diff != -2 && diff != -1) {
                            //需要更新
                            String msg = "检测到新版本,是否更新?";
                            json = "{url:'UpgradeVersionAction_upgrade.action',name:'" + v.getName() + "',flag:true,msg:'" + msg + "'}";
                        } else {
                            json = "{url:'',name:'',flag:false,msg:''}";
                        }
                    }
                }
            } else if (os.equals("win64")) {
                String win64_version = StringContext.systemPath + "/client/windows/x64" + "/version.xml";
                File win64_info = new File(win64_version);
                if (win64_info.exists()) {
                    Version v = VersionUtils.readInfo(win64_info);
                    if (v != null) {
                        String vs = v.getVersion();
                        int diff = VersionUtils.compareVersion(vs, version);
                        if (diff != 0 && diff != -2 && diff != -1) {
                            //需要更新
                            String msg = "检测到新版本,是否更新?";
                            json = "{url:'UpgradeVersionAction_upgrade.action',name:'" + v.getName() + "',flag:true,msg:'" + msg + "'}";
                        } else {
                            json = "{url:'',name:'',flag:false,msg:''}";
                        }
                    }
                }
            }
        }
        try {
            actionBase.actionEnd(response, json, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String upgrade() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
//        String json = "{success:false}";
        String os = request.getParameter("os");
//        String name = request.getParameter("name");
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent, ";");
            st.nextToken();
            //得到用户的浏览器名  MSIE  Firefox
            String userBrowser = st.nextToken();
            String name = null;
            String path = null;
            if (os.equals("android")) {
                String android_version = StringContext.systemPath + "/client/android" + "/version.xml";
                File android_info = new File(android_version);
                if (android_info.exists()) {
                    Version v = VersionUtils.readInfo(android_info);
                    if (v != null) {
                        name = v.getName();
                    }
                }
                path = StringContext.systemPath + "/client/android" + "/" + name;
            } else if (os.equals("win32")) {
                String win32_version = StringContext.systemPath + "/client/windows/x86" + "/version.xml";
                File win32_info = new File(win32_version);
                if (win32_info.exists()) {
                    Version v = VersionUtils.readInfo(win32_info);
                    if (v != null) {
                        name = v.getName();
                    }
                }
                path = StringContext.systemPath + "/client/windows/x86" + "/" + name;
            } else if (os.equals("win64")) {
                String win64_version = StringContext.systemPath + "/client/windows/x64" + "/version.xml";
                File win64_info = new File(win64_version);
                if (win64_info.exists()) {
                    Version v = VersionUtils.readInfo(win64_info);
                    if (v != null) {
                        name = v.getName();
                    }
                }
                path = StringContext.systemPath + "/client/windows/x64" + "/" + name;
            }
            File source = new File(path);
            if (source.exists()) {
                FileUtil.downType(response, name, userBrowser);
                response = FileUtil.copy(source, response);
//                json = "{success:true}";
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
