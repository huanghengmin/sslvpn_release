Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name:'client_to_client', mapping:'client_to_client'} ,
        {name:'allow_num_connecting', mapping:'allow_num_connecting'} ,
        {name:'refresh_connecting', mapping:'refresh_connecting'},
        {name:'refresh_num_connecting', mapping:'refresh_num_connecting'},
        {name:'support_lzo', mapping:'support_lzo'},
        {name:'traffic', mapping:'traffic'} ,
        {name:'allowed_ping', mapping:'allowed_ping'},
        {name:'dns_type', mapping:'dns_type'},
        {name:'default_domain_suffix', mapping:'default_domain_suffix'},
        {name:'first', mapping:'first'},
        {name:'second', mapping:'second'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../AdvancedAction_findConfig.action"
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
    store.on('load', function () {
        var client_to_client = store.getAt(0).get('client_to_client');
        var allow_num_connecting = store.getAt(0).get('allow_num_connecting');
        var support_lzo = store.getAt(0).get('support_lzo');
        var refresh_num_connecting = store.getAt(0).get('refresh_num_connecting');
        var refresh_connecting = store.getAt(0).get('refresh_connecting');
        var traffic = store.getAt(0).get('traffic');
        var allowed_ping = store.getAt(0).get('allowed_ping');

        var dns_type = store.getAt(0).get('dns_type');
        var default_domain_suffix = store.getAt(0).get('default_domain_suffix');
        var first = store.getAt(0).get('first');
        var second = store.getAt(0).get('second');

        Ext.getCmp('server.client_to_client').setValue(client_to_client);
        Ext.getCmp('server.allow_num_connecting').setValue(allow_num_connecting);
        Ext.getCmp('server.refresh_num_connecting').setValue(refresh_num_connecting);
        Ext.getCmp('server.refresh_connecting').setValue(refresh_connecting);
        Ext.getCmp('server.support_lzo').setValue(support_lzo);
        Ext.getCmp('server.traffic').setValue(traffic);
        Ext.getCmp('server.allowed_ping').setValue(allowed_ping);

        Ext.getCmp('server.dns_type').setValue(dns_type);
        Ext.getCmp('server.default_domain_suffix').setValue(default_domain_suffix);
        Ext.getCmp('server.first_dns').setValue(first);
        Ext.getCmp('server.second_dns').setValue(second);
    });
    var formPanel = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:200,
        defaultType:'textfield',
        defaults:{
//            width:250,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[
            /*{
                xtype:'fieldset',
                title:'客户端通信',
                collapsible:true,
                *//*    defaults:{
                 labelWidth:89
                 },*//*
                items:[*/
//                    {
//                        xtype:'fieldset',
//                        collapsible:true,
//                        title:'客户端相互通信',
//                        items:[
                    {
                        xtype:'fieldset',
                        title:'是否允许客户端相互通信?',
                        collapsible:true,
                        items:[
                            new Ext.form.RadioGroup({
                                id:'server.client_to_client',
//                                        fieldLabel : "协议",    //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
//                hideLabel : true,   //隐藏RadioGroup标签
                                layout:'anchor',
                                defaults:{
                                    anchor:'100%',
                                    labelStyle:'padding-left:4px;'
                                },
                                columns:1,
                                collapsible:true,
                                collapsed:true,
                                items:[
                                    new Ext.form.Radio({                          //三个必须项
                                        checked:true, //设置当前为选中状态,仅且一个为选中.
                                        boxLabel:"是(允许客户端相互通信)", //Radio标签
                                        name:"client_to_client", //用于form提交时传送的参数名
                                        inputValue:1, //提交时传送的参数值
                                        listeners:{
                                            check:function (checkbox, checked) {        //选中时,调用的事件
                                                if (checked) {

                                                }
                                            }
                                        }
                                    }),
                                    new Ext.form.Radio({            //以上相同
                                        boxLabel:"否(除非明确允许用户权限)",
                                        name:"client_to_client",
                                        inputValue:0,
                                        listeners:{
                                            check:function (checkbox, checked) {
                                                if (checked) {

                                                }
                                            }
                                        }
                                    })/*,
                                     {
                                     xtype: 'checkbox',
                                     name: 'allow_admin_accessAll',
                                     boxLabel: '允许管理访问所有VPN客户端ip地址',
                                     hideLabel: true,
                                     checked: false
                                     }*/
                                ]
                            })
                        ]
//                    }
//                        ]
//                    }
//                ]
            },
           /* {
                xtype:'fieldset',
                title:'每个用户允许多个会话',
//                hidden:true,
                collapsible:true,
                defaults:{
                 labelWidth:89
                 },
                items:[*/
                    {
                        xtype:'fieldset',
                        hidden:true,
                        title:'是否允许用户发起多个连接会话?',
                        collapsible:true,
                        items:[

                            {
                                xtype:'checkbox',
                                name:'allow_num_connecting',
                                id:'server.allow_num_connecting',
                                boxLabel:'允许(静态IP配置无效)',
                                hideLabel:true
                            }
                        ]
//                    }
//                ]
            },
           /* {
                xtype:'fieldset',
                title:'客户端连接超时时间配置',
                collapsible:true,
                *//*    defaults:{
                 labelWidth:89
                 },*//*
                items:[*/
                    {
                        xtype:'fieldset',
                        title:'客户端定期向服务器发送心跳包,当超过指定时间未接收到心跳包表示用户已下线',
                        collapsible:true,
                        items:[
                            {
                                xtype:'textfield',
                                name:'refresh_connecting',
                                id:'server.refresh_connecting',
                                fieldLabel:'客户端检测间隔(秒)'
                            },
                            {
                                xtype:'textfield',
                                name:'refresh_num_connecting',
                                id:'server.refresh_num_connecting',
                                fieldLabel:'客户端超时时间(秒)'
                            }
                        ]
//                    }
//                ]
            },
           /* {
                xtype:'fieldset',
                title:'数据压缩传输',
                collapsible:true,
                *//*    defaults:{
                 labelWidth:89
                 },*//*
                items:[*/
                    {
                        xtype:'fieldset',
                        title:'是否支持客户端数据压缩传输?',
                        collapsible:true,
                        items:[
                            {
                                xtype:'checkbox',
                                name:'support_lzo',
                                id:'server.support_lzo',
                                boxLabel:'支持交互数据压缩传输',
//                                inputValue:1,
                                hideLabel:true
                            }
                        ]
//                    }
//                ]
            },
            {
                xtype:'fieldset',
                title:'客户端网关配置(所有流量通过服务器传输)',
                collapsible:true,
                labelWidth:250,
                /*defaults:{
                 labelWidth:250
                 },*/
                items:[
                    new Ext.form.RadioGroup({
                        id:'server.traffic',
                        fieldLabel:"客户端网关", //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
//                hideLabel : true,   //隐藏RadioGroup标签
//                    layout: 'anchor',
                        defaults:{
                            anchor:'100%',
                            labelStyle:'padding-left:20px;'
                        },
                        columns:5,
                        collapsible:true,
                        collapsed:true,
                        items:[
                            new Ext.form.Radio({                          //三个必须项
                                checked:true, //设置当前为选中状态,仅且一个为选中.
                                boxLabel:"是", //Radio标签
                                name:"traffic", //用于form提交时传送的参数名
                                inputValue:1, //提交时传送的参数值
                                listeners:{
                                    check:function (checkbox, checked) {        //选中时,调用的事件
                                        if (checked) {
                                        }
                                    }
                                }
                            }),
                            new Ext.form.Radio({            //以上相同
                                boxLabel:"否",
                                name:"traffic",
                                inputValue:0,
                                listeners:{
                                    check:function (checkbox, checked) {
                                        if (checked) {
                                        }
                                    }
                                }
                            })]
                    }),
                    new Ext.form.RadioGroup({
                        hidden:true,
                        id:'server.allowed_ping',
//                        fieldLabel:"客户端推送互联网DNS", //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
//                hideLabel : true,   //隐藏RadioGroup标签
//                    layout: 'anchor',
                        defaults:{
                            anchor:'100%',
                            labelStyle:'padding-left:20px;'
                        },
                        columns:5,
                        collapsible:true,
                        collapsed:true,
                        items:[
                            new Ext.form.Radio({                          //三个必须项
                                checked:true, //设置当前为选中状态,仅且一个为选中.
                                boxLabel:"是", //Radio标签
                                name:"allowed_ping", //用于form提交时传送的参数名
                                inputValue:1, //提交时传送的参数值
                                listeners:{
                                    check:function (checkbox, checked) {        //选中时,调用的事件
                                        if (checked) {
                                        }
                                    }
                                }
                            }),
                            new Ext.form.Radio({            //以上相同
                                boxLabel:"否",
                                name:"allowed_ping",
                                inputValue:0,
                                listeners:{
                                    check:function (checkbox, checked) {
                                        if (checked) {
                                        }
                                    }
                                }
                            })]
                    })
                ]
            } ,
            {
                xtype:'fieldset',
                collapsible:true,
                title:'客户端DNS设置',
                defaultType:'textfield',
                labelWidth:150,
                defaults:{
                    //                width:250,
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                items:[
                    new Ext.form.RadioGroup({
                        id:'server.dns_type',
//                        fieldLabel:"客户端DNS", //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
                        //                hideLabel : true,   //隐藏RadioGroup标签
                        layout:'anchor',
                        defaults:{
                            anchor:'100%',
                            labelStyle:'padding-left:4px;'
                        },
                        columns:1,
                        collapsible:true,
                        collapsed:true,
                        items:[
                            new Ext.form.Radio({                          //三个必须项
                                checked:true, //设置当前为选中状态,仅且一个为选中.
                                boxLabel:"允许客户端自定义DNS", //Radio标签
                                name:"dns_type", //用于form提交时传送的参数名
                                inputValue:0, //提交时传送的参数值
                                listeners:{
                                    check:function (checkbox, checked) {        //选中时,调用的事件
                                        if (checked) {
                                            var dns_fieldset = Ext.getCmp("dns_fieldset");
                                            if (dns_fieldset.isVisible()) {
                                                dns_fieldset.hide();
                                            }
                                        }
                                    }
                                }
                            }),
                            new Ext.form.Radio({            //以上相同
                                boxLabel:"客户端DNS与服务器一致",
                                name:"dns_type",
                                inputValue:1,
                                listeners:{
                                    check:function (checkbox, checked) {
                                        if (checked) {
                                            var dns_fieldset = Ext.getCmp("dns_fieldset");
                                            if (dns_fieldset.isVisible()) {
                                                dns_fieldset.hide();
                                            }
                                        }
                                    }
                                }
                            }),
                            new Ext.form.Radio({
                                boxLabel:"客户端DNS设定",
                                name:"dns_type",
                                inputValue:2,
                                listeners:{
                                    check:function (checkbox, checked) {
                                        if (checked) {
                                            var dns_fieldset = Ext.getCmp("dns_fieldset");
                                            if (!dns_fieldset.isVisible()) {
                                                dns_fieldset.show();
                                            }
                                        }
                                    }
                                }
                            })]
                    }),
                    {
                        xtype:'fieldset',
                        id:'dns_fieldset',
                        collapsible:true,
                        hidden:true,
                        labelWidth:200,
                        defaultType:'textfield',
                        defaults:{
                            width:200,
//                            allowBlank:false,
                            blankText:'该项不能为空!'
                        },
                        title:'客户端DNS设定',
                        defaultType:'textfield',
                        layout:'form',
                        items:[
                            {
                                fieldLabel:'首选DNS服务器',
                                name:'first_dns',
//                                emptyText:'首选DNS服务器',
                                id:"server.first_dns",
                                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                                regexText:'请输入正确的DNS',
//                                allowBlank:false,
                                blankText:"首选DNS服务器"
                            } ,
                            {
                                fieldLabel:'备用DNS服务器',
                                name:'second_dns',
//                                emptyText:'备用DNS服务器',
                                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                                regexText:'请输入正确的DNS',
                                id:"server.second_dns",
//                                allowBlank:false,
                                blankText:"备用DNS服务器"
                            }
                        ]
                    }
                ]
            },
            {
                xtype:'fieldset',
                title:'交互数据域名后缀(增加安全性)',
                hidden:true,
                collapsible:true,
                defaults:{
                 labelWidth:89
                 },
                items:[
                   /*    {
                     xtype:'fieldset',
                     title:'默认域名后缀',
                     collapsible:true,
                     items:[*/
                    {
                        xtype:'textfield',
                        name:'default_domain_suffix',
                        value:'sslvpn.com',
                        id:'server.default_domain_suffix',
                        fieldLabel:'交互数据域名后缀'
                    }
//                        ]
//                    }
                ]
            }
            /*,
             {
             xtype:'fieldset',
             title:'民营路由子网(可选)',
             collapsible:true,
             *//*    defaults:{
             labelWidth:89
             },*//*
             items:[
             {
             xtype:'fieldset',
             labelWidth:1,
             title:'私人子网，这应该是路由，而不是通过NAT（网络/ 子网掩码，每行一个）列表访问列表:',
             collapsible:true,
             items:[
             {
             xtype: 'textarea',
             //                                fieldLabel:'列表',
             anchor: '100%',
             name: 'private_net'
             }
             ]
             }
             ]
             }*//*,
             {
             xtype:'fieldset',
             title:'WINDOWS网络(可选)',
             collapsible:true,
             labelWidth:300,
             *//*    defaults:{
             labelWidth:89
             },*//*
             items:[
             //                    {
             //                        xtype:'fieldset',
             //                        collapsible:true,
             //                        title:'客户端相互通信',
             //                        items:[
             {
             xtype:'fieldset',
             title:'是否允许客户端相互通信?',
             collapsible:true,
             items:[
             new Ext.form.RadioGroup({
             //                                        fieldLabel : "协议",    //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
             //                hideLabel : true,   //隐藏RadioGroup标签
             layout: 'anchor',
             defaults: {
             anchor: '100%',
             labelStyle: 'padding-left:4px;'
             },
             columns: 1,
             collapsible: true,
             collapsed: true,
             items : [
             new Ext.form.Radio({                          //三个必须项
             checked : true,                       //设置当前为选中状态,仅且一个为选中.
             boxLabel : "请勿更改客户端上的Windows网络设置",        //Radio标签
             name : "set_windows_client",                   //用于form提交时传送的参数名
             inputValue : "no",              //提交时传送的参数值
             listeners : {
             check : function(checkbox, checked) {        //选中时,调用的事件
             if (checked) {

             }
             }
             }
             }),
             new Ext.form.Radio({            //以上相同
             boxLabel : "禁用TCP / IP上的NetBIOS客户",
             name : "set_windows_client",
             inputValue : "no_access",
             listeners : {
             check : function(checkbox, checked) {
             if (checked) {

             }
             }
             }
             }),
             new Ext.form.Radio({            //以上相同
             boxLabel : "通过TCP / IP启用NetBIOS和使用这些Windows客户端的网络设置",
             name : "set_windows_client",
             inputValue : "update_client",
             listeners : {
             check : function(checkbox, checked) {
             if (checked) {

             }
             }
             }
             })
             ]
             }),
             {
             xtype: 'textfield',
             name: 'first_win_server',
             fieldLabel: '主WINS服务器'
             },
             {
             xtype: 'textfield',
             name: 'second_win_server',
             fieldLabel: '次要WINS服务器'
             },
             {
             xtype: 'displayfield',
             //                                name: 'second_win_server',
             fieldLabel: 'NetBIOS通过TCP/IP节点类型'
             },
             new Ext.form.RadioGroup({
             //                                        fieldLabel : "NetBIOS通过TCP/IP节点类型",    //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
             //                hideLabel : true,   //隐藏RadioGroup标签
             layout: 'anchor',
             defaults: {
             anchor: '100%',
             labelStyle: 'padding-left:4px;'
             },
             columns: 1,
             collapsible: true,
             collapsed: true,
             items : [
             new Ext.form.Radio({                          //三个必须项
             checked : true,                       //设置当前为选中状态,仅且一个为选中.
             boxLabel : "b类节点（广播）",        //Radio标签
             name : "net_bios",                   //用于form提交时传送的参数名
             inputValue : "b",              //提交时传送的参数值
             listeners : {
             check : function(checkbox, checked) {        //选中时,调用的事件
             if (checked) {

             }
             }
             }
             }),
             new Ext.form.Radio({            //以上相同
             boxLabel : "p节点（点至点名称查询WINS服务器）",
             name : "net_bios",
             inputValue : "p",
             listeners : {
             check : function(checkbox, checked) {
             if (checked) {

             }
             }
             }
             }),
             new Ext.form.Radio({            //以上相同
             boxLabel : "m节点（广播，然后查询名称服务器）",
             name : "net_bios",
             inputValue : "m",
             listeners : {
             check : function(checkbox, checked) {
             if (checked) {

             }
             }
             }
             }) ,
             new Ext.form.Radio({            //以上相同
             boxLabel : "h节点（查询名称服务器，然后广播）",
             name : "net_bios",
             inputValue : "h",
             listeners : {
             check : function(checkbox, checked) {
             if (checked) {

             }
             }
             }
             })
             ]
             }) ,
             {
             xtype: 'textfield',
             name: 'nbdd_server',
             fieldLabel: 'NBDD服务器'
             },
             {
             xtype: 'textfield',
             name: 'china_id',
             fieldLabel: '国家统计局范围ID'
             }
             ]
             }

             //                        ]
             //                    }
             ]
             } ,
             {
             xtype:'fieldset',
             title:'其他OpenVPN的配置指令（高级）',
             collapsible:true,
             *//*    defaults:{
             labelWidth:89
             },*//*
             items:[
             {
             xtype:'fieldset',
             labelWidth:1,
             title:'服务器配置指令',
             collapsible:true,
             items:[
             {
             xtype: 'textarea',
             //                                fieldLabel:'列表',
             anchor: '100%',
             name: 'server_command'
             }
             ]
             } ,
             {
             xtype:'fieldset',
             labelWidth:1,
             title:'客户端配置指令',
             collapsible:true,
             items:[
             {
             xtype: 'textarea',
             //                                fieldLabel:'列表',
             anchor: '100%',
             name: 'client_command'
             }
             ]
             }
             ]
             }*/
        ],
        buttons:[
            '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../AdvancedAction_updateConfig.action",
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
        autoWidth:true,
        autoHeight:true,
        border:false,
        items:[
             {
             id:'panel.info',
             xtype:'fieldset',
             title:'高级配置',
             items:[formPanel]
             }
        ]
    });
    new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        autoScroll:true,
        items:[
            {
                frame:true,
                autoScroll:true,
                items:[panel]
            }
        ]
    });
});


function hideField(field) {
    field.disable();// for validation
    field.hide();
    field.getEl().up('.x-form-item').setDisplayed(false); // hide label
}

function showField(field) {
    field.enable();
    field.show();
    field.getEl().up('.x-form-item').setDisplayed(true);// show label
}