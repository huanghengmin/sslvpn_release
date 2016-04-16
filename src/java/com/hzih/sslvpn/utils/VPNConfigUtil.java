package com.hzih.sslvpn.utils;

import com.hzih.sslvpn.domain.*;
import com.hzih.sslvpn.web.action.sslvpn.access_control.AccessControlXML;
import com.inetec.common.security.License;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-10
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class VPNConfigUtil {

    /**
     * 构建android 客户端配置文件
     *
     * @param server        服务器端配置对象
     * @param androidConfig android 客户端配置文件保存路径
     * @throws Exception
     */
    public static void createAndroidConfig(Server server, String androidConfig,List<Balancer> balancers) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("client").append("\n");

//        sb.append("max-routes 5000").append("\n");

        sb.append("dev tun").append("\n");
        //服务监听协议
        sb.append("proto " + server.getProtocol()).append("\n");

        if (server.getLocal().equals("all_interface")) {
            //服务监听地址
            sb.append("remote www.sslvpn.net " + server.getPort()).append("\n");
        } else {
            //服务端口
            sb.append("remote " + server.getLocal() + " " + server.getPort()).append("\n");
        }

        if (null != balancers && balancers.size() > 0) {
            for (Balancer balancer : balancers) {
                sb.append("remote " + balancer.getHost() + " " + balancer.getPort()).append("\n");
            }
        }

        sb.append("persist-key").append("\n");
        sb.append("persist-tun").append("\n");
        //证书相关配置
        sb.append("ca ca.crt").append("\n");
        sb.append("cert client.crt").append("\n");
        sb.append("key client.key").append("\n");
        //加密tls文件
        sb.append("tls-auth ta.key 1").append("\n");
        //加密算法
        sb.append("cipher " + server.getCipher()).append("\n");
        //支持客户端数据压缩
        if (server.getComp_lzo() == 1)
            sb.append("comp-lzo").append("\n");
        //日志级别
//        sb.append("verb " + server.getVerb()).append("\n");
        //重复日志记录数
//        sb.append("mute " + server.getMute()).append("\n");

        File file = new File(androidConfig);
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
    }

    public static void createWindowsConfig(Server server, String windowsConfig,List<Balancer> balancers) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("client").append("\n");

        sb.append("max-routes 5000").append("\n");

        sb.append("dev tun").append("\n");
        //服务监听协议
        sb.append("proto " + server.getProtocol()).append("\n");
        if (server.getLocal().equals("all_interface")) {
            //服务监听地址
            sb.append("remote www.sslvpn.net " + server.getPort()).append("\n");
        } else {
            //服务端口
            sb.append("remote " + server.getLocal() + " " + server.getPort()).append("\n");
        }

        if (null != balancers && balancers.size() > 0) {
            for (Balancer balancer : balancers) {
                sb.append("remote " + balancer.getHost() + " " + balancer.getPort()).append("\n");
            }
            //也可以改为随机连接
            sb.append("remote-random" ).append("\n");
        }
        sb.append("resolv-retry infinite").append("\n");

        sb.append("persist-key").append("\n");
        sb.append("persist-tun").append("\n");
        //证书相关配置
        sb.append("ca ca.crt").append("\n");

        //加密tls文件
        sb.append("tls-auth ta.key 1").append("\n");

        //加密算法
        sb.append("cipher " + server.getCipher()).append("\n");
        //支持客户端数据压缩
        if (server.getComp_lzo() == 1)
            sb.append("comp-lzo").append("\n");
        //日志级别
        sb.append("verb " + server.getVerb()).append("\n");
        //重复日志记录数
        sb.append("mute " + server.getMute()).append("\n");

        File file = new File(windowsConfig);
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
    }

    public static void configServer(Server server, String server_file_path, List<PrivateNet> nets,List<Balancer> balancers) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (!server.getLocal().equals("all_interface")) {
            //服务监听地址
            sb.append("local " + server.getLocal()).append("\n");
        }
        //服务监听协议
        sb.append("proto " + server.getProtocol()).append("\n");
        //服务端口
        sb.append("port " + server.getPort()).append("\n");
        sb.append("dev tun").append("\n");
        sb.append("management localhost 7505").append("\n");

        //证书相关配置
        sb.append("ca " + StringContext.ca_file).append("\n");
        sb.append("cert " + StringContext.server_file).append("\n");
        sb.append("key " + StringContext.key_file).append("\n");

        File crl = new File(StringContext.server_sslPath + "/crl/crl.pem");
        if (crl.exists() && crl.length() > 0) {
            sb.append("crl-verify " + StringContext.server_sslPath + "/crl/crl.pem").append("\n");
        }

        sb.append("dh " + StringContext.server_sslPath + "/dh/dh1024.pem").append("\n");

        //动态ip分配地址
        sb.append("server " + server.getServer()).append("\n");

        //静态ip地址路由
        if (server.getStatic_net() != null && !"".equals(server.getStatic_net()))
            sb.append("route ").append(server.getStatic_net()).append("\n");

        //如果允许访问内部私有网络
        if (null != server.getAllow_private_net() && server.getAllow_private_net().equals("1")) {
            //加入所有子网路由表
            if (null != nets && nets.size() > 0) {
                for (PrivateNet net : nets) {
                    sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
                }
            }
        }
        //此处配置客户端相互通信即所有客户端可以相互通信
        if (server.getClient_to_client() == 1) {
            sb.append("client-to-client").append("\n");
        }
        //客户端可以使用同一证书多次连接
        if (server.getDuplicate_cn() == 1) {
            sb.append("duplicate-cn").append("\n");
        } else {
            sb.append("ifconfig-pool-persist " + StringContext.pool_path + "/ipp.txt").append("\n");

            //客户端权限配置地址
            sb.append("client-config-dir " + StringContext.user_manager_path).append("\n");
            //加入后必须在client-config-dir 目录中有对应的文件才能连接
//            sb.append(";ccd-exclusive ").append("\n");
        }
        //保持存活时间
        sb.append("keepalive " + server.getKeep_alive_interval() + " " + server.getKeep_alive()).append("\n");
        //支持客户端数据压缩
        if (server.getComp_lzo() == 1)
            sb.append("comp-lzo").append("\n");

        //配置客户端所有流量都通过VPN服务器
        if (server.getTraffic_server() == 1) {
            sb.append("push ").append("\"redirect-gateway def1 bypass-dhcp" + "\"").append("\n");
        }
        if (server.getClient_dns_type() == 2) {
            if (null != server.getClient_first_dns() && "" != server.getClient_first_dns()) {
                sb.append("push ").append("\"dhcp-option DNS " + server.getClient_first_dns() + "\"").append("\n");
            }
            if (null != server.getClient_second_dns() && "" != server.getClient_second_dns()) {
                sb.append("push ").append("\"dhcp-option DNS " + server.getClient_second_dns() + "\"").append("\n");
            }
        }
        //VPN允许用户通过它访问互联网
        if (server.getAllow_ping_server() == 1) {
            sb.append("push ").append("\"dhcp-option DNS 8.8.8.8\"").append("\n");
        }
        //默认域名后缀
        if (null != server.getDefault_domain_suffix()) {
            sb.append("push ").append("\"dhcp-option DOMAIN " + server.getDefault_domain_suffix() + "\"").append("\n");
        }
        //加密tls文件
        sb.append("tls-auth " + StringContext.static_key_path + "/ta.key" + " 0").append("\n");

        //加密算法
        sb.append("cipher " + server.getCipher()).append("\n");

        //最大连接数
//        sb.append("max-clients " + server.getMax_clients()).append("\n");
        int max = 3000;
        /*try {
            max = License.getMaxConnect();
        } catch (Exception e) {

        }*/
        sb.append("max-clients " + max).append("\n");

        sb.append("persist-key").append("\n");
        sb.append("persist-tun").append("\n");
        sb.append("status " + StringContext.serverLogPath + "/" + "server_status.log").append("\n");

        if (server.getLog_append() == 1)
            sb.append("log-append  " + StringContext.serverLogPath + "/" + "server.log").append("\n");
        else
            sb.append("log " + StringContext.serverLogPath + "/" + "server.log").append("\n");

        //日志级别
        sb.append("verb " + server.getVerb()).append("\n");
        //重复日志记录数
        sb.append("mute " + server.getMute()).append("\n");

        sb.append("script-security 2").append("\n");

        if (server.getUse_learn_address_script() == 1)
            sb.append("learn-address " + StringContext.script_path + "/learn_address_script.sh").append("\n");

        if (server.getUse_connect_script() == 1)
            sb.append("client-connect " + StringContext.script_path + "/client_connect.sh").append("\n");

        if (server.getUse_disconnect_script() == 1)
            sb.append("client-disconnect " + StringContext.script_path + "/client_disconnect.sh").append("\n");

        /*if (AccessControlXML.getAttribute(AccessControlXML.bs_proxy_ip) != null)
            sb.append("bs-proxy-ip " + AccessControlXML.getAttribute(AccessControlXML.bs_proxy_ip)).append("\n");

        if (AccessControlXML.getAttribute(AccessControlXML.bs_proxy_port) != null)
            sb.append("bs-proxy-port " + AccessControlXML.getAttribute(AccessControlXML.bs_proxy_port)).append("\n");

        if (AccessControlXML.getAttribute(AccessControlXML.control_url) != null)
            sb.append("auth-url " + AccessControlXML.getAttribute(AccessControlXML.control_url)).append("\n");

        if (AccessControlXML.getAttribute(AccessControlXML.status) != null && AccessControlXML.getAttribute(AccessControlXML.status).equalsIgnoreCase("1"))
            sb.append("auth-flag   true").append("\n");
        else
            sb.append("auth-flag   false").append("\n");

        if (AccessControlXML.getAttribute(AccessControlXML.proxy_port) != null)
            sb.append("proxy-port " + AccessControlXML.getAttribute(AccessControlXML.proxy_port)).append("\n");*/
        File file = new File(server_file_path);
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();

        createAndroidConfig(server, StringContext.android_config_file,balancers);
        createWindowsConfig(server, StringContext.windows_config_file,balancers);

    }

    public static void configUser(User user, String manager_user_path, List<PrivateNet> nets) throws Exception {
        StringBuilder sb = new StringBuilder();
        Set<PrivateNet> user_nets = null;
        try {
            user_nets = user.getUser_subNets();
        } catch (Exception e) {

        }

        int client_to_client = 0;

        try {
            client_to_client = user.getAllow_all_client();
        } catch (Exception e) {

        }

        int allow_all = 0;

        try {
            allow_all = user.getAllow_all_subnet();
        } catch (Exception e) {

        }


        int dynamic = 1;
        try {
            dynamic = user.getDynamic_ip();
        } catch (Exception e) {

        }

        if (dynamic == 0) {
            //push static ip
            String static_ip = user.getStatic_ip();
            String start_ip = static_ip.substring(0, static_ip.lastIndexOf("."));
            int end = Integer.parseInt(static_ip.substring(static_ip.lastIndexOf(".") + 1, static_ip.length()));
            sb.append("ifconfig-push ").append(user.getStatic_ip()).append(" ").append(start_ip).append(end + 1).append("\n");
        }
        if (client_to_client == 1) {
            sb.append("client-to-client").append("\n");
        }
        if (allow_all == 1) {
            //all private nets
            if (null != nets && nets.size() > 0) {
                for (PrivateNet net : nets) {
                    sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
                }
            }
        } else {
            //user_nets
            if (null != user_nets && user_nets.size() > 0) {
                for (PrivateNet net : user_nets) {
                    sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
                }
            }
        }
        if (user.getEnabled() == 0) {
            sb.append("disable ").append("\n");
        }
        File file = new File(manager_user_path + "/" + user.getCn());
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
    }

    public static void configUser(User user, String manager_user_path, List<PrivateNet> nets, Groups groups) throws Exception {
        StringBuilder sb = new StringBuilder();
        Set<PrivateNet> user_nets = null;
        try {
            user_nets = user.getUser_subNets();
        } catch (Exception e) {

        }

        int client_to_client = 0;

        try {
            client_to_client = user.getAllow_all_client();
        } catch (Exception e) {

        }

        int allow_all = 0;

        try {
            allow_all = user.getAllow_all_subnet();
        } catch (Exception e) {

        }


        int dynamic = 1;
        try {
            dynamic = user.getDynamic_ip();
        } catch (Exception e) {

        }
        if (dynamic == 0) {
            //push static ip
            String static_ip = user.getStatic_ip();
            String start_ip = static_ip.substring(0, static_ip.lastIndexOf("."));
            int end = Integer.parseInt(static_ip.substring(static_ip.lastIndexOf(".") + 1, static_ip.length()));
            sb.append("ifconfig-push ").append(user.getStatic_ip()).append(" ").append(start_ip).append(end + 1).append("\n");
        }
        if (client_to_client == 1) {
            sb.append("client-to-client").append("\n");
        }
        if (allow_all == 1) {
            //all private nets
            if (null != nets && nets.size() > 0) {
                for (PrivateNet net : nets) {
                    sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
                }
            }
        } else {
            //user_nets
            List<PrivateNet> privateNets = new ArrayList<>();

            if (groups != null) {
                Set<PrivateNet> netSet = groups.getPrivateNets();
                if (netSet != null) {
                    for (PrivateNet net : netSet) {
                        if (!privateNets.contains(net))
                            privateNets.add(net);
                    }
                }

            }

            if (null != user_nets && user_nets.size() > 0) {
                for (PrivateNet net : user_nets) {
                    if (!privateNets.contains(net)) {
                        privateNets.add(net);
                    }
//                    sb.append("push ").append("\"route "+net.getNet()+" "+net.getNet_mask()+"\"").append("\n");
                }
            }

            if (privateNets != null && privateNets.size() > 0) {
                for (PrivateNet net : privateNets) {
                    sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
                }
            }
        }
        if (user.getEnabled() == 0) {
            sb.append("disable ").append("\n");
        }
        File file = new File(manager_user_path + "/" + user.getCn());
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
    }
}
