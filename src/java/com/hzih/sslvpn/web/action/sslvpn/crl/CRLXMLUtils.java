package com.hzih.sslvpn.web.action.sslvpn.crl;

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

public class CRLXMLUtils {
    private static Logger logger = Logger.getLogger(CRLXMLUtils.class);
    public static final String crl = "crl";
    public static final String url = "url";
    public static final String second = "second";
    public static final String hour = "hour";
    public static final String day = "day";
    public static final String charset = "utf-8";


    /**
     * @param name
     * @return
     */
    public static String getValue(String name) {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        String result = null;
        try {
            doc = saxReader.read(new File(StringContext.crl_xml));
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        }
        if(doc!=null){
            Element ldap = doc.getRootElement();
            Element el = ldap.element(name);
            result = el.getText();
        }
        return result;
    }
    /**
     * @param url
     * @param second
     * @param hour
     * @param day
     */
    public static boolean save(String url, String second, String hour, String day) {
        boolean flag = false;
        Document doc = DocumentHelper.createDocument();
        Element crl = doc.addElement(CRLXMLUtils.crl);
        Element url_el = crl.addElement(CRLXMLUtils.url);
        url_el.addText(url);
        Element second_el = crl.addElement(CRLXMLUtils.second);
        second_el.addText(second);
        Element hour_el = crl.addElement(CRLXMLUtils.hour);
        hour_el.addText(hour);
        Element day_el = crl.addElement(CRLXMLUtils.day);
        day_el.addText(day);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        format.setIndent(true);
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.crl_xml)), format);
            try {
                xmlWriter.write(doc);
                flag = true;
            } catch (IOException e) {
                logger.info(e.getMessage());
            } finally {
                try {
                    xmlWriter.flush();
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        }
        return flag;
    }
}


