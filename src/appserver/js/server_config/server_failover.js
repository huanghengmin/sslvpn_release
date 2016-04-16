Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';


    var formPanel = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:120,
        defaultType:'textfield',
        defaults:{
//            width:250,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[
            {
                xtype:'fieldset',
                title:'冗余模型',
                collapsible:true,
                /*    defaults:{
                 labelWidth:89
                 },*/
                items:[
                    {
                        xtype:'fieldset',
                        title:'选择冗余模型',
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
                                        boxLabel : "LAN模式（基于UCARP的故障转移）",        //Radio标签
                                        name : "redundance",                   //用于form提交时传送的参数名
                                        inputValue : "lan",              //提交时传送的参数值
                                        listeners : {
                                            check : function(checkbox, checked) {        //选中时,调用的事件
                                                if (checked) {

                                                }
                                            }
                                        }
                                    }),
                                    new Ext.form.Radio({            //以上相同
                                        boxLabel : "没有冗余",
                                        name : "redundance",
                                        inputValue : "no",
                                        listeners : {
                                            check : function(checkbox, checked) {
                                                if (checked) {

                                                }
                                            }
                                        }
                                    })
                                ]
                            })
                        ]
                    }, {
                        xtype:'fieldset',
                        title:'网络模型(在的LAN冗余模型，虚拟IP地址在局域网上小学和中学的故障转移节点，它必须驻留在同一个局域网之间共享。当LAN模式冗余启用时，所有访问服务器服务将提供通过这个IP地址)',
                        collapsible:true,
                        items:[
                            {
                                xtype:'textfield',
                                fieldLabel:'共享虚拟IP地址'
                            }
                        ]
                    } ,
                    {
                        xtype:'fieldset',
                        title:'主节点(配置故障转移对中的主节点。)',
                        collapsible:true,
                        items:[
                            {
                                xtype:'textfield',
                                fieldLabel:'主机名/ IP'
                            },
                            {
                                xtype:'textfield',
                                fieldLabel:'SSH用户名'
                            },
                            {
                                xtype:'textfield',
                                fieldLabel:'SSH密码（可选）'
                            },
                            {
                                xtype:'textfield',
                                fieldLabel:'SSH端口'
                            }

                        ]
                    },
                    {
                        xtype:'fieldset',
                        title:'二级节点(配置辅助节点故障转移对中。记住辅助节点上安装全新的访问服务器软件包，OVPN初始化运行时，一定要明确指定节点作为辅助节点)',
                        collapsible:true,
                        items:[
                            {
                                xtype:'textfield',
                                fieldLabel:'主机名/ IP'
                            },
                            {
                                xtype:'textfield',
                                fieldLabel:'SSH用户名'
                            },
                            {
                                xtype:'textfield',
                                fieldLabel:'SSH密码（可选）'
                            },
                            {
                                xtype:'textfield',
                                fieldLabel:'SSH端口'
                            }
                        ]
                    } ,
                    {
                        xtype:'fieldset',
                        title:'验证(确认主要及次要节点可以通过SSH和基于DNS的故障切换配置是否正确连接到对方。主要及次要节点时，只能进行验证。验证过程将需要几秒钟才能完成',
                        collapsible:true,
                        buttonAlign:'leaf',
                      /*  items:[
                        ],*/
                        buttons:[
//                            '->',
                            {
                                id:'insert_win.info',
                                text:'验证',
                                handler:function () {
                                    if (formPanel.form.isValid()) {
                                        formPanel.getForm().submit({
                                            url:"../../AndroidConfigAction_strategy.action",
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
                            } ,{
                                id:'reset_win.info',
                                text:'重置字段',
                                handler:function () {
                                    if (formPanel.form.isValid()) {
                                        formPanel.getForm().submit({
                                            url:"../../AndroidConfigAction_strategy.action",
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
                    }
                ]
            }
        ],
        buttons:[
            '->',
            {
                id:'insert_win.info',
                text:'提交完成后重启系统',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../AndroidConfigAction_strategy.action",
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
//        width:800,
        autoWidth:true,
        autoHeight:true,
        border:false,
        items:[
            /* {
             id:'panel.info',
             xtype:'fieldset',
             title:'VPN设置',
             width:730,
             items:[*/formPanel/*]
             }*/
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
