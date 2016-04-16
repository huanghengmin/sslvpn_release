package com.hzih.sslvpn.web.action.sslvpn.server;

import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.entity.X509User;
import com.hzih.sslvpn.entity.mapper.X509UserAttributeMapper;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.action.sslvpn.ldap.LdapUtils;
import com.hzih.sslvpn.web.action.sslvpn.ldap.LdapXMLUtils;
import org.apache.log4j.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Date;
import java.util.TimerTask;

public class MyUserTask extends TimerTask {

    private Logger log = Logger.getLogger(MyUserTask.class);

    private UserDao userDao;

    public MyUserTask(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void run() {
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
            log.info("自动更新用户信息成功,时间:"+new Date());
        } else {
            log.info("自动更新用户信息失败,时间:"+new Date());
        }
        LdapUtils.close(ctx);
    }
}