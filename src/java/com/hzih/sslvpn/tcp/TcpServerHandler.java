package com.hzih.sslvpn.tcp;

import com.hzih.sslvpn.entity.*;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.mina.MessageInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: sunny
 * Date: 14-7-25
 * Time: 上午10:26
 */
public class TcpServerHandler extends IoHandlerAdapter {
    final static Logger logger = Logger.getLogger(TcpServer.class);


    public TcpServerHandler() {

    }

    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if(message instanceof MessageInfo) {
            MessageInfo messageInfo = (MessageInfo) message;
            String charset = messageInfo.getCharset();
            String bodyStr = new String(messageInfo.getBody(),charset);
            logger.info(session.getRemoteAddress()+ " 发送了 "+bodyStr);
            String responseBody;
            if(bodyStr!=null ){
                    String body = AESDecoder(bodyStr);
                    String cmdType = body.substring(body.indexOf("<CmdType>")+9,body.indexOf("</CmdType>")).trim();
                    if(SipType.DownConfig.equalsIgnoreCase(cmdType)) {//vpn下载配置
                        DownRequest down = new DownRequest().xmlToBean(body.getBytes(charset));
                        DownConfigResponse response = new DownConfigResponse();
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        response.setDeviceId(serviceUtils.deviceId);
                        response.setCmdType(down.getCmdType());
                        response.setDeviceType(down.getDeviceType());
                        response.setTimeService(serviceUtils.timeServer);
                        response.setCa(serviceUtils.caServer);
                        response.setTerminalMonitor(serviceUtils.terminalMonitorServer);
                        response.setCenter(serviceUtils.centerServer);
                        response.setVpn(serviceUtils.vpnConnect);
                        File caFile = getFile(down, "ca");
                        File taFile = getFile(down, "ta");

                        response.setKeyFileName(new String(Base64.encodeBase64("ta.key".getBytes())));
                        response.setKeyFileSize(taFile.length());
                        response.setKeyFile(FileUtil.encodeBase64FileTOString(taFile.getPath()));

                        response.setCrtFileName(new String(Base64.encodeBase64("ca.crt".getBytes())));
                        response.setCrtFileSize(caFile.length());
                        response.setCrtFile(FileUtil.encodeBase64FileTOString(caFile.getPath()));

                        File ovpnFile = getFile(down, "ovpn");
                        File strategyFile = getFile(down, "strategy");

                        response.setOvpnFileName(new String(Base64.encodeBase64("client.ovpn".getBytes())));
                        response.setOvpnFileSize(ovpnFile.length());
                        response.setOvpnFile(FileUtil.encodeBase64FileTOString(ovpnFile.getPath()));

                        response.setStrategyFileName(new String(Base64.encodeBase64(strategyFile.getName().getBytes())));
                        response.setStrategyFileSize(strategyFile.length());
                        response.setStrategyFile(FileUtil.encodeBase64FileTOString(strategyFile.getPath()));

                        response.setResult(SipXml.ResultSuccess);
                        responseBody = AESEncoder(response.toString());
                    } else if(SipType.DownCA.equalsIgnoreCase(cmdType)) { //ca 下载ca证书
                        DownRequest down = new DownRequest().xmlToBean(body.getBytes(charset));
                        DownCAResponse response = new DownCAResponse();
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        response.setDeviceId(serviceUtils.deviceId);
                        response.setCmdType(down.getCmdType());
                        response.setDeviceType(down.getDeviceType());

                        File crtFile = getFile(down, "crt");
                        File keyFile = getFile(down, "key");

                        response.setKeyFileName(new String(Base64.encodeBase64("client.key".getBytes())));
                        response.setKeyFileSize(keyFile.length());
                        response.setKeyFile(FileUtil.encodeBase64FileTOString(keyFile.getPath()));

                        response.setCrtFileName(new String(Base64.encodeBase64("client.crt".getBytes())));
                        response.setCrtFileSize(crtFile.length());
                        response.setCrtFile(FileUtil.encodeBase64FileTOString(crtFile.getPath()));

                        response.setResult(SipXml.ResultSuccess);
                        responseBody = AESEncoder(response.toString());
                    } else if(SipType.ApplyCA.equalsIgnoreCase(cmdType)) { //ca 申请证书
                        ApplyRequest apply = new ApplyRequest().xmlToBean(body.getBytes(charset));
                        //TODO 申请证书的信息入库
                        //生成证书
                        logger.info("申请证书的信息入库...");
                        ApplyResponse response = new ApplyResponse();
                        ServiceUtils serviceUtils = ServiceUtils.getService();
                        response.setDeviceId(serviceUtils.deviceId);
                        response.setCmdType(apply.getCmdType());
                        response.setDeviceType(apply.getDeviceType());
                        response.setResult(SipXml.ResultSuccess);
                        responseBody = AESEncoder(response.toString());
                    } else {
                        responseBody = "<?xml version=\"1.0\">\r\n\r\n<Response>\r\n<default>"+System.currentTimeMillis()+"</default>\r\n</Response>";
                        responseBody = AESEncoder(responseBody);
                    }
            } else {
                responseBody = "<?xml version=\"1.0\">\r\n\r\n<Response>\r\n<default>"+System.currentTimeMillis()+"</default>\r\n<msg>request body is null</msg>\r\n</Response>";
            }
            byte[] body = responseBody.getBytes(charset);
            messageInfo = new MessageInfo();
            messageInfo.setVersion(MessageInfo.Version);
            messageInfo.setBodyLen(body.length);
            messageInfo.setReserved(new byte[21]);
            messageInfo.setBody(body);
            session.write(messageInfo);
        } else {
            logger.info("string:" + message.toString());

        }
    }

    private File getFile(DownRequest request,String type) {
        if("strategy".equals(type)) {
            if(SipType.Win.equals(request.getOsType())) {
                return new File(StringContext.systemPath+"/config/strategy.xml");
            }
        } else if("ovpn".equals(type)) {
//            if(SipType.Win.equals(request.getOsType())) {
//                return new File(StringContext.systemPath+"/client_config/VPN_windows.ovpn");
//            } else {
                return new File(StringContext.systemPath+"/client_config/VPN_phone.ovpn");
//            }
        } else if("ca".equals(type)) {
            if(SipType.Win.equals(request.getOsType())) {
                if(SipType.DownConfig.equalsIgnoreCase(request.getCmdType())) {
                    return new File(StringContext.systemPath+"/ssl/ca/ca.pem");
                }
            }
        }else if("ta".equals(type)) {
            if(SipType.Win.equals(request.getOsType())) {
                if(SipType.DownConfig.equalsIgnoreCase(request.getCmdType())) {
                    return new File(StringContext.server_sslPath+"/static_key/ta.key");
                }
            }
        } else if("key".equals(type)) {
            if(SipType.Win.equals(request.getOsType())) {
                 if(SipType.DownCA.equalsIgnoreCase(request.getCmdType())) {
                    if(request.getDeviceId()!=null) {
                        return new File(StringContext.systemPath + "/certificate/"+request.getDeviceId()+".cer");
                    }
                }
            }
        } else if("crt".equals(type)) {
            if(SipType.Win.equals(request.getOsType())) {
                if(SipType.DownCA.equalsIgnoreCase(request.getCmdType())) {
                    if(request.getDeviceId()!=null) {
                        return new File(StringContext.systemPath + "/certificate/"+request.getDeviceId()+".key");
                    }
                }
            }
        }
        return null;
    }

    private String AESDecoder(String bodyStr) {

        byte[] decryptResult = Base64.decodeBase64(bodyStr.getBytes());
        return new String(decryptResult);
    }

    private String AESEncoder(String bodyStr) {
        byte[] encryptResult = Base64.encodeBase64(bodyStr.getBytes());
        return new String(encryptResult);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info("Disconnecting the idle.");
    }

    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.warn(cause.getMessage(),cause);
        session.close(true);
    }
}
