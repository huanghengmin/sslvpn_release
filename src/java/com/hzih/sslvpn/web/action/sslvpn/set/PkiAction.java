package com.hzih.sslvpn.web.action.sslvpn.set;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.*;
import com.hzih.sslvpn.domain.CaManager;
import com.hzih.sslvpn.domain.KeyManager;
import com.hzih.sslvpn.domain.PkcsServer;
import com.hzih.sslvpn.domain.ServerManager;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.MergeFiles;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */
public class PkiAction extends ActionSupport {
    private ServerDao serverDao;
    private CaManagerDao caManagerDao;
    private ServerManagerDao serverManagerDao;
    private KeyManagerDao keyManagerDao;
    private FileSerialDao fileSerialDao;
    private PkcsServerDao pkcsServerDao;

    public PkcsServerDao getPkcsServerDao() {
        return pkcsServerDao;
    }

    public void setPkcsServerDao(PkcsServerDao pkcsServerDao) {
        this.pkcsServerDao = pkcsServerDao;
    }

    private File crtFile;
    private String crtFileFileName;
    // 使用列表保存多个上传文件的MIME类型
    private String crtFileContentType;
    
    private File serverPfxFile;
    private String serverPfxFileFileName;
    private String serverPfxContentType;

    public File getServerPfxFile() {
        return serverPfxFile;
    }

    public void setServerPfxFile(File serverPfxFile) {
        this.serverPfxFile = serverPfxFile;
    }

    public String getServerPfxFileFileName() {
        return serverPfxFileFileName;
    }

    public void setServerPfxFileFileName(String serverPfxFileFileName) {
        this.serverPfxFileFileName = serverPfxFileFileName;
    }

    public String getServerPfxContentType() {
        return serverPfxContentType;
    }

    public void setServerPfxContentType(String serverPfxContentType) {
        this.serverPfxContentType = serverPfxContentType;
    }

    private File serverFile;
    private String serverFileFileName;
    // 使用列表保存多个上传文件的MIME类型
    public File getCrtFile() {
        return crtFile;
    }public void setCrtFile(File crtFile) {
        this.crtFile = crtFile;
    }public String getCrtFileFileName() {
        return crtFileFileName;
    }public void setCrtFileFileName(String crtFileFileName) {
        this.crtFileFileName = crtFileFileName;
    }public String getCrtFileContentType() {
        return crtFileContentType;
    }public void setCrtFileContentType(String crtFileContentType) {
        this.crtFileContentType = crtFileContentType;
    }public File getServerFile() {
        return serverFile;
    }public void setServerFile(File serverFile) {
        this.serverFile = serverFile;
    }public String getServerFileFileName() {
        return serverFileFileName;
    }public void setServerFileFileName(String serverFileFileName) {
        this.serverFileFileName = serverFileFileName;
    }public String getServerFileContentType() {
        return serverFileContentType;
    }public void setServerFileContentType(String serverFileContentType) {
        this.serverFileContentType = serverFileContentType;
    }public File getKeyFile() {
        return keyFile;
    }public void setKeyFile(File keyFile) {
        this.keyFile = keyFile;
    }public String getKeyFileFileName() {
        return keyFileFileName;
    }public void setKeyFileFileName(String keyFileFileName) {
        this.keyFileFileName = keyFileFileName;
    }public String getKeyFileContentType() {
        return keyFileContentType;
    }public void setKeyFileContentType(String keyFileContentType) {
        this.keyFileContentType = keyFileContentType;
    }private String serverFileContentType;

    private File keyFile;
    private String keyFileFileName;
    // 使用列表保存多个上传文件的MIME类型
    private String keyFileContentType;

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

    public FileSerialDao getFileSerialDao() {
        return fileSerialDao;
    }

    public void setFileSerialDao(FileSerialDao fileSerialDao) {
        this.fileSerialDao = fileSerialDao;
    }

    public CaManagerDao getCaManagerDao() {
        return caManagerDao;
    }

    public void setCaManagerDao(CaManagerDao caManagerDao) {
        this.caManagerDao = caManagerDao;
    }

    public ServerManagerDao getServerManagerDao() {
        return serverManagerDao;
    }

    public void setServerManagerDao(ServerManagerDao serverManagerDao) {
        this.serverManagerDao = serverManagerDao;
    }

    public KeyManagerDao getKeyManagerDao() {
        return keyManagerDao;
    }

    public void setKeyManagerDao(KeyManagerDao keyManagerDao) {
        this.keyManagerDao = keyManagerDao;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(PkiAction.class);

    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String getAuthConfig()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int pageIndex = start/limit+1;
        PageResult pageResult =  caManagerDao.listByPage(pageIndex,limit);
        if(pageResult!=null){
            List<CaManager> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<CaManager> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    CaManager log = raUserIterator.next();
                    if(raUserIterator.hasNext()){
                        json += "{" +
                                "id:'"+log.getId()+
                                "',ca_file:'" + log.getCa_file() +
                                "',ca_name:'" + log.getCa_name() +
                                "',status:'" + log.getStatus() +
                                "',cn:'" + log.getCn()  +
                                "',not_before:'" + log.getNotBefore()+
                                "',not_after:'" + log.getNotAfter() +"'" +
                                "},";
                    }else {
                        json += "{" +
                                "id:'"+log.getId()+
                                "',ca_file:'" + log.getCa_file() +
                                "',ca_name:'" + log.getCa_name() +
                                "',status:'" + log.getStatus() +
                                "',cn:'" + log.getCn()  +
                                "',not_before:'" + log.getNotBefore()+
                                "',not_after:'" + log.getNotAfter() +"'" +

                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }
    
    public String updateAuthConfig()throws Exception{
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        String[] ids = request.getParameterValues("ids");
        caManagerDao.modify_check_no();
        if(null!=ids&&!"".equals(ids)&&ids.length>0){
            for (int i = 0;i<ids.length;i++) {
                String id = ids[i];
                if(!"".equals(id))
                caManagerDao.modify_check_on(Integer.parseInt(id));
            }
        }
        List<CaManager> checks = caManagerDao.findAllCheck();
        if(null!=checks&&checks.size()>0) {
            String[] ss = new String[checks.size()];
            for (int i=0;i<ss.length;i++){
                   ss[i]=checks.get(i).getCa_file();
            }
            MergeFiles.mergeFiles(StringContext.ca_file,ss);
        }
        json = "{success:true}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String findCaConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int pageIndex = start/limit+1;
        PageResult pageResult =  caManagerDao.listByPage(pageIndex,limit);
        if(pageResult!=null){
            List<CaManager> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<CaManager> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    CaManager log = raUserIterator.next();
                    if(raUserIterator.hasNext()){
                        json += "{" +
                                "id:'"+log.getId()+
                                "',ca_file:'" + log.getCa_file() +
                                "',ca_name:'" + log.getCa_name()  +
                                "',cn:'" + log.getCn()  +
                                "',not_before:'" + log.getNotBefore()  +
                                "',not_after:'" + log.getNotAfter() +"'" +
                                "},";
                    }else {
                        json += "{" +
                                "id:'"+log.getId()+
                                "',ca_file:'" + log.getCa_file() +
                                "',ca_name:'" + log.getCa_name()  +
                                "',cn:'" + log.getCn()  +
                                "',not_before:'" + log.getNotBefore()  +
                                "',not_after:'" + log.getNotAfter() +"'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String findServerConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int pageIndex = start/limit+1;
        PageResult pageResult =  serverManagerDao.listByPage(pageIndex,limit);
        if(pageResult!=null){
            List<ServerManager> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<ServerManager> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    ServerManager log = raUserIterator.next();
                    if(raUserIterator.hasNext()){
                        json += "{" +
                                "id:'"+log.getId()+
                                "',server_file:'" + log.getServer_file() +
                                "',server_name:'" + log.getServer_name() +
                                "',cn:'" + log.getCn()  +
                                "',not_before:'" + log.getNotBefore()  +
                                "',not_after:'" + log.getNotAfter() +"'" +
                                "},";
                    }else {
                        json += "{" +
                                "id:'"+log.getId()+
                                "',server_file:'" + log.getServer_file() +
                                "',server_name:'" + log.getServer_name()  +
                                "',cn:'" + log.getCn() +
                                "',not_before:'" + log.getNotBefore() +
                                "',not_after:'" + log.getNotAfter() +"'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String findServerKeyConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int pageIndex = start/limit+1;
        PageResult pageResult =  keyManagerDao.listByPage(pageIndex,limit);
        if(pageResult!=null){
            List<KeyManager> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<KeyManager> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    KeyManager log = raUserIterator.next();
                    if(raUserIterator.hasNext()){
                        json += "{" +
                                "id:'"+log.getId()+
                                "',key_file:'" + log.getKey_file() +
                                "',key_name:'" + log.getKey_name() +"'" +
                                "},";
                    }else {
                        json += "{" +
                                "id:'"+log.getId()+
                                "',key_file:'" + log.getKey_file() +
                                "',key_name:'" + log.getKey_name() +"'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public static boolean execute(String command){
        try
        {
            Process process = Runtime.getRuntime().exec (command);
            InputStreamReader ir=new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader (ir);
            String line;
            while ((line = input.readLine ()) != null){
                logger.info(line);
            }
            return true;
        }catch (java.io.IOException e){
            logger.info("IOException:"+e);
        }
        return false;
    }

    public static boolean split_server_pfx(String savePath,String pfxPwd,String uploadFileFileName){
        String pfx = savePath +"/"+uploadFileFileName;
        String pem = savePath +"/key/key.pem";
        String cert =  savePath +"/server/server.pem";
        String private_key =  savePath +"/key/key.pem";
        if(pfxPwd==null||pfxPwd.equals("")){
            boolean flag =execute("openssl pkcs12 -in "+pfx+" -passin pass: -nocerts -out "+pem+" -passout pass:123456");
            if(flag){
                boolean ex = execute("openssl pkcs12 -in " +pfx+" -passin pass: -clcerts -nokeys -out "+cert);
                if(ex){
                    execute("openssl rsa -in "+pem+" -passin pass:123456 -out "+private_key);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
        }
        }else {
            boolean flag =execute("openssl pkcs12 -in "+pfx+" -passin pass:"+pfxPwd +" -nocerts -out "+pem+" -passout pass:123456");
            if(flag){
                boolean ex = execute("openssl pkcs12 -in " +pfx+" -passin pass:"+pfxPwd+" -clcerts -nokeys -out "+cert);
                if(ex){
                    execute("openssl rsa -in "+pem+" -passin pass:123456 -out "+private_key);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        if(new File(pem).exists() && new File(cert).exists() && new File(private_key).exists()) {
            return true;
        }
        return false;
    }
    
    
    public String findPkcsConfig()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        PkcsServer server = pkcsServerDao.findPkcsServer();
        String  json= new String();
        if(null!=server)  {
              json = "{success:true,total:" + 1 + ",rows:[";
                        json += "{" +
                                "pkcs_file:'" + server.getPkcs_crt() +
                                "',pkcs_name:'" + server.getPkcs_name()  +
                                "',cn:'" + server.getCn()  +
                                "',not_before:'" + server.getNotBefore()  +
                                "',not_after:'" + server.getNotAfter() +"'" +
                                "}";
                json += "]}";
        }else {
             json= "{success:true,total:" + 0 + ",rows:[]}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String update_server_cert()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String pwd = request.getParameter("pwd");
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新VPN网关证书失败'}";
        try{
            boolean server = FileUtil.saveUploadFile(serverPfxFile,StringContext.server_sslPath +"/"+serverPfxFileFileName);
            if(server){
                boolean flag =split_server_pfx( StringContext.server_sslPath,pwd,serverPfxFileFileName);

                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
                CertificateFactory  certificatefactory = CertificateFactory.getInstance("X.509");
                FileInputStream f = new FileInputStream(StringContext.server_sslPath+"/server/server.pem");
                X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(f);
                String  subject  = cert.getSubjectDN().getName();

                Date notBefore=cert.getNotBefore();//得到开始有效日期
                Date notAfter = cert.getNotAfter();  //得到截止日期
                String s_notBefore="";
                String s_notAfter="";
                if(null!=notBefore){
                    s_notBefore = format.format(notBefore);
                }

                if(null!=notBefore){
                    s_notAfter = format.format(notAfter);
                }
//                    String serialNumber=cert.getSerialNumber().toString(16).toUpperCase();//得到序列号   16进制
//                    int version =cert.getVersion();
//                    String cn = cert.getIssuerDN().toString();
                PkcsServer pkcsServer = new PkcsServer();
                pkcsServer.setId(1);
                pkcsServer.setPkcs_crt(StringContext.server_sslPath+"/server/server.pem");
                pkcsServer.setPkcs_name(serverPfxFileFileName);
                pkcsServer.setCn(getSubject_Cn(subject));
                pkcsServer.setNotAfter(s_notAfter);
                pkcsServer.setNotBefore(s_notBefore);

                pkcsServerDao.updatePkcsServer(pkcsServer);
                if(flag)
                json = "{success:true,msg:'更新VPN网关证书成功'}";
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    
    public String upload_ca()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存CA证书失败'}";
        int ca_serial =  fileSerialDao.get_serial("ca_serial");
        String dir = StringContext.systemPath+"/ssl/ca/";
        File directory = new File(dir);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        try{
            boolean crtFlag = FileUtil.saveUploadFile(crtFile, dir+ca_serial+".pem");
            if(crtFlag){

//                byte[] bytes = ByteFileUtils.File2byte(crtFile);

//                byte[] b64certreq = Base64.encode(bytes);

                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
                CertificateFactory  certificatefactory = CertificateFactory.getInstance("X.509");
                FileInputStream server = new FileInputStream(crtFile);
                X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(server);
                String  subject  = cert.getSubjectDN().getName();

                Date notBefore=cert.getNotBefore();//得到开始有效日期
                Date notAfter = cert.getNotAfter();  //得到截止日期
                String s_notBefore="";
                String s_notAfter="";
                if(null!=notBefore){
                    s_notBefore = format.format(notBefore);
                }

                if(null!=notBefore){
                    s_notAfter = format.format(notAfter);
                }
//                    String serialNumber=cert.getSerialNumber().toString(16).toUpperCase();//得到序列号   16进制
//                    int version =cert.getVersion();
//                    String cn = cert.getIssuerDN().toString();
                CaManager caManager = new CaManager();
                caManager.setCa_file(dir+ca_serial+".pem");
                caManager.setCa_name(crtFileFileName);
                caManager.setCn(getSubject_Cn(subject));
                caManager.setNotAfter(s_notAfter);
                caManager.setNotBefore(s_notBefore);
                caManagerDao.add(caManager);
                fileSerialDao.update_serial("ca_serial",ca_serial+1);
                json = "{success:true,msg:'保存CA证书成功'}";
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
    
    public String upload_server()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存网关证书失败'}";
        int server_serial =  fileSerialDao.get_serial("server_serial");
        String dir = StringContext.systemPath+"/ssl/server/";
        File directory = new File(dir);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        try{
            FileUtil.saveUploadFile(serverFile, dir+server_serial+".pem");
            boolean crtFlag = FileUtil.saveUploadFile(serverFile,StringContext.server_file);
            if(crtFlag){

                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
                CertificateFactory  certificatefactory = CertificateFactory.getInstance("X.509");
                FileInputStream server = new FileInputStream(serverFile);
                X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(server);
                String  subject  = cert.getSubjectDN().getName();

                Date notBefore=cert.getNotBefore();//得到开始有效日期
                Date notAfter = cert.getNotAfter();  //得到截止日期
                String s_notBefore="";
                String s_notAfter="";
                if(null!=notBefore){
                    s_notBefore = format.format(notBefore);
                }

                if(null!=notBefore){
                    s_notAfter = format.format(notAfter);
                }
//                    String serialNumber=cert.getSerialNumber().toString(16).toUpperCase();//得到序列号   16进制
//                    int version =cert.getVersion();
//                    String cn = cert.getIssuerDN().toString();
                ServerManager serverManager = new ServerManager();
                serverManager.setServer_file(dir+server_serial+".pem");
                serverManager.setServer_name(serverFileFileName);
                serverManager.setCn(getSubject_Cn(subject));
                serverManager.setNotAfter(s_notAfter);
                serverManager.setNotBefore(s_notBefore);
                serverManagerDao.add(serverManager);
                fileSerialDao.update_serial("server_serial",server_serial+1);
                json = "{success:true,msg:'保存网关证书成功'}";
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update_server()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新网关证书失败'}";
        String id = request.getParameter("id");
        if(null!=id&&!id.equals("")){
            ServerManager serverManager = serverManagerDao.findById(Integer.parseInt(id));
            try{
                FileUtil.saveUploadFile(serverFile, serverManager.getServer_file());
                boolean crtFlag = FileUtil.saveUploadFile(serverFile,StringContext.server_file);
                if(crtFlag){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
                    CertificateFactory  certificatefactory = CertificateFactory.getInstance("X.509");
                    FileInputStream server = new FileInputStream(serverFile);
                    X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(server);
                    String  subject  = cert.getSubjectDN().getName();

                    Date notBefore=cert.getNotBefore();//得到开始有效日期
                    Date notAfter = cert.getNotAfter();  //得到截止日期
                    String s_notBefore="";
                    String s_notAfter="";
                    if(null!=notBefore){
                        s_notBefore = format.format(notBefore);
                    }

                    if(null!=notBefore){
                        s_notAfter = format.format(notAfter);
                    }
//                    String serialNumber=cert.getSerialNumber().toString(16).toUpperCase();//得到序列号   16进制
//                    int version =cert.getVersion();
//                    String cn = cert.getIssuerDN().toString();
                    serverManager.setServer_name(serverFileFileName);
                    serverManager.setCn(getSubject_Cn(subject));
                    serverManager.setNotAfter(s_notAfter);
                    serverManager.setNotBefore(s_notBefore);

                    serverManagerDao.update(serverManager);
                    json = "{success:true,msg:'更新网关证书成功'}";
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
    
    
    public String getSubject_Cn(String subject){
        String[] ss =null;
        if(subject.contains(","))
            ss = subject.split(",");
        if(null!=ss){
            for (int i=0;i<ss.length;i++){
                if(ss[i].contains("CN=")){
                   return ss[i].substring(ss[i].indexOf("=")+1,ss[i].length());
                }
            }
        }
        return "";
    }

    public String update_key()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新私钥失败'}";
        String id = request.getParameter("id");
        if(null!=id&&!id.equals("")){
            KeyManager keyManager = keyManagerDao.findById(Integer.parseInt(id));
            try{
                FileUtil.saveUploadFile(keyFile, keyManager.getKey_file());
                boolean crtFlag = FileUtil.saveUploadFile(keyFile,StringContext.key_file);
                if(crtFlag){
                    keyManager.setKey_name(keyFileFileName);
                    keyManagerDao.update(keyManager);
                    json = "{success:true,msg:'更新私钥成功'}";
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String upload_key()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存私钥失败'}";
        int key_serial =  fileSerialDao.get_serial("key_serial");
        String dir = StringContext.systemPath+"/ssl/key/";
        File directory = new File(dir);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        try{
            FileUtil.saveUploadFile(keyFile, dir+key_serial+".key");
            boolean crtFlag = FileUtil.saveUploadFile(keyFile,StringContext.key_file);
            if(crtFlag){
                keyManagerDao.add(new KeyManager(keyFileFileName,dir+key_serial+".key"));
                fileSerialDao.update_serial("key_serial",key_serial+1);
                json = "{success:true,msg:'保存私钥成功'}";
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
    
    public String remover_ca()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        try{
            String id =  request.getParameter("id");
            CaManager caManager = caManagerDao.findById(Integer.parseInt(id));

           File ca_file = new File(caManager.getCa_file());
            if(ca_file.exists()){
                ca_file.delete();
            }
            caManagerDao.delete(caManager);
            json = "{success:true,msg:'删除成功'}";
        }catch (Exception e){
            e.printStackTrace();
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remover_server()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        try{
            String id =  request.getParameter("id");
            ServerManager serverManager = serverManagerDao.findById(Integer.parseInt(id));

            File server_file = new File(serverManager.getServer_file());
            if(server_file.exists()){
                server_file.delete();
            }
            serverManagerDao.delete(serverManager);
            json = "{success:true,msg:'删除成功'}";
        }catch (Exception e){
            e.printStackTrace();
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remover_key()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        try{
            String id =  request.getParameter("id");
            KeyManager keyManager = keyManagerDao.findById(Integer.parseInt(id));

            File key_file = new File(keyManager.getKey_file());
            if(key_file.exists()){
                key_file.delete();
            }
            keyManagerDao.delete(keyManager);
            json = "{success:true,msg:'删除成功'}";
        }catch (Exception e){
            e.printStackTrace();
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String downloadCa() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        String id = request.getParameter("id");
        CaManager caManager =caManagerDao.findById(Integer.parseInt(id));

        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent,";");
        st.nextToken();
        //得到用户的浏览器名  MSIE  Firefox
        String userBrowser = st.nextToken();
        File file = new File(caManager.getCa_file());
        FileUtil.downType(response, caManager.getCa_name(),userBrowser);
        response = FileUtil.copy(file, response);
        json = "{success:true}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String downloadServer() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        String id = request.getParameter("id");
        ServerManager serverManager =serverManagerDao.findById(Integer.parseInt(id));

        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent,";");
        st.nextToken();
        //得到用户的浏览器名  MSIE  Firefox
        String userBrowser = st.nextToken();
        File file = new File(serverManager.getServer_file());
        FileUtil.downType(response, serverManager.getServer_name(),userBrowser);
        response = FileUtil.copy(file, response);
        json = "{success:true}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String downloadKey() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "{success:false}";
        String id = request.getParameter("id");
        KeyManager keyManager =keyManagerDao.findById(Integer.parseInt(id));

        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent,";");
        st.nextToken();
        //得到用户的浏览器名  MSIE  Firefox
        String userBrowser = st.nextToken();
        File file = new File(keyManager.getKey_file());
        FileUtil.downType(response, keyManager.getKey_name(),userBrowser);
        response = FileUtil.copy(file, response);
        json = "{success:true}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String findServerCrt()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String server_file = request.getParameter("server_file");
        File file = new File(server_file);
        String  json= "{success:true,total:" + 1 + ",rows:[";
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
        if(file.exists()){
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            FileInputStream server = new FileInputStream(file);
            X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(server);
            String  subject  = cert.getSubjectDN().getName();
            Date notBefore=cert.getNotBefore();//得到开始有效日期
            Date notAfter = cert.getNotAfter();  //得到截止日期
            String s_notBefore="";
            String s_notAfter="";
            if(null!=notBefore){
                s_notBefore = format.format(notBefore);
            }

            if(null!=notBefore){
                s_notAfter = format.format(notAfter);
            }
            String serialNumber=cert.getSerialNumber().toString(16).toUpperCase();//得到序列号   16进制
            int version =cert.getVersion();
            String cn = cert.getIssuerDN().toString();
            json += "{" +
                    "name:'版本',"+
                    "content:' V"+version+"'"+
                    "},";
            json += "{" +
                    "name:'证书序列号'," +
                    "content:'"+serialNumber+"'"+
                    "},";
            json += "{" +
                    "name:'颁发者'," +
                    "content:'"+cn+"'"+
                    "},";
            json += "{" +
                    "name:'有效起始日期'," +
                    "content:'"+s_notBefore.toString()+"'"+
                    "},";
            json += "{" +
                    "name:'有效终止日期'," +
                    "content:'"+s_notAfter.toString()+"'"+
                    "},";
            json += "{" +
                    "name:'证书主题'," +
                    "content:'"+subject+"'"+
                    "}";
        }else {
            json += "{" +
                    "name:'版本',"+
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'证书序列号'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'颁发者'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'有效起始日期'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'有效终止日期'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'证书主题'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "}";
        }
        json += "]}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findTrustCrt()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String ca_file = request.getParameter("ca_file");
        File file = new File(ca_file);
        String  json= "{success:true,total:" + 1 + ",rows:[";
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM日dd日HH时mm分ss秒");
        if(file.exists()){
            CertificateFactory  certificatefactory = CertificateFactory.getInstance("X.509");
            FileInputStream server = new FileInputStream(file);
            X509Certificate cert = (X509Certificate)certificatefactory.generateCertificate(server);
            String  subject  = cert.getSubjectDN().getName();
            
            Date notBefore=cert.getNotBefore();//得到开始有效日期
            Date notAfter = cert.getNotAfter();  //得到截止日期
            String s_notBefore="";
            String s_notAfter="";
            if(null!=notBefore){
                s_notBefore = format.format(notBefore);
            }

            if(null!=notBefore){
                s_notAfter = format.format(notAfter);
            }
            String serialNumber=cert.getSerialNumber().toString(16).toUpperCase();//得到序列号   16进制
            int version =cert.getVersion();
            String cn = cert.getIssuerDN().toString();
            json += "{" +
                    "name:'版本',"+
                    "content:' V"+version+"'"+
                    "},";
            json += "{" +
                    "name:'证书序列号'," +
                    "content:'"+serialNumber+"'"+
                    "},";
            json += "{" +
                    "name:'颁发者'," +
                    "content:'"+cn+"'"+
                    "},";
            json += "{" +
                    "name:'有效起始日期'," +
                    "content:'"+s_notBefore.toString()+"'"+
                    "},";
            json += "{" +
                    "name:'有效终止日期'," +
                    "content:'"+s_notAfter.toString()+"'"+
                    "},";
            json += "{" +
                    "name:'证书主题'," +
                    "content:'"+subject+"'"+
                    "}";
        }else {
            json += "{" +
                    "name:'版本',"+
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'证书序列号'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'颁发者'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'有效起始日期'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'有效终止日期'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "},";
            json += "{" +
                    "name:'证书主题'," +
                    "content:'未发现证书,可能已被删除!'"+
                    "}";
        }
        json += "]}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findKey()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String key_file = request.getParameter("key_file");
        String  json= "{success:true,total:" + 1 + ",rows:[";
        String keyContent =  FileUtil.readFileByLines(key_file);
        json += "{" +
                "name:'私钥文件:',"+
                "content:'"+keyContent+"'"+
                "}";

        json += "]}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

   /* public String delete()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String id = request.getParameter("id");
        try{
            boolean flag = httpsDao.checkSiteBlock(id);
            if(flag){
                json= "{success:true,msg:'站点正在被引用!请删除引用后重试'}";
            }else {
                Site site1 = siteDao.findById(Integer.parseInt(id));
                if(site1!=null){
                    boolean del_flag = siteDao.delete(site1);
                    if(del_flag){
                        String dir = StringContext.systemPath+"/ssl/"+site1.getSite_name()+"/";
                        File directory = new File(dir);
                        if(directory.exists()) {
                            boolean delete_flag = FileUtil.delFolder(dir);
                            if(delete_flag){
                                json= "{success:true,msg:'删除站点成功!'}";
                                logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "权限控制","用户删除站点信息成功");
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            json = "{success:false,msg:'删除站点失败'}";
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "权限控制", "用户删除站点信息失败");
            logger.error(e.getMessage());
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }*/
}
