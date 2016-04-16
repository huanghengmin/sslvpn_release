<%@page contentType="text/html;charset=utf-8" %>
<%@include file="/include/common.jsp" %>
<%@include file="/taglib.jsp" %>

<c:if test="${account==null}">
    <%response.sendRedirect("login.jsp");%>
</c:if>
<html>
<head>
    <title>SSL认证网关管理中心</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <META http-equiv="x-ua-compatible" content="ie=EmulateIE6"/>

    <%--<script type="text/javascript" language="Javascript" src="<c:url value="/js/extux.js"/>"></script>--%>
    <%--<script type="text/javascript" language="Javascript" src="<c:url value="/js/index.jsp"/>"></script>--%>
</head>
<body>
<DIV id=top-div>
    <DIV id=funmenu><A onclick="setFrontPage();return false;"
                       href="javascript:void(0);"><IMG src="img/house.png">设为首页</A>| <A
            onclick=window.external.AddFavorite(location.href,document.title);
            href="javascript:void(0);"><IMG src="img/page_white_office.png">加入收藏</A>|
        <A onclick="showUpdatePwd();return false;" href="javascript:void(0);"><IMG
                src="img/key.png">修改密码</A>| <A onclick="logout();return false;"
                                               href="javascript:void(0);"><IMG src="img/door_out.png">退出系统</A></DIV>
</DIV>
<DIV>
<script type="text/javascript">
var centerPanel;
Ext.onReady(function () {
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var imagePath = 'js/ext/resources/images';
    Ext.BLANK_IMAGE_URL = imagePath + '/default/tree/s.gif';

    var menu_root_node_1 = new Ext.tree.TreeNode({
        text: '权限管理',
        expanded: false
    });
    var menu_root_node_2 = new Ext.tree.TreeNode({
        text: '网络管理',
        expanded: false
    });
    var menu_root_node_3 = new Ext.tree.TreeNode({
        text: '系统管理',
        expanded: false
    });
    var menu_root_node_4 = new Ext.tree.TreeNode({
        text: '审计管理',
        expanded: false
    });
    var menu_root_node_5 = new Ext.tree.TreeNode({
        text: '服务状态',
        expanded: false
    });
    var menu_root_node_6 = new Ext.tree.TreeNode({
        text: '服务配置',
        expanded: false
    });
    var menu_root_node_7 = new Ext.tree.TreeNode({
        text: '用户管理',
        expanded: false
    });
    var menu_root_node_8 = new Ext.tree.TreeNode({
        text: '系统配置',
        expanded: false
    });
    var menu_root_node_9 = new Ext.tree.TreeNode({
        text: '吊销列表',
        expanded: false
    });
    var menu_root_node_10 = new Ext.tree.TreeNode({
        text: '系统监控',
        expanded: false
    });


    var mrn_1_1 = new Ext.tree.TreeNode({
        id: 'mrn_1_1',
        text: '用户管理',
        leaf: true,
        url: 'pages/user/userIndex.jsp'
    });
    var mrn_1_2 = new Ext.tree.TreeNode({
        id: 'mrn_1_2',
        text: '角色管理',
        leaf: true,
        url: 'pages/user/roleIndex.jsp'
    });
    var mrn_1_3 = new Ext.tree.TreeNode({
        id: 'mrn_1_3',
        text: '安全策略',
        leaf: true,
        url: 'pages/user/safePolicy.jsp'
    });

    <lbs:access code="SECOND_YHGL">
    menu_root_node_1.appendChild(mrn_1_1);
    </lbs:access>
    <lbs:access code="SECOND_JSGL">
    menu_root_node_1.appendChild(mrn_1_2);
    </lbs:access>
    <lbs:access code="SECOND_AQCL">
    menu_root_node_1.appendChild(mrn_1_3);
    </lbs:access>

    var mrn_2_1 = new Ext.tree.TreeNode({
        id: 'mrn_2_1',
        text: '接口管理',
        leaf: true,
        url: 'pages/net/manager_interface.jsp'
    });
    var mrn_2_2 = new Ext.tree.TreeNode({
        id: 'mrn_2_2',
        text: '连通测试',
        leaf: true,
        url: 'pages/net/manager_pingTelnet.jsp'
    });
    var mrn_2_3 = new Ext.tree.TreeNode({
        id: 'mrn_2_3',
        text: '路由管理',
        leaf: true,
        url: 'pages/net/manager_router.jsp'
    });
    var mrn_2_4 = new Ext.tree.TreeNode({
        id: 'mrn_2_5',
        text: '安全配置',
        leaf: true,
        url: 'pages/net/manager_security_config.jsp'
    });
    <lbs:access code="SECOND_JKGL">
    menu_root_node_2.appendChild(mrn_2_1);
    </lbs:access>
    <lbs:access code="SECOND_LTCS">
    menu_root_node_2.appendChild(mrn_2_2);
    </lbs:access>
    <lbs:access code="SECOND_LYGL">
    menu_root_node_2.appendChild(mrn_2_3);
    </lbs:access>
    <lbs:access code="SECOND_PZGL">
    menu_root_node_2.appendChild(mrn_2_4);
    </lbs:access>

    var mrn_3_1 = new Ext.tree.TreeNode({
        id: 'mrn_3_1',
        text: '平台说明',
        leaf: true,
        url: 'pages/system/manager_version.jsp'
    });
    var mrn_3_2 = new Ext.tree.TreeNode({
        id: 'mrn_3_2',
        text: '平台管理',
        leaf: true,
        url: 'pages/system/manager_platform.jsp'
    });

    var mrn_3_3 = new Ext.tree.TreeNode({
        id: 'mrn_3_3',
        text: '证书管理',
        leaf: true,
        url: 'pages/system/manager_license.jsp'
    });
    var mrn_3_4 = new Ext.tree.TreeNode({
        id: 'mrn_3_4',
        text: '日志下载',
        leaf: true,
        url: 'pages/system/manager-downloadLog.jsp'
    });

    var mrn_3_5 = new Ext.tree.TreeNode({
        id: 'mrn_3_5',
        text: '版本升级',
        leaf: true,
        url: 'pages/system/manager_upgrade_version.jsp'
    });

    var mrn_3_6 = new Ext.tree.TreeNode({
        id: 'mrn_3_6',
        text: '客户端版本',
        leaf: true,
        url: 'pages/system/manager_client_version.jsp'
    });

    var mrn_3_7 = new Ext.tree.TreeNode({
        id: 'mrn_3_7',
        text: '双机热备',
        leaf: true,
        url: 'pages/config/manager_equipment_backup.jsp'
    });

    var mrn_3_8 = new Ext.tree.TreeNode({
        id: 'mrn_3_8',
        text: '服务管理',
        leaf: true,
        url: 'pages/system/manager_service.jsp'
    });

    <lbs:access code="SECOND_PTSM">
    menu_root_node_3.appendChild(mrn_3_1);
    </lbs:access>
    <lbs:access code="SECOND_PTGL">
    menu_root_node_3.appendChild(mrn_3_2);
    </lbs:access>
    <lbs:access code="SECOND_ZSGL">
    menu_root_node_3.appendChild(mrn_3_3);
    </lbs:access>
    <lbs:access code="SECOND_RZXZ">
    menu_root_node_3.appendChild(mrn_3_4);
    </lbs:access>
    <lbs:access code="SECOND_BBSJ">
    menu_root_node_3.appendChild(mrn_3_5);
    </lbs:access>
    <%--<lbs:access code="SECOND_BBSJ">
    menu_root_node_3.appendChild(mrn_3_6);
    </lbs:access>--%>
    <lbs:access code="SECOND_BBSJ">
    menu_root_node_3.appendChild(mrn_3_7);
    </lbs:access>
    <lbs:access code="SECOND_BBSJ">
    menu_root_node_3.appendChild(mrn_3_8);
    </lbs:access>

    var mrn_4_1 = new Ext.tree.TreeNode({
        id: 'mrn_4_1',
        text: '管理日志',
        leaf: true,
        url: 'pages/audit/audit_user.jsp'
    });
    <lbs:access code="SECOND_GLRZ">
    menu_root_node_4.appendChild(mrn_4_1);
    </lbs:access>

    var mrn_5_1 = new Ext.tree.TreeNode({
        id: 'mrn_5_1',
        text: '服务状态',
        leaf: true,
        url: 'pages/server_status/server_status.jsp'
    });
    var mrn_5_2 = new Ext.tree.TreeNode({
        id: 'mrn_5_2',
        text: '在线用户',
        leaf: true,
        url: 'pages/server_status/server_online.jsp'
    });
    var mrn_5_3 = new Ext.tree.TreeNode({
        id: 'mrn_5_3',
        text: '用户日志',
        leaf: true,
        url: 'pages/server_status/server_logs.jsp'
    });
    <lbs:access code="SECOND_FWZT">
    menu_root_node_5.appendChild(mrn_5_1);
    </lbs:access>
    <lbs:access code="SECOND_ZXYH">
    menu_root_node_5.appendChild(mrn_5_2);
    </lbs:access>
    <lbs:access code="SECOND_YHRZ">
    menu_root_node_5.appendChild(mrn_5_3);
    </lbs:access>

    var mrn_6_1 = new Ext.tree.TreeNode({
        id: 'mrn_6_1',
        text: '网络配置',
        leaf: true,
        url: 'pages/server_config/server_net_config.jsp'
    });
    var mrn_6_2 = new Ext.tree.TreeNode({
        id: 'mrn_6_2',
        text: '基本配置',
        leaf: true,
        url: 'pages/server_config/server_set.jsp'
    });
    var mrn_6_3 = new Ext.tree.TreeNode({
        id: 'mrn_6_3',
        text: '高级配置',
        leaf: true,
        url: 'pages/server_config/server_advanceds.jsp'
    });

    var mrn_6_4 = new Ext.tree.TreeNode({
        id: 'mrn_6_4',
        text: '证书管理',
        leaf: true,
        url: 'pages/server_config/server_web_config.jsp'
    });

    /* var mrn_6_5 = new Ext.tree.TreeNode({
        id: 'mrn_6_5',
        text: '子网配置',
        leaf: true,
        url: 'pages/server_config/server_subnet_config.jsp'
    });*/

    <lbs:access code="SECOND_WLPZ">
    menu_root_node_6.appendChild(mrn_6_1);
    </lbs:access>
    <lbs:access code="SECOND_JBPZ">
    menu_root_node_6.appendChild(mrn_6_2);
    </lbs:access>
    <lbs:access code="SECOND_GJPZ">
    menu_root_node_6.appendChild(mrn_6_3);
    </lbs:access>
    <lbs:access code="SECOND_ZSPZ">
    menu_root_node_6.appendChild(mrn_6_4);
    </lbs:access>
    <%--<lbs:access code="SECOND_ZWPZ">--%>
    <%--menu_root_node_6.appendChild(mrn_6_5);--%>
    <%--</lbs:access>--%>


    var mrn_7_1 = new Ext.tree.TreeNode({
        id: 'mrn_7_1',
        text: '用户权限',
        leaf: true,
        url: 'pages/user_manager/user_permissions.jsp'
    });
    var mrn_7_2 = new Ext.tree.TreeNode({
        id: 'mrn_7_2',
        text: '用户组权限',
        leaf: true,
        url: 'pages/user_manager/group_permissions.jsp'
    });
    var mrn_7_3 = new Ext.tree.TreeNode({
        id: 'mrn_7_3',
        text: '用户记录',
        leaf: true,
        url: 'pages/user_manager/ipp.jsp'
    });

    <lbs:access code="SECOND_YHQX">
    menu_root_node_7.appendChild(mrn_7_1);
    </lbs:access>
    <lbs:access code="SECOND_YHZQX">
    menu_root_node_7.appendChild(mrn_7_2);
    </lbs:access>
    <lbs:access code="SECOND_YHJL">
    menu_root_node_7.appendChild(mrn_7_3);
    </lbs:access>


    var mrn_8_1 = new Ext.tree.TreeNode({
        id: 'mrn_8_1',
        text: 'LDAP配置',
        leaf: true,
        url: 'pages/ldap/ldap_config.jsp'
    });
    var mrn_8_2 = new Ext.tree.TreeNode({
        id: 'mrn_8_2',
        text: '日志服务器',
        leaf: true,
        url: 'pages/syslog/syslog_config.jsp'
    });

    var mrn_8_4 = new Ext.tree.TreeNode({
        id: 'mrn_8_4',
        text: '客户端策略',
        leaf: true,
        url: 'pages/strategy/strategy.jsp'
    });

    var mrn_8_5 = new Ext.tree.TreeNode({
        id: 'mrn_8_5',
        text: '服务器配置',
        leaf: true,
        url: 'pages/config/config_properties.jsp'
    });

   /* var mrn_8_5 = new Ext.tree.TreeNode({
        id: 'mrn_8_5',
        text: '访问控制',
        leaf: true,
        url: 'pages/access_control/access_control.jsp'
    });*/

 /*   var mrn_8_3 = new Ext.tree.TreeNode({
        id: 'mrn_8_3',
        text: '监测服务配置',
        leaf: true,
        url: 'pages/monitor/monitor_config.jsp'
    });*/

    <lbs:access code="SECOND_LDAP">
    menu_root_node_8.appendChild(mrn_8_1);
    </lbs:access>
    <lbs:access code="SECOND_SYSLOG">
    menu_root_node_8.appendChild(mrn_8_2);
    </lbs:access>
    <lbs:access code="SECOND_STRATEGY">
    menu_root_node_8.appendChild(mrn_8_4);
    </lbs:access>
    <lbs:access code="SECOND_STRATEGY">
    menu_root_node_8.appendChild(mrn_8_5);
    </lbs:access>
    <%--<lbs:access code="SECOND_CONTROL">--%>
    <%--menu_root_node_8.appendChild(mrn_8_5);--%>
    <%--</lbs:access>--%>
    <%--<lbs:access code="SECOND_JCPZ">--%>
    <%--menu_root_node_8.appendChild(mrn_8_3);--%>
    <%--</lbs:access>--%>

    var mrn_9_1 = new Ext.tree.TreeNode({
        id: 'mrn_9_1',
        text: '吊销列表',
        leaf: true,
        url: 'pages/crl/revoke_certificates_list.jsp'
    });

    <lbs:access code="SECOND_DXLB">
    menu_root_node_9.appendChild(mrn_9_1);
    </lbs:access>


    var mrn_10_1 = new Ext.tree.TreeNode({
        id: 'mrn_10_1',
        text: '主机监控',
        leaf: true,
        url: 'pages/host/myjfreezhexian.jsp'
    });

    <lbs:access code="SECOND_ZJJK">
    menu_root_node_10.appendChild(mrn_10_1);
    </lbs:access>


    var tree_menu_1 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_1,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_2 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_2,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_3 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_3,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_4 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_4,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_5 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_5,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_6 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_6,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_7 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_7,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_8 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_8,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });
    var tree_menu_9 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_9,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });

    var tree_menu_10 = new Ext.tree.TreePanel({
        border: false,
        root: menu_root_node_10,
        rootVisible: false,
        listeners: {
            click: nodeClick
        }
    });

    function nodeClick(node, e) {
        if (node.isLeaf()) {
            var _url = node.attributes.url;
            if (_url != '') {
                if (_url.indexOf('?') > 0)
                    _url += '&time=' + new Date().getTime();
                else
                    _url += '?time=' + new Date().getTime();
            }
            var _tab = centerPanel.getComponent(node.id);
            if (!_tab) {
                centerPanel.removeAll();
                centerPanel.add({
                    id: node.id,
                    title: node.text,
                    closable: true,
                    iconCls: node.attributes.iconCls,
                    html: '<iframe id="frame_' + node.id + '" width="100%" height="100%" frameborder="0" src="' + _url + '"></iframe>'
                });
            }
            centerPanel.setActiveTab(node.id);
        }
    }

    var northBar = new Ext.Toolbar({
        id: 'northBar',
        items: [
            {xtype: 'tbtext',
                id: 'msg_title',
                text: ''
            },
            {
                xtype: "tbfill"
            },
            {
                id: 'warningMsg',
                iconCls: 'warning',
                hidden: true,
                handler: function () {
                    eastPanel.expand(true);
                }
            },
            {
                xtype: 'tbseparator'
            },
            {
                pressed: false,
                text: '刷新',
                iconCls: 'refresh',
                handler: function () {
                    var mID = centerPanel.getActiveTab().getId();
                    if (centerPanel.getActiveTab().getStateId() == null) {
                        window.frames[0].location.reload();
                    } else {
                        window.parent.document.getElementById('frame_' + mID).contentWindow.location.reload();
                    }
                }
            },
            {
                xtype: 'tbseparator'
            },
            {
                pressed: false,
                text: '帮助',
                iconCls: 'help',
                handler: function () {
                    window.open('help.doc');
                }
                /*},{
                 xtype: 'tbseparator'
                 },{
                 xtype:"combo",
                 width: 120*/
            }
        ]
    });

    //页面的上部分
    var northPanel = new Ext.Panel({
        region: 'north',			//北部，即顶部，上面
        contentEl: 'top-div',		//面板包含的内容
        split: false,
        titlebar: false,
        border: false, 				//是否显示边框
        collapsible: false, 		//是否可以收缩,默认不可以收缩，即不显示收缩箭头
        height: 86,
        bbar: northBar
    });

    //左边菜单
    var westPanel = new Ext.Panel({
        title: '系统功能',			//面板名称
        region: 'west',			//该面板的位置，此处是西部 也就是左边
        split: true,				//为true时，布局边框变粗 ,默认为false
        titlebar: true,
        collapsible: true,
        animate: true,
        border: true,
        bodyStyle: 'border-bottom: 0px solid;',
        bodyborder: true,
        width: 200,
        minSize: 100,				//最小宽度
        maxSize: 300,
        layout: 'accordion',
        iconCls: 'title-1',
        layoutConfig: { 			//布局
            titleCollapse: true,
            animate: true
        },
        items: [
            <lbs:access code="TOP_QXGL" >
            {
                title: '权限管理',
                border: false,
                iconCls: 'user',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_1]
            },
            </lbs:access>
            <lbs:access code="TOP_WLGL" >
            {
                title: '网络管理',
                border: false,
                iconCls: 'wlgl',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_2]
            },
            </lbs:access>
            <lbs:access code="TOP_XTGL" >
            {
                title: '系统管理',
                border: false,
                iconCls: 'system',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_3]
            },
            </lbs:access>
            <lbs:access code="TOP_SJGL" >
            {
                title: '审计管理',
                border: false,
                iconCls: 'audit',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_4]
            },
            </lbs:access>
            <lbs:access code="TOP_FWZT" >
            {
                title: '服务状态',
                border: false,
                iconCls: 'serversettwo',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_5]
            },
            </lbs:access>
            <lbs:access code="TOP_FWPZ" >
            {
                title: '服务配置',
                border: false,
                iconCls: 'serverset',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_6]
            },
            </lbs:access>
            <lbs:access code="TOP_YHGL" >
            {
                title: '用户管理',
                border: false,
                iconCls: 'usertwo',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_7]
            },
            </lbs:access>
            <lbs:access code="TOP_XTPZ" >
            {
                title: '系统配置',
                border: false,
                iconCls: 'setting',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_8]
            },
            </lbs:access>
            <lbs:access code="TOP_DXGL" >
            {
                title: '吊销管理',
                border: false,
                iconCls: 'crl16',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_9]
            },
            </lbs:access>
            <lbs:access code="TOP_JKGL" >
            {
                title: '监控管理',
                border: false,
                iconCls: 'monitor',
                bodyStyle: 'border-bottom: 1px solid;padding-top: 5px;padding-left: 15px;',
                items: [tree_menu_10]
            },
            </lbs:access>
            {}
        ]
    });

    //页面的中间面板
    centerPanel = new Ext.TabPanel({
        id: 'mainContent',
        region: 'center',
        deferredRender: false,
        enableTabScroll: true,
        activeTab: 0,
        items: []
    });
    centerPanel.activate(0);

    var viewport = new Ext.Viewport({
        layout: 'border',
        loadMask: true,
        items: [northPanel, 		//上
            westPanel,  		//左
            centerPanel		//中
        ]
    });

    northBar.get(0).setText("您好！${account.name}");
    //检查会话是否超时
//                    var task = {
//                        run : function() {
//                            Ext.Ajax.request({
//                               url: 'checkTimeout.action?time='+new Date().getTime(),
//                               success: function(response){
//                                var result = response.responseText;
//                                if(result!=null&&result.length>0){
//                                    alert("会话过期，请重新登录");
//                                    location.href="login.jsp";
//                                }
//
//                               }
//                            });
//                        },
//                        interval : 10000
//                    };
//                    Ext.TaskMgr.start(task);
});

function showWindow(flag) {
    <%--if(flag==1){--%>
    <%--window.open("admin/forward.do?m=eqalert");--%>
    <%--}else if(flag==2){--%>
    <%--window.open("admin/forward.do?m=bsalert");--%>
    <%--}else if(flag==3){--%>
    <%--window.open("admin/forward.do?m=scalert");--%>
    <%--}--%>
}

function logout() {
    Ext.Msg.confirm("确认", "确认退出系统吗？", function (btn) {
        if (btn == 'yes') {
            window.location = "logout.action";
        } else {
            return false;
        }
    });
}

Ext.apply(Ext.form.VTypes, {
    //验证方法
    password: function (val, field) {//val指这里的文本框值，field指这个文本框组件
        if (field.password.password_id) {
            //password是自定义的配置参数，一般用来保存另外的组件的id值
            var pwd = Ext.get(field.password.password_id);//取得user_password的那个id的值
            return (val == pwd.getValue());//验证
        }
        return true;
    },
    //验证提示错误提示信息(注意：方法名+Text)
    passwordText: "两次密码输入不一致!"
});

var pwdForm = new Ext.FormPanel({
    region: 'center',
    deferredRender: true,
    frame: true,
    border: false,
    labelAlign: 'right',
    defaults: {xtype: "textfield", inputType: "password"},
    items: [
        {
            fieldLabel: '当前密码',
            name: 'pwd',
            id: 'pwd',
            width: 150
        },
        {
            fieldLabel: '输入新密码',
            name: 'newpwd',
            id: 'newpwd',
            width: 150
        },
        {
            fieldLabel: '再次输入新密码',
            name: 'rnewpwd',
            id: 'rnewpwd',
            width: 150,
            password: {password_id: 'newpwd'},
            vtype: 'password'
        }
    ]
});
var pwdWin;
function showUpdatePwd() {
    if (!pwdWin) {
        var pwdWin = new Ext.Window({
            layout: 'border',
            width: 310,
            height: 160,
            closeAction: 'hide',
            plain: true,
            modal: true,
            title: '修改密码',
            resizable: false,

            items: pwdForm,

            buttons: [
                {
                    text: '保存',
                    listeners: {
                        'click': function () {
                            pwdForm.getForm().submit({
                                clientValidation: true,
                                url: 'AccountAction_updatePwd.action',
                                success: function (form, action) {
                                    Ext.Msg.alert('保存成功', '保存成功！');
                                    pwdWin.close();
                                },
                                failure: function (form, action) {
                                    Ext.Msg.alert('保存失败', '系统错误，请联系管理员。');
                                    pwdWin.close();
                                }
                            });
                        }
                    }
                },
                {
                    text: '取消',
                    listeners: {
                        'click': function () {
                            pwdWin.close();
                        }
                    }
                }
            ]

        });

        pwdWin.show();
    }

}

function nodeClick2(_url, id, text) {
    if (_url != '') {
        if (_url.indexOf('?') > 0)
            _url += '&time=' + new Date().getTime();
        else
            _url += '?time=' + new Date().getTime();
    }
    var _tab = centerPanel.getComponent(id);
    if (!_tab) {
        centerPanel.removeAll();
        centerPanel.add({
            id: id,
            title: text,
            closable: true,
            iconCls: '',
            html: '<iframe id="frame_' + id + '" width="100%" height="100%" frameborder="0" src="' + _url + '"></iframe>'
        });
    }
    centerPanel.setActiveTab(id);
}

</script>
</DIV>
</body>
</html>