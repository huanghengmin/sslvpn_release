Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name:'timeServerIp', mapping:'timeServerIp'} ,
        {name:'timeServerPort', mapping:'timeServerPort'} ,

        {name:'caServerIp', mapping:'caServerIp'},
        {name:'caServerPort', mapping:'caServerPort'},

        {name:'vpnConnectIp', mapping:'vpnConnectIp'},
        {name:'vpnConnectPort', mapping:'vpnConnectPort'},

        {name:'terminalMonitorServerIp', mapping:'terminalMonitorServerIp'},
        {name:'terminalMonitorServerPort', mapping:'terminalMonitorServerPort'},

        {name:'centerServerIp', mapping:'centerServerIp'},
        {name:'centerServerPort', mapping:'centerServerPort'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../ServicesAction_find.action"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });

    store.load();
    store.on('load',function(){
        var timeServerIp = store.getAt(0).get('timeServerIp');
        var timeServerPort = store.getAt(0).get('timeServerPort');
        var caServerIp = store.getAt(0).get('caServerIp');
        var caServerPort = store.getAt(0).get('caServerPort');
        var vpnConnectIp = store.getAt(0).get('vpnConnectIp');
        var vpnConnectPort = store.getAt(0).get('vpnConnectPort');
        var terminalMonitorServerIp = store.getAt(0).get('terminalMonitorServerIp');
        var terminalMonitorServerPort = store.getAt(0).get('terminalMonitorServerPort');
        var centerServerIp = store.getAt(0).get('centerServerIp');
        var centerServerPort = store.getAt(0).get('centerServerPort');
        Ext.getCmp('services.timeServerIp').setValue(timeServerIp);
        Ext.getCmp('services.timeServerPort').setValue(timeServerPort);
        Ext.getCmp('services.caServerIp').setValue(caServerIp);
        Ext.getCmp('services.caServerPort').setValue(caServerPort);
        Ext.getCmp('services.vpnConnectIp').setValue(vpnConnectIp);
        Ext.getCmp('services.vpnConnectPort').setValue(vpnConnectPort);
        Ext.getCmp('services.terminalMonitorServerIp').setValue(terminalMonitorServerIp);
        Ext.getCmp('services.terminalMonitorServerPort').setValue(terminalMonitorServerPort);
        Ext.getCmp('services.centerServerIp').setValue(centerServerIp);
        Ext.getCmp('services.centerServerPort').setValue(centerServerPort);
    });

    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        layout:'column',
        border:false,
        items:[{
            plain:true,
            columnWidth :.5,
            border:false,
            layout: 'form',
            items:[{
                plain:true,
                labelAlign:'right',
                labelWidth:140,
                defaultType:'textfield',
                border:false,
                layout: 'form',
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[
                    new Ext.form.TextField({
                    fieldLabel : '时间服务器IP',
                    name : 'timeServerIp',
                    id : 'services.timeServerIp',
                    regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                    regexText:'请输入正确的IP地址',
                    allowBlank : false,
                    blankText : "不能为空，请正确填写"
                }),
                    new Ext.form.TextField({
                        fieldLabel : '证书服务器IP',
                        name : 'caServerIp',
                        regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                        regexText:'请输入正确的IP地址',
                        id:"services.caServerIp",
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    }),
                    new Ext.form.TextField({
                        fieldLabel : 'VPN链接IP',
                        name : 'vpnConnectIp',
                        regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                        regexText:'请输入正确的IP地址',
                        id:"services.vpnConnectIp",
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    }),
                    new Ext.form.TextField({
                        fieldLabel : '终端监控服务器IP',
                        name : 'terminalMonitorServerIp',
                        regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                        regexText:'请输入正确的IP地址',
                        id:"services.terminalMonitorServerIp",
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    }),
                    new Ext.form.TextField({
                        fieldLabel : '业务处理服务器IP',
                        name : 'centerServerIp',
                        regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                        regexText:'请输入正确的IP地址',
                        id:"services.centerServerIp",
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    })
                ]
            }]
        },{
                plain:true,
                defaultType:'textfield',
                columnWidth :.5,
                labelAlign:'right',
                labelWidth:140,
                border:false,
                layout: 'form',
                defaults:{
                    width:200,
                    allowBlank:false,
                    blankText:'该项不能为空！'
                },
                items:[
                    new Ext.form.TextField({
                    fieldLabel : '时间服务器端口',
                    id : 'services.timeServerPort',
                    regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                    regexText:'请输入正确的端口',
                    name : 'timeServerPort',
                    allowBlank : false,
                    blankText : "不能为空，请正确填写"
                    }),
                    new Ext.form.TextField({
                        fieldLabel : '证书服务器端口',
                        id : 'services.caServerPort',
                        regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                        regexText:'请输入正确的端口',
                        name : 'caServerPort',
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    }),
                    new Ext.form.TextField({
                        fieldLabel : 'VPN链接端口',
                        id : 'services.vpnConnectPort',
                        regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                        regexText:'请输入正确的端口',
                        name : 'vpnConnectPort',
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    }),
                    new Ext.form.TextField({
                        fieldLabel : '终端监控服务器端口',
                        id : 'services.terminalMonitorServerPort',
                        regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                        regexText:'请输入正确的端口',
                        name : 'terminalMonitorServerPort',
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    }),
                    new Ext.form.TextField({
                        fieldLabel : '业务处理服务器端口',
                        id : 'services.centerServerPort',
                        regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                        regexText:'请输入正确的端口',
                        name : 'centerServerPort',
                        allowBlank : false,
                        blankText : "不能为空，请正确填写"
                    })]
            }],
        buttons:[
            '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../ServicesAction_save.action",
                            method:'POST',
                            waitTitle:'系统提示',
                            waitMsg:'正在连接...',
                            success:function () {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'保存成功,点击返回页面!',
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function () {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'保存失败，请与管理员联系!',
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            buttons:Ext.MessageBox.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }
        ]
    });


    var panel = new Ext.Panel({
        plain:true,
        width:800,
        border:false,
        items:[{
            id:'panel.info',
            xtype:'fieldset',
            title:'服务器信息配置',
            width:780,
            items:[formPanel]
        }]
    });
    new Ext.Viewport({
        layout :'fit',
        renderTo:Ext.getBody(),
        autoScroll:true,
        items:[{
            frame:true,
            autoScroll:true,
            items:[panel]
        }]
    });

});


