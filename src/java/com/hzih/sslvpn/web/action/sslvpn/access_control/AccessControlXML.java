package com.hzih.sslvpn.web.action.sslvpn.access_control;

import com.hzih.sslvpn.utils.StringContext;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 下午7:59
 * To change this template use File | Settings | File Templates.
 */
public class AccessControlXML {
    private static Logger logger=Logger.getLogger(AccessControlXML.class);
    public  static  final String control = "control";
    public  static  final String status = "status";
    public  static  final String control_url = "control_url";
    public  static  final String proxy_port = "proxy_port";
    public  static  final String bs_proxy_ip = "bs_proxy_ip";
    public  static  final String bs_proxy_port = "bs_proxy_port";
    private static  final String path = StringContext.systemPath+"/config/control_config.xml";

    public static String getAttribute(String attributeName){
        SAXReader saxReader = new SAXReader();
        Document doc=null;
        try {
            doc =saxReader.read(new File(path));
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        }
        Element root = doc.getRootElement();
        String result = root.attributeValue(attributeName);
        return result;
    }

    public static boolean saveConfig(String status,String control_url,String proxy_port,String bs_proxy_ip,String bs_proxy_port){
        boolean flag = false;
        Document doc= DocumentHelper.createDocument();
        Element root=doc.addElement(AccessControlXML.control);
        root.addAttribute(AccessControlXML.status,status);
        root.addAttribute(AccessControlXML.control_url,control_url);
        root.addAttribute(AccessControlXML.proxy_port, proxy_port);
        root.addAttribute(AccessControlXML.bs_proxy_ip,bs_proxy_ip);
        root.addAttribute(AccessControlXML.bs_proxy_port,bs_proxy_port);
        OutputFormat outputFormat=new OutputFormat("",true);
        try {
            XMLWriter xmlWriter=new XMLWriter(new FileOutputStream(new File(path)),outputFormat);
            try {
                xmlWriter.write(doc);
                flag = true;
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        }
        return flag;
    }
}
