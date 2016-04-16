package com.hzih.sslvpn.web.action.sslvpn.crl;

import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.servlet.crl.CrlTimingUpdate;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.security.cert.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-6
 * Time: 下午1:33
 * To change this template use File | Settings | File Templates.
 */
public class RevokeAction extends ActionSupport {

    private File crlFile;
    private String crlFileFileName;
    // 使用列表保存多个上传文件的MIME类型
    private String crlFileContentType;

    public File getCrlFile() {
        return crlFile;
    }

    public void setCrlFile(File crlFile) {
        this.crlFile = crlFile;
    }

    public String getCrlFileFileName() {
        return crlFileFileName;
    }

    public void setCrlFileFileName(String crlFileFileName) {
        this.crlFileFileName = crlFileFileName;
    }

    public String getCrlFileContentType() {
        return crlFileContentType;
    }

    public void setCrlFileContentType(String crlFileContentType) {
        this.crlFileContentType = crlFileContentType;
    }

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String get_revoke() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        StringBuilder sb = new StringBuilder();
        File crl_file = new File(StringContext.crl_file);
        if (crl_file.exists() && crl_file.length() > 0) {
            FileInputStream fis = new FileInputStream(crl_file);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509CRL aCrl = (X509CRL) cf.generateCRL(fis);
            Set tSet = aCrl.getRevokedCertificates();
            Iterator tIterator = tSet.iterator();
            sb.append("{success:true,total:" + tSet.size() + ",rows:[");
            while (tIterator.hasNext()) {
                X509CRLEntry tEntry = (X509CRLEntry) tIterator.next();
                String sn = tEntry.getSerialNumber().toString(16).toUpperCase();
                String issName = aCrl.getIssuerDN().toString();
                String time = new SimpleDateFormat("yyyy年MM月dd日HH日mm分ss秒").format(tEntry.getRevocationDate());
                User user = userDao.findBySerialNumber(sn);
                if (null != user) {
                    sb.append("{");
                    sb.append("username:'" + user.getCn() + "'").append(",");
                    sb.append("serial:'" + sn + "'").append(",");
                    sb.append("id_card:'" + user.getId_card() + "'").append(",");
                    sb.append("revoke_time:'" + time + "'").append(",");
                    sb.append("iss_name:'" + issName + "'").append("");
                    sb.append("},");
                }
            }
            fis.close();
            String json = "";
            if (sb.toString().endsWith(",")) {
                json = sb.toString().substring(0, sb.length() - 1);
            }
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }

    public String update_crl() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        boolean crlFlag = FileUtil.saveUploadFile(crlFile, StringContext.crl_file);
        if (crlFlag) {
            msg = "更新CRL列表成功";
            json = "{success:true,msg:'"+msg+"'}";
        } else {
            msg = "更新CRL列表失败";
            json = "{success:false,msg:'"+msg+"'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    //http://CA:PORT/CRL_download.action
    public String down_crl() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String url = request.getParameter("url");
        if(url!=null){
            CrlTimingUpdate crlTimingUpdate = new CrlTimingUpdate();
            boolean flag = crlTimingUpdate.down_crl(url);
            if (flag) {
                msg = "下载CRL列表成功";
                json = "{success:true,msg:'"+msg+"'}";
            } else {
                msg = "下载CRL列表失败";
                json = "{success:false,msg:'"+msg+"'}";
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

}
