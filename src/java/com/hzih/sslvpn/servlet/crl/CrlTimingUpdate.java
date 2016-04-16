package com.hzih.sslvpn.servlet.crl;

import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: hhm
 * Date: 12-9-17
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
public class CrlTimingUpdate{
    private Logger logger = Logger.getLogger(CrlTimingUpdate.class);

    public InputStream callDownCrl(String url){
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(5 * 1000);
        PostMethod post = new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5 * 1000);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(post);
            if (statusCode == 200) {
                InputStream data = post.getResponseBodyAsStream();
                return data;
            }
        } catch (Exception e) {
//                logger.error(e.getMessage(),e);
                logger.error("Http下载点下载CRL列表失败,时间:" + new Date() + "," + e.getMessage(),e);
        }
        return null;
    }



    public boolean down_crl(String url){
        InputStream in = callDownCrl(url);
        if (null != in) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(StringContext.crl_file);
                byte[] content = new byte[1024 * 1024];
                int length;
                while ((length = in.read(content, 0, content.length)) != -1) {
                    out.write(content, 0, length);
                    out.flush();
                }
                in.close();
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
               return false;
            }
        }
        return false;
    }

}
