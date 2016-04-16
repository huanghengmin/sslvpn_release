package com.hzih.sslvpn.web.action.sslvpn.monitor;

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

public class MonitorConfigUtil {
    private static Logger logger=Logger.getLogger(MonitorConfigUtil.class);
    public  static  final String root = "root";
    public  static  final String ip = "ip";
    public  static  final String port = "port";
    public  static  final String jsonrpc2_port = "jsonrpc2_port";
    private static  final String path = StringContext.systemPath+"/config/monitor_config.xml";

    public static String getAttribute(String attributeName){
        SAXReader saxReader = new SAXReader();
        Document doc=null;
        try {
            doc = saxReader.read(new File(path));
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        }
        Element root = doc.getRootElement();
        String result = root.attributeValue(attributeName);
        return result;
    }

    public static void saveConfig(String ip, String port, String jsonrpc2_port){
        Document doc=DocumentHelper.createDocument();
        Element root=doc.addElement(MonitorConfigUtil.root);
        root.addAttribute(MonitorConfigUtil.ip,ip);
        root.addAttribute(MonitorConfigUtil.port,port);
        root.addAttribute(MonitorConfigUtil.jsonrpc2_port,jsonrpc2_port);
        OutputFormat outputFormat=new OutputFormat("",true);
        try {
            XMLWriter xmlWriter=new XMLWriter(new FileOutputStream(new File(path)),outputFormat);
            try {
                xmlWriter.write(doc);
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        }
    }
}
