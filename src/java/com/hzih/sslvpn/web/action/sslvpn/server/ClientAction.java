package com.hzih.sslvpn.web.action.sslvpn.server;

import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-9
 * Time: 下午12:35
 * To change this template use File | Settings | File Templates.
 */
public class ClientAction extends ActionSupport {

    /**
     *
     * @return
     * @throws Exception
     */
    public String downDh()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        File file = new File(StringContext.dh_file);
        if(file.exists())
            response = FileUtil.copy(file, response);
        else
            response.setStatus(404);
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String downStaticKey()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        File file = new File(StringContext.ta_key_file);
        if(file.exists())
            response = FileUtil.copy(file, response);
        else
            response.setStatus(404);
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String downCa()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        File file = new File(StringContext.ca_file);
        if(file.exists())
            response = FileUtil.copy(file, response);
        else
            response.setStatus(404);
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String downWindowsConfig()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        File file = new File(StringContext.windows_config_file);
        if(file.exists())
            response = FileUtil.copy(file, response);
        else
            response.setStatus(404);
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String downAndroidConfig()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        File file = new File(StringContext.android_config_file);
        if(file.exists())
            response = FileUtil.copy(file, response);
        else
            response.setStatus(404);
        return null;
    }
}
