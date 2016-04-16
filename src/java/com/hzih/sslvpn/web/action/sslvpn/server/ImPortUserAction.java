package com.hzih.sslvpn.web.action.sslvpn.server;

import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.entity.X509User;
import com.hzih.sslvpn.entity.mapper.X509UserAttributeMapper;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.sslvpn.ldap.*;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImPortUserAction extends ActionSupport {

    private Logger log = Logger.getLogger(ImPortUserAction.class);

    private UserDao userDao;


    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String ImportUser()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String json = null;
        int i=0;
        DirContext ctx = new LdapUtils().getCtx();
        if (ctx != null) {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration en = null;
            try {
                en = ctx.search(LdapXMLUtils.getValue(LdapXMLUtils.base), "(objectClass=" + X509User.getObjAttr() + ")", constraints);
            } catch (NamingException e) {
                log.error(e.getMessage());
            }
            X509UserAttributeMapper mapper = new X509UserAttributeMapper();
            while (en != null && en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    i++;
                    if(i%50==0){
                        try {
                            userDao.flushSession();
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                    if (si != null) {
                        User x509User = null;
                        try {
                            x509User = mapper.mapFromAttributes(si);
                        } catch (NamingException e) {
                            log.error(e.getMessage());
                        }
                        if (x509User != null) {
                            User u = null;
                            try {
                                u = userDao.findByCommonName(x509User.getCn());
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                           boolean flag = false;
                           try{
                            if (u != null) {
                                i++;
                                u.setCn(x509User.getCn());
                                u.setId_card(x509User.getId_card());
                                u.setPhone(x509User.getPhone());
                                u.setAddress(x509User.getAddress());
                                u.setEmail(x509User.getEmail());
                                u.setEmployeeCode(x509User.getEmployeeCode());
                                u.setOrgCode(x509User.getOrgCode());
                                u.setStatus(x509User.getStatus());
                                u.setSerial_number(x509User.getSerial_number());
                                u.setIssueCa(x509User.getIssueCa());
                                u.setProvince(x509User.getProvince());
                                u.setCity(x509User.getCity());
                                u.setOrganization(x509User.getOrganization());
                                u.setInstitutions(x509User.getInstitutions());
                                u.setDescription(x509User.getDescription());
                                flag = userDao.modify(u);
                            } else {
                                i++;
                                flag = userDao.add(x509User);
                            }
                           }catch (Exception e){
                               log.error(e.getMessage());
                               flag =false;
                           }
                            if(flag){
                                try {
                                    VPNConfigUtil.configUser(x509User, StringContext.user_manager_path, null);
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
            msg = "批量导入用户成功!";
        } else {
            msg = "LDAP服务器连通失败!";
        }
        LdapUtils.close(ctx);
        json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

}


