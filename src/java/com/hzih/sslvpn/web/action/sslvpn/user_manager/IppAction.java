package com.hzih.sslvpn.web.action.sslvpn.user_manager;

import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-17
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
public class IppAction extends ActionSupport {
    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getAllIps() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int lines = 0;
        StringBuilder sb = new StringBuilder();
        File pool_file = new File(StringContext.pool_file);
        if (pool_file.exists()) {
            List<String> list = new ArrayList<>();
            FileReader in = new FileReader(pool_file);
            LineNumberReader reader = new LineNumberReader(in);
            String s = null;
            int pageNo = start / limit + 1;
            int startRow = (pageNo - 1) * limit + 1;
            int endRow = pageNo * limit;
            s = reader.readLine();
            while (s != null) {
                lines++;
                if (lines >= startRow && lines <= endRow) {
                    if (s != null && s.length() > 0)
                        list.add(s);
                }
                s = reader.readLine();
            }
            reader.close();
            in.close();
            if (list != null && list.size() > 0) {
                for (String line : list) {
                    if (line.contains(",")) {
                        String username = line.substring(0, line.indexOf(","));
                        String vpn_ip = line.substring(line.indexOf(",") + 1, line.length());
                        String vpn_ip_start = vpn_ip.substring(0, vpn_ip.lastIndexOf(".") + 1);
                        String vpn_ip_end = vpn_ip.substring(vpn_ip.lastIndexOf(".") + 1, vpn_ip.length());
                        int endTow = Integer.parseInt(vpn_ip_end) + 2;
                        String vpn_ip_real = vpn_ip_start + String.valueOf(endTow);
                        sb.append("{username:'" + username + "',vpn_ip:'" + vpn_ip_real + "'},");
                    }
                }
            }
//            InputStream fis;
//            BufferedReader br = null;
//            String  line = null;
//            fis = new FileInputStream(pool_file);
//            br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
           /* while ((line = br.readLine()) != null) {
                if(null!=line&&!line.equals("")){
                    if(line.contains(",")){
                        String username = line.substring(0,line.indexOf(","));
                        String vpn_ip = line.substring(line.indexOf(",")+1,line.length());
                        String vpn_ip_start = vpn_ip.substring(0,vpn_ip.lastIndexOf(".")+1);
                        String vpn_ip_end = vpn_ip.substring(vpn_ip.lastIndexOf(".")+1,vpn_ip.length());
                        int endTow =  Integer.parseInt(vpn_ip_end)+2;
                        String vpn_ip_real = vpn_ip_start+String.valueOf(endTow);
                        sb.append("{username:'"+username+"',vpn_ip:'"+vpn_ip_real+"'},");
                        count++;
                    }
                }
            }*/
//            br.close();
        }
        StringBuilder json = new StringBuilder();
        if (null != sb && !"".equals(sb) && sb.length() > 0) {
            json.append("{success:true,total:" + lines + ",rows:[");
            json.append(sb.substring(0, sb.length() - 1));
            json.append("]}");
        } else {
            json.append("{success:true,total:" + lines + ",rows:[");
            json.append("]}");
        }
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }

}
