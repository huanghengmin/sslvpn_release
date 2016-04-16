Ext.onReady(function () {
    var tabs = new Ext.TabPanel({
        renderTo:Ext.getBody(),
        height:setHeight()
    });

    var record = new Ext.data.Record.create([
        {name:'protocol',mapping:'protocol'},
        {name:'port',mapping:'port'},
        {name:'interface',mapping:'interface'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../ServerStatusAction_findConfig.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"root"
    },record);
    var store = new Ext.data.Store({
        proxy : proxy,
        reader : reader
    });
    store.load();
    store.on('load',function(){
        var protocol = store.getAt(0).get('protocol');
        var port = store.getAt(0).get('port');
        var interface = store.getAt(0).get('interface');
        Ext.getCmp('server.interface').setValue(interface);
        Ext.getCmp('server.listen').setValue(protocol+'/'+port);
    });

    var panel = new Ext.form.FormPanel({
        id:'about.info',
        labelWidth:150,
        labelAlign:'right',
        border:false,
//        frame:true,
        loadMask:{ msg:'正在加载数据，请稍后.....' },
        autoScroll:true,
        items:[
            {
                id:'server.interface',
                xtype:'displayfield',
                fieldLabel:'服务器IP',
                value:'192.168.1.212'
            },
            /*{
             id:'ser.info',
             xtype:'displayfield',
             fieldLabel : '对用户进行身份验证',
             value:'PAM'
             },{
             id:'os.info',
             xtype:'displayfield',
             fieldLabel : '接受VPN客户端连接的IP地址'  ,
             value:'eth14：192.168.1.212'
             },*/{
                xtype:'displayfield',
                id:'server.listen',
                fieldLabel:'服务端口',
                value:'tcp/1194'
            },{
                xtype:'displayfield',
                id:'server.sucrity',
                fieldLabel:'策略端口',
                value:'tcp/80'
            }, {
                fieldLabel:'运行状态',
                name:'runtime',
                id:'runtime',
                value:"<font color='orange'>服务未启动</font>",
                xtype:'displayfield'

            }/*,{
             xtype:'displayfield',
             fieldLabel : 'OSI层',
             value:'3（路由/ NAT）'
             },{
             xtype:'displayfield',
             fieldLabel : '客户访问私有子网使用',
             value:'NAT'
             },{
             xtype:'displayfield',
             fieldLabel : '节点',
             value:'VPN'
             }*/
        ]
    });

//--------------------------------------------------------服务状态---------------------------------------------------//
    var fwztform = new Ext.form.FormPanel({
//        height:100,
//        height:setHeight(),
        border:false,
        labelWidth:150,
//        frame:true,
        defaultType:'textfield',
        buttonAlign:'left',
        labelAlign:'right',
        //此处添加url，那么在getForm().sumit方法不需要在添加了url地址了
//        baseParams:{create:true },
        defaults:{
            blankText:'不能为空!',
            msgTarget:'side'
        },
        items:[
            {
                fieldLabel:'服务状态',
                id:'serverstatus',
                name:'serverstatus',
                value:"[<font color='red'>未启动</font>]  [<a href='javascript:;' style='color:blue;' onclick='openServer()'>启动</a>]",
                xtype:'displayfield'

            }/*,
            {
                fieldLabel:'运行状态',
                name:'runtime',
                id:'runtime',
                value:"<font color='orange'>服务未启动</font>",
                xtype:'displayfield'

            }*/

        ]
    });

    tabs.add({
        title:'服务状态',
        items:[
            {
                xtype:'fieldset',
                title:'运行状态控制',
                collapsible:true,
                items:[{
                    xtype:'fieldset',
                    title:'运行状态控制',
                    collapsible:true,
                    items:[fwztform]
                },{
                    xtype:'fieldset',
                    title:'配置信息',
                    collapsible:true,
                    items:[panel]
                }]
            }


        ]
    });
    tabs.activate(0);

    checkServerStatus();
});

function setHeight() {
    var h = document.body.clientHeight - 8;
    return h;
}

function setWidth() {
    var h = document.body.clientWidth - 300;
    return h;
}

function checkServerStatus() {
    Ext.Ajax.request({
        url:'../../ServerStatusAction_checkServerStatus.action',
        success:function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("0" == reText.msg) {
                Ext.getCmp("serverstatus").setValue("[<font color='red'>未启动</font>] [<a href='javascript:;' style='color:blue;' onclick='openServer()'>启动</a>]")
                Ext.getCmp("runtime").setValue("[<font color='orange'>服务未启动</font>]");
            } else {
                Ext.getCmp("serverstatus").setValue("[<font color='green'>已启动</font>] [<font color='#ff4500'><a href='javascript:;' style='color:blue;' onclick='closeServer()'>停止</a></font>][<a href='javascript:;' style='color:blue;' onclick='reloadServer()'>重启</a>]");
                Ext.getCmp("runtime").setValue("[<font color='orange'>服务正在运行</font>]");
            }
        }
    });
}

function reloadServer() {
    Ext.Ajax.request({
        url:'../../ServerStatusAction_reloadServer.action',
        success:function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("1" == reText.msg) {
                Ext.Msg.alert('提示', "重启VPN服务成功");
            } else {
                Ext.Msg.alert('提示', "重启VPN服务失败");
            }
            checkServerStatus();
        }
    });
}

function closeServer() {
    Ext.Ajax.request({
        url:'../../ServerStatusAction_closeServer.action',
        success:function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("0" == reText.msg) {
                Ext.getCmp("serverstatus").setValue("[<font color='red'>未启动</font>] [<a href='javascript;' style='color:blue;' onclick='openServer()'>启动</a>]");
                Ext.Msg.alert('提示', "停止VPN服务成功");
            } else {
                Ext.Msg.alert('提示', "停止VPN服务失败");
            }
            checkServerStatus();
        }
    });
}

function openServer() {
    Ext.Ajax.request({
        url:'../../ServerStatusAction_openServer.action',
        success:function (response, result) {
            var reText = Ext.util.JSON.decode(response.responseText);
            if ("1" == reText.msg) {
                Ext.getCmp("serverstatus").setValue("[<font color='green'>已启动</font>] [<font color='#ff4500'><a href='javascript:;' style='color:blue;' onclick='closeServer()'>停止</a></font>]");
                Ext.Msg.alert('提示', "开启VPN服务成功");
            } else {
                Ext.Msg.alert('提示', "开启VPN服务失败");
            }
            checkServerStatus();
        }
    });
}


