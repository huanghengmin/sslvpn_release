package com.hzih.sslvpn.web.action.update;

import com.hzih.sslvpn.utils.ExtractZip;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-25
 * Time: 下午12:22
 * To change this template use File | Settings | File Templates.
 */
public class ClientVersionAction extends ActionSupport {
    private File uploadFile;
    private static final int BUFFER_SIZE = 1 * 1024;
    private String fileContentType;
    private String uploadFileFileName;

    public String upload() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        if (!uploadFileFileName.endsWith("zip")) {
            String msg = "上传的文件格式不对";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
        }
        String os = request.getParameter("os");
        String toSrc = null;
        if (os.equals("android"))
            toSrc = StringContext.systemPath + "/client/android" + "/" + uploadFileFileName;
        else if (os.equals("win32")) {
            toSrc = StringContext.systemPath + "/client/windows/x86" + "/" + uploadFileFileName;
        } else if (os.equals("win64")) {
            toSrc = StringContext.systemPath + "/client/windows/x64" + "/" + uploadFileFileName;
        }
        File toFile = new File(toSrc);
        if (!toFile.exists()) {
            toFile.getParentFile().mkdirs();
        }
        try {
            writeFile(this.uploadFile, toFile);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "上传失败";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
        }
        ExtractZip extractZip = new ExtractZip();
        if (extractZip.Unzip(toSrc)) {
            String msg = "上传成功";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
            toFile.delete();
        } else {
            String msg = "上传失败";
            String json = "{success:true,msg:'" + msg + "'}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }

    public String find() throws Exception {
        /**
         * 返回三个客户端文件名和版本信息
         */
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int totalCount = 0;
        totalCount = totalCount + 3;
        StringBuilder json = new StringBuilder("{totalCount:" + totalCount + ",root:[");
        Version av = null;
        Version win32v = null;
        StringBuilder sb = new StringBuilder();
        String android_version = StringContext.systemPath +"/client/android" + "/version.xml";
        File android_info = new File(android_version);
        if (android_info.exists()) {
            av = VersionUtils.readInfo(android_info);
            if (av != null) {
                sb.append("{");
                sb.append("version:'" + av.getVersion() + "',");
                sb.append("name:'" + av.getName() + "',");
                sb.append("os:'android'");
                sb.append("}");
            }
        }

        String win32_version = StringContext.systemPath +"/client/windows/x86" + "/version.xml";
        File win32_info = new File(win32_version);
        if (win32_info.exists()) {
            win32v = VersionUtils.readInfo(win32_info);
            if (win32v != null) {
                if (av != null)
                    sb.append(",");
                sb.append("{");
                sb.append("version:'" + win32v.getVersion() + "',");
                sb.append("name:'" + win32v.getName() + "',");
                sb.append("os:'win32'");
                sb.append("}");
            }
        }

        String win64_version = StringContext.systemPath +"/client/windows/x64" + "/version.xml";
        File win64_info = new File(win64_version);
        if (win64_info.exists()) {
            Version v = VersionUtils.readInfo(win64_info);
            if (v != null) {
                if (win32v != null)
                    sb.append(",");
                sb.append("{");
                sb.append("version:'" + v.getVersion() + "',");
                sb.append("name:'" + v.getName() + "',");
                sb.append("os:'win64'");
                sb.append("}");
            }
        }
        json.append(sb.toString());
        json.append("]}");
        try {
            actionBase.actionEnd(response, json.toString(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String download() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String os = request.getParameter("os");
        String name = request.getParameter("name");
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent, ";");
            st.nextToken();
            //得到用户的浏览器名  MSIE  Firefox
            String userBrowser = st.nextToken();
            String path = null;
            if (os.equals("android")) {
                path = StringContext.systemPath +"/client/android" + "/" + name;
            } else if (os.equals("win32")) {
                path = StringContext.systemPath +"/client/windows/x86" + "/" + name;
            } else if (os.equals("win64")) {
                path = StringContext.systemPath +"/client/windows/x64" + "/" + name;
            }
            File source = new File(path);
            if (source.exists()) {
                FileUtil.downType(response, name, userBrowser);
                response = FileUtil.copy(source, response);
                json = "{success:true}";
            }
        } catch (Exception e) {
            return null;
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public File getUploadFile() {
        return this.uploadFile;
    }

    public void setUploadFile(File file) {
        this.uploadFile = file;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    private static void writeFile(File src, File dst) {
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                while (in.read(buffer) > 0) {
                    out.write(buffer);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
