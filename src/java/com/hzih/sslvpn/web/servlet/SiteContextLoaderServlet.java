package com.hzih.sslvpn.web.servlet;
import com.hzih.sslvpn.myjfree.RunMonitorInfoList;
import com.hzih.sslvpn.myjfree.RunMonitorLiuliangBean2List;
import com.hzih.sslvpn.domain.SafePolicy;
import com.hzih.sslvpn.service.SafePolicyService;
import com.hzih.sslvpn.constant.AppConstant;
import com.hzih.sslvpn.constant.ServiceConstant;
import com.hzih.sslvpn.servlet.crl.CrlTask;
import com.hzih.sslvpn.syslog.SysLogSendService;
import com.hzih.sslvpn.tcp.ServiceUtils;
import com.hzih.sslvpn.tcp.TcpServer;
import com.hzih.sslvpn.web.SiteContext;
import com.hzih.sslvpn.web.action.sslvpn.crl.CRLXMLUtils;
import com.inetec.common.util.OSInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.*;
import java.io.IOException;
import java.net.InetSocketAddress;

public class SiteContextLoaderServlet extends DispatcherServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(SiteContextLoaderServlet.class);
    public static boolean isRunSysLogService = false;
    public static SysLogSendService sysLogSendService = new SysLogSendService();
    public static CrlTask crlTask = null;



    //启动syslog服务
    public void runSysLogSendService(){
        log.info("启动syslog日志!");
        if (SiteContextLoaderServlet.isRunSysLogService) {
            return;
        }else {
            sysLogSendService.init();
            Thread thread = new Thread(sysLogSendService);
            thread.start();
            SiteContextLoaderServlet.isRunSysLogService = true;
        }
    }


    public void startCrlTask(){
        if(crlTask==null){
            long interval = 0;
            String second = CRLXMLUtils.getValue(CRLXMLUtils.second);
            String hour = CRLXMLUtils.getValue(CRLXMLUtils.hour);
            String day = CRLXMLUtils.getValue(CRLXMLUtils.day);
            if(second!=null&&Integer.parseInt(second)>0){
               interval += Integer.parseInt(second)*1000*60;
            }
            if(hour!=null&&Integer.parseInt(hour)>0){
                interval += Integer.parseInt(hour)*1000*60*60;
            }
            if(day!=null&&Integer.parseInt(day)>0){
                interval += Integer.parseInt(day)*1000*60*60*60;
            }
            if(interval>0) {
                crlTask = new CrlTask(interval,CRLXMLUtils.getValue(CRLXMLUtils.crl));
                crlTask.start();
            }
        }

       /* if(crlTask!=null){
            crlTask.start();
        }*/
    }

	@Override
	public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SiteContext.getInstance().contextRealPath = config.getServletContext().getRealPath("/");
        servletContext.setAttribute("appConstant", new AppConstant());
        SafePolicyService service = (SafePolicyService)context.getBean(ServiceConstant.SAFEPOLICY_SERVICE);
        SafePolicy data = service.getData();
        SiteContext.getInstance().safePolicy = data;
        //启动syslog
//        runSysLogSendService();
        
        //启动jsonrpc service
//        JSONRPCService();

        InetSocketAddress localAddressServer = new InetSocketAddress("0.0.0.0",5000);
        TcpServer tcpServer = new TcpServer();
        tcpServer.init(localAddressServer);
        new Thread(tcpServer).start();

        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isLinux()) {
            new RunMonitorInfoList().start();
            new RunMonitorLiuliangBean2List().start();
        }

        //开启CRL更新线程
        startCrlTask();
    }

	@Override
	public ServletConfig getServletConfig() {
		// do nothing
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)throws ServletException, IOException {
		// do nothing
	}

	@Override
	public String getServletInfo() {
		// do nothing
		return null;
	}

	@Override
	public void destroy() {
        // do nothing
	}
}
