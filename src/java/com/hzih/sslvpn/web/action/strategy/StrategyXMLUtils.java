package com.hzih.sslvpn.web.action.strategy;

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

public class StrategyXMLUtils {

    private static Logger logger = Logger.getLogger(StrategyXMLUtils.class);
    public static final String root = "strategy";
    public static final String gps = "gps";
    public static final String gps_interval = "gps_interval";
    public static final String view = "view";
    public static final String view_interval = "view_interval";
    public static final String terminal = "terminal";
    public static final String terminal_interval = "terminal_interval";
    public static final String strategy_interval = "strategy_interval";
    public static final String threeyards = "threeyards";
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
            doc = saxReader.read(new File(StringContext.strategy_xml));
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
     *
     * @param gps
     * @param gps_interval
     * @param view
     * @param view_interval
     * @return
     */
    public static boolean save(String gps, String gps_interval,
                               String view, String view_interval,
                               String terminal,String terminal_interval,String threeyards,String strategy_interval) {
        boolean flag = false;
        Document doc = DocumentHelper.createDocument();

        Element root = doc.addElement(StrategyXMLUtils.root);

        Element gps_el = root.addElement(StrategyXMLUtils.gps);
        gps_el.addText(gps);

        Element gps_interval_el = root.addElement(StrategyXMLUtils.gps_interval);
        gps_interval_el.addText(gps_interval);

        Element view_el = root.addElement(StrategyXMLUtils.view);
        view_el.addText(view);

        Element view_interval_el = root.addElement(StrategyXMLUtils.view_interval);
        view_interval_el.addText(view_interval);


        Element terminal_el = root.addElement(StrategyXMLUtils.terminal);
        terminal_el.addText(terminal);

        Element terminal_interval_el = root.addElement(StrategyXMLUtils.terminal_interval);
        terminal_interval_el.addText(terminal_interval);

        Element threeyards_el = root.addElement(StrategyXMLUtils.threeyards);
        threeyards_el.addText(threeyards);

        Element strategy_interval_el = root.addElement(StrategyXMLUtils.strategy_interval);
        strategy_interval_el.addText(strategy_interval);

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        format.setIndent(true);
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.strategy_xml)), format);
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


