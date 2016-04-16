Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var record = new Ext.data.Record.create([
        {name: 'control_url', mapping: 'control_url'},
        {name: 'proxy_port', mapping: 'proxy_port'},
        {name: 'bs_proxy_ip', mapping: 'bs_proxy_ip'},
        {name: 'bs_proxy_port', mapping: 'bs_proxy_port'},
        {name: 'status', mapping: 'status'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../AccessControl_selectControl.action"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    store.load();
    store.on('load', function () {
        var control_url = store.getAt(0).get('control_url');
        var status = store.getAt(0).get('status');
        var proxy_port = store.getAt(0).get('proxy_port');
        var bs_proxy_ip = store.getAt(0).get('bs_proxy_ip');
        var bs_proxy_port = store.getAt(0).get('bs_proxy_port');
        Ext.getCmp('access_control.url').setValue(control_url);
        Ext.getCmp('access_control.status').setValue(status);
        Ext.getCmp('access_control.proxy_port').setValue(proxy_port);
        Ext.getCmp('access_control.bs_proxy_ip').setValue(bs_proxy_ip);
        Ext.getCmp('access_control.bs_proxy_port').setValue(bs_proxy_port);
    });

    var data4 = [[0, '否'], [1, '是']];
    var store4 = new Ext.data.SimpleStore({
        fields: ['value', 'name'], data: data4
    });

    var formPanel = new Ext.form.FormPanel({
        plain: true,
//        width:500,
        labelAlign: 'right',
        labelWidth: 200,
        defaultType: 'textfield',
        defaults: {
            width: 500,
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            /* new Ext.form.TextField({
             fieldLabel:'CA 发布证书IP',
             name:'ip',
             regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
             regexText:'请输入正确的IP地址',
             id:"ca.ip",
             allowBlank:false,
             blankText:"CA 发布证书IP"
             }),
             new Ext.form.TextField({
             fieldLabel:'CA 发布证书端口',
             name:'port',
             id:"ca.port",
             regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
             regexText:'请输入正确的端口',
             allowBlank:false,
             blankText:"CA 发布证书端口"
             }),*/
            new Ext.form.ComboBox({
                fieldLabel: '启用访问控制',
                hiddenName: 'status',
                id: 'access_control.status',
                store: store4,
                valueField: "value",
                displayField: "name",
                typeAhead: true,
                mode: "local",
                forceSelection: true,
                triggerAction: "all",
                allowBlank: false
            }),
            new Ext.form.TextField({
                fieldLabel: '访问控制地址',
                name: 'control_url',
                id: "access_control.url",
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: '代理端口',
                name: 'proxy_port',
                id: "access_control.proxy_port",
                regex: /^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText: '请输入正确的端口',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: 'BS服务IP地址',
                name: 'bs_proxy_ip',
                id: "access_control.bs_proxy_ip",
                regex: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText: '请输入正确的IP地址',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: 'BS服务端口',
                name: 'bs_proxy_port',
                id: "access_control.bs_proxy_port",
                regex: /^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText: '请输入正确的端口',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            })
            /*,
             new Ext.form.TextField({
             fieldLabel : 'LDAP 连接端口',
             id:"ca.ldap.port",
             regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
             regexText:'请输入正确的端口',
             name : 'ldapPort',
             allowBlank : false,
             blankText : "不能为空，请正确填写"
             }),
             new Ext.form.TextField({
             fieldLabel:'LDAP 发布DN',
             name:'ldapBaseDN',
             id:"ca.ldap.ldapBaseDN",
             allowBlank:false,
             blankText:"LDAP 发布DN"
             }),
             new Ext.form.TextField({
             fieldLabel:'LDAP 登陆DN',
             name:'ldapAdmin',
             allowBlank:false,
             id:"ca.ldap.ldapAdmin",
             blankText:"LDAP 登陆DN"
             }) ,
             new Ext.form.TextField({
             fieldLabel:'LDAP 登陆密码',
             name:'ldapAdminPass',
             id:"ca.ldap.ldapAdminPass",
             inputType:'password',
             allowBlank:false,
             blankText:"LDAP 登陆密码"
             })*/
            /* ,
             new Ext.form.ComboBox({
             fieldLabel:'隶属地区',
             mode : 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载
             // 则为remote.默认值为：remote
             border : true,
             frame : true,
             pageSize : 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
             // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
             width : 250,
             emptyText : '可输入姓名或警号查询，支持模糊查询',
             //                title : '省份',
             triggerAction : 'all',
             displayField : 'orgname',
             valueField : 'orgcode',
             //                queryDelay : 600,
             id:'hzih.ca.addCa.province',
             //                typeAhead : true,
             editable : false,
             store : province_store,
             //                selectOnFocus : true,
             listeners : {
             select:function(){
             var value = this.getValue();
             Ext.getCmp("hzih.ca.addCa.city").clearValue();
             city_store.proxy   = new Ext.data.HttpProxy ({
             url:"../../OrgCodeAction_findCityByProvinceCode.action?orgcode="+value
             })
             city_store.load(0,10);
             }*/
            /*,
             render:function(){
             Ext.getCmp("hzih.ca.addCa.province").getEl().up('.x-form-item').setDisplayed(false)
             }*/
            /*
             }
             }),
             new Ext.form.ComboBox({
             fieldLabel : '市',
             emptyText:'请选择所在市区',
             id: 'hzih.ca.addCa.city',
             hiddenName : 'hzihCa.hzihcity',
             triggerAction : "all",// 是否开启自动查询功能
             store : city_store,// 定义数据源
             displayField : "orgname",// 关联某一个逻辑列名作为显示值
             valueField : "orgcode",// 关联某一个逻辑列名作为实际值
             mode : "remote",// 如果数据来自本地用local 如果来自远程用remote默认为remote
             emptyText : "请选择",// 没有选择时候的默认值
             pageSize : 10//, 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
             // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
             //                name:'hzihCa.hzihcity'*/
            /*,*/
            /*
             //allowBlank:false,
             //blankText:"市",
             //                listeners:{
             //                    render:function(){
             //                        Ext.getCmp("hzih.ca.addCa.city").getEl().up('.x-form-item').setDisplayed(false)
             //                    }
             //                }
             })*/
        ],
        buttons: [
            // '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../AccessControl_saveConfig.action",
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在连接...',
                            success: function () {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: '保存成功,点击返回页面!',
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            },
                            failure: function () {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: '保存失败，请与管理员联系!',
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.ERROR,
                                    closable: false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }/*,
             {
             text:'测试ldap连接',
             handler:function () {
             formPanel.getForm().submit({
             url:"../../SysConfig_ldapConnections.action",
             method:'POST',
             waitTitle:'系统提示',
             waitMsg:'正在连接...',
             success:function () {
             Ext.MessageBox.show({
             title:'信息',
             width:250,
             msg:'LDAP服务器已连通!',
             buttons:Ext.MessageBox.OK,
             buttons:{'ok':'确定'},
             icon:Ext.MessageBox.INFO
             });
             },
             failure:function () {
             Ext.MessageBox.show({
             title:'信息',
             width:250,
             msg:'LDAP服务器连通失败!',
             buttons:Ext.MessageBox.OK,
             buttons:{'ok':'确定'},
             icon:Ext.MessageBox.ERROR,
             closable:false
             });
             }
             });
             }
             }*//* ,{
             text:'测试echo.sh脚本',
             handler:function () {
             formPanel.getForm().submit({
             url:"../../TestAction_test.action",
             method:'POST',
             waitTitle:'系统提示',
             waitMsg:'正在连接...',
             success:function () {
             Ext.MessageBox.show({
             title:'信息',
             width:250,
             msg:'测试成功!',
             buttons:Ext.MessageBox.OK,
             buttons:{'ok':'确定'},
             icon:Ext.MessageBox.INFO
             });
             },
             failure:function () {
             Ext.MessageBox.show({
             title:'信息',
             width:250,
             msg:'测试失败!',
             buttons:Ext.MessageBox.OK,
             buttons:{'ok':'确定'},
             icon:Ext.MessageBox.ERROR,
             closable:false
             });
             }
             });
             }
             }*/
        ]
    });

    var panel = new Ext.Panel({
        plain: true,
//        width:800,
        border: false,
        items: [{
            id: 'panel.info',
            xtype: 'fieldset',
            title: '系统配置',
//            width:600,
            items: [formPanel]
        }]
    });
    new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        autoScroll: true,
        items: [{
            frame: true,
            autoScroll: true,
            items: [panel]
        }]
    });

});


