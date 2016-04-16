package com.hzih.sslvpn.utils;

import com.inetec.common.security.License;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LicenseUtils {
    private static final Logger logger = Logger.getLogger(LicenseUtils.class);
    /**
     *  权限控制
     * @param isExistLicense    是否存在 usb-key
     * @return
     */
	public List<String> getNeedsLicenses(boolean isExistLicense) {
        String qxManager = "TOP_QXGL:SECOND_YHGL:SECOND_JSGL:SECOND_AQCL:";     // 权限管理
        String wlManager = "TOP_WLGL:SECOND_JKGL:SECOND_LTCS:SECOND_LYGL:SECOND_PZGL:"; //网络管理
        String xtManager = "TOP_XTGL:SECOND_PTSM:SECOND_PTGL:SECOND_ZSGL:SECOND_RZXZ:SECOND_BBSJ:SECOND_CBBSJ:"; //系统管理
        String sjManager = "TOP_SJGL:SECOND_GLRZ:";         //审计管理
        String ztManager = "TOP_FWZT:SECOND_FWZT:SECOND_ZXYH:SECOND_YHRZ:";         //服务状态
        String pzManager = "TOP_FWPZ:SECOND_WLPZ:SECOND_JBPZ:SECOND_GJPZ:SECOND_ZSPZ:SECOND_ZWPZ:";  //服务配置
        String yhManager = "TOP_YHGL:SECOND_YHQX:SECOND_YHZQX:SECOND_YHJL:";   //用户管理
//        String xpManager = "TOP_XTPZ:SECOND_LDAP:SECOND_SYSLOG:SECOND_JCPZ:SECOND_STRATEGY:SECOND_CONTROL:";   //系统配置
        String xpManager = "TOP_XTPZ:SECOND_LDAP:SECOND_SYSLOG:SECOND_STRATEGY:";   //系统配置
        String dxManager = "TOP_DXGL:SECOND_DXLB:";   //吊销管理
        String jkManager = "TOP_JKGL:SECOND_ZJJK";   //监控管理
        String permission = qxManager + wlManager + xtManager + sjManager + ztManager + pzManager + yhManager+xpManager+dxManager+jkManager;
        if(isExistLicense){
	    	try{
                String license = License.getModules();//许可证允许的权限
                permission += license;
            } catch (Exception e) {
                logger.error("读取USB-KEY出错!");
            }
		}
		String[] permissions = permission.split(":");
		List<String> lps = new ArrayList<String>();
		for (int i = 0; i < permissions.length; i++) {
			lps.add(permissions[i]);
		}
		return lps;
	}
}
