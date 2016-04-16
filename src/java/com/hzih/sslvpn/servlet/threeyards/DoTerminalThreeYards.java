package com.hzih.sslvpn.servlet.threeyards;

import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.strategy.StrategyXMLUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by hhm on 2014/12/16.
 */
public class DoTerminalThreeYards extends HttpServlet {
    private UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext servletContext = this.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        userDao = (UserDao) ctx.getBean("userDao");
    }


    private boolean checkCRL(String serial) {
        boolean flag = false;
        File file = new File(StringContext.crl_file);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509CRL aCrl = (X509CRL) cf.generateCRL(fis);
                Set tSet = aCrl.getRevokedCertificates();
                Iterator tIterator = tSet.iterator();
                while (tIterator.hasNext()) {
                    X509CRLEntry tEntry = (X509CRLEntry) tIterator.next();
                    String sn = tEntry.getSerialNumber().toString(16).toUpperCase();
                    if (sn.equalsIgnoreCase(serial)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return flag;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String serial = request.getParameter("serial");
        String terminalId = request.getParameter("terminalId");
        String simId = request.getParameter("simId");
        String json = null;
        String msg = null;
        String code = null;
        if (serial != null && !"".equals(serial) && terminalId != null && !"".equals(terminalId) && simId != null && !"".equals(simId)) {
            if (("1").equalsIgnoreCase(StrategyXMLUtils.getValue(StrategyXMLUtils.threeyards))) {
                try {
                    User user = userDao.findBySerialNumber(serial);
                    if (user != null) {
                        if (user.getTerminal_id() != null && !"".equals(user.getTerminal_id()) && user.getNet_id() != null && !"".equals(user.getNet_id())) {
                            if (!user.getTerminal_id().equalsIgnoreCase(terminalId) || !user.getNet_id().equalsIgnoreCase(simId)) {
                                msg = "客户端三码校验不匹配.请更换成为原有TF卡和SIM卡后尝试连接.";
                                code = ReturnCode.RETURN_CLIENT_MSG_NOT_MATCH;
                                json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                writer.write(json);
                                writer.flush();
                                writer.close();
                            } else {
                                if (user.getEnabled() == 1) {
                                    if(checkCRL(serial)){
                                        msg = "客户端证书已被吊销，无法拨号服务器.";
                                        code = ReturnCode.RETURN_CLIENT_REVOKED;
                                        json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                        writer.write(json);
                                        writer.flush();
                                        writer.close();
                                    }else {
                                        msg = "客户端三码校验匹配成功.";
                                        code = ReturnCode.RETURN_CLIENT_MSG_MATCH_SUCCESS;
                                        json = "{success:true,msg:'" + msg + "',code:'"+code+"'}";
                                        writer.write(json);
                                        writer.flush();
                                        writer.close();
                                    }
                                } else {
                                    msg = "用户已被禁止拨号.";
                                    code = ReturnCode.RETURN_CLIENT_DISABLE;
                                    json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                    writer.write(json);
                                    writer.flush();
                                    writer.close();
                                }
                            }
                        } else {
                            if (user.getEnabled() == 1) {
                                if(checkCRL(serial)){
                                    msg = "客户端证书已被吊销，无法拨号服务器.";
                                    code = ReturnCode.RETURN_CLIENT_REVOKED;
                                    json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                    writer.write(json);
                                    writer.flush();
                                    writer.close();
                                }else {
                                    User simUser = userDao.findSimId(simId);
                                    if(simUser!=null){
                                        msg = "客户端三码校验不匹配.请更换成为原有TF卡和SIM卡后尝试连接.";
                                        code = ReturnCode.RETURN_CLIENT_MSG_NOT_MATCH;
                                        json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                        writer.write(json);
                                        writer.flush();
                                        writer.close();
                                    }

                                    User terminalUser = userDao.findTerminalId(terminalId);
                                    if(terminalUser!=null){
                                        msg = "客户端三码校验不匹配.请更换成为原有TF卡和SIM卡后尝试连接.";
                                        code = ReturnCode.RETURN_CLIENT_MSG_NOT_MATCH;
                                        json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                        writer.write(json);
                                        writer.flush();
                                        writer.close();
                                    }
                                    if(simUser==null&&terminalUser==null) {
                                        user.setTerminal_id(terminalId);
                                        user.setNet_id(simId);
                                        userDao.modify(user);
                                        msg = "客户端三码信息保存成功.";
                                        code = ReturnCode.RETURN_SERVER_SAVE_CLIENT_MSG_SUCCESS;
                                        json = "{success:true,add:true,msg:'" + msg + "',code:'" + code + "'}";
                                        writer.write(json);
                                        writer.flush();
                                        writer.close();
                                    }
                                }
                            } else {
                                msg = "用户已被禁止拨号.";
                                code = ReturnCode.RETURN_CLIENT_DISABLE;
                                json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                writer.write(json);
                                writer.flush();
                                writer.close();
                            }
                        }
                    } else {
                        msg = "服务器未找到对应用户.";
                        code = ReturnCode.RETURN_SERVER_NOT_FOUND_USER;
                        json = "{success:true,msg:'" + msg + "',code:'"+code+"'}";
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = "服务器未找到对应用户.";
                    code = ReturnCode.RETURN_SERVER_NOT_FOUND_USER;
                    json = "{success:true,msg:'" + msg + "',code:'"+code+"'}";
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            } else {
                try {
                    User user = userDao.findBySerialNumber(serial);
                    if (user != null) {
                        if (user.getEnabled() == 1) {
                            if(checkCRL(serial)){
                                msg = "客户端证书已被吊销，无法拨号服务器.";
                                code = ReturnCode.RETURN_CLIENT_REVOKED;
                                json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                                writer.write(json);
                                writer.flush();
                                writer.close();
                            }else {
                                msg = "服务器未启用三码合一校验.";
                                code = ReturnCode.RETURN_SERVER_DISABLE_CLIENT_MSG_MATCH;
                                json = "{success:true,msg:'" + msg + "',code:'"+code+"'}";
                                writer.write(json);
                                writer.flush();
                                writer.close();
                            }
                        } else {
                            msg = "用户已被禁止拨号";
                            code = ReturnCode.RETURN_CLIENT_DISABLE;
                            json = "{success:false,msg:'" + msg + "',code:'"+code+"'}";
                            writer.write(json);
                            writer.flush();
                            writer.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = "服务器未找到对应用户.";
                    code = ReturnCode.RETURN_SERVER_NOT_FOUND_USER;
                    json = "{success:true,msg:'" + msg + "',code:'"+code+"'}";
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }
}
