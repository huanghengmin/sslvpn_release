
Ext.onReady(function(){

    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name:'dynamic_ip',mapping:'dynamic_ip'} ,
        {name:'dynamic_ip_mask',mapping:'dynamic_ip_mask'} ,
        //{name:'static_ip',mapping:'static_ip'} ,
        //{name:'static_ip_mask',mapping:'static_ip_mask'} ,
        {name:'group_default_net',mapping:'group_default_net'} ,
        {name:'sub_net',mapping:'sub_net'} ,
        {name:'allow_all_access_sub_net',mapping:'allow_all_access_sub_net'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../ServerSetAction_findSetConfig.action"
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
        var dynamic_ip = store.getAt(0).get('dynamic_ip');
        var dynamic_ip_mask = store.getAt(0).get('dynamic_ip_mask');
        //var static_ip = store.getAt(0).get('static_ip');
        //var static_ip_mask = store.getAt(0).get('static_ip_mask');
//        var group_default_net = store.getAt(0).get('group_default_net');
//        var sub_net = store.getAt(0).get('sub_net');
        var allow_all_access_sub_net = store.getAt(0).get('allow_all_access_sub_net');
        Ext.getCmp('server.dynamic.ip').setValue(dynamic_ip);
        Ext.getCmp('server.dynamic.ip.mask').setValue(dynamic_ip_mask);
        Ext.getCmp('server.allow_all_access_sub_net').setValue(allow_all_access_sub_net);
        //Ext.getCmp('server.static.ip').setValue(static_ip);
        //Ext.getCmp('server.static.ip.mask').setValue(static_ip_mask);
//        Ext.getCmp('server.group_default_net').setValue(group_default_net);
//        Ext.getCmp('server.sub_net').setValue(sub_net);

    });

    <!--server-->
    var server_start =0;
    var server_pageSize = 5;
    var server_toolbar = new Ext.Toolbar({
        plain:true,
        width:350,
        height:30,
        items:[
            {
                id:'add_server.info',
                xtype:'button',
                text:'添加组默认网络',
                iconCls:'add',
                handler:function () {
                    addGroupNet(server_grid_panel, server_store);
                }
            }
        ]
    });
    var server_record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'net',			mapping:'net'},
        {name:'net_mask',			mapping:'net_mask'}
    ]);
    var server_proxy = new Ext.data.HttpProxy({
        url:"../../GroupNetAction_find.action"
    });
    var server_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
         id:'id'
    },server_record);
    var server_store = new Ext.data.GroupingStore({
        id:"group.store.info",
        proxy : server_proxy,
        reader : server_reader
    });
    var server_boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var server_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var server_colM = new Ext.grid.ColumnModel([
        server_boxM,
        server_rowNumber,
        {header:"网络地址",			dataIndex:"net",  align:'center',sortable:true,menuDisabled:true},
        {header:"子网掩码",		dataIndex:"net_mask",      align:'center',sortable:true,menuDisabled:true},
        {header:'操作标记',		dataIndex:"flag",	  align:'center',sortable:true,menuDisabled:true, renderer:show_server_flag,	width:100}
    ]);
    var server_page_toolbar = new Ext.PagingToolbar({
        pageSize : server_pageSize,
        store:server_store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var server_grid_panel = new Ext.grid.GridPanel({
        id:'server.grid.info',
        plain:true,
        height:Ext.getBody().getHeight()/3,
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:server_colM,
        sm:server_boxM,
        store:server_store,
        tbar : server_toolbar,/*
         listeners:{
         render:function(){
//         tbar.render(this.tbar);
         }
         },*/
        bbar:server_page_toolbar
    });




    <!--key -->
    var key_start = 0;
    var key_pageSize = 5;
    var key_toolbar = new Ext.Toolbar({
        plain: true,
        width: 350,
        height: 30,
        items: [
            {
                id: 'add_key.info',
                xtype: 'button',
                text: '添加私有子网',
                iconCls: 'add',
                handler: function () {
                    addPrivateNet(key_grid_panel, key_store);
                }
            },/* {
                xtype: 'checkbox',
                boxLabel: '允许客户端访问所有私有子网',
                inputValue: 1,
                name: 'allow_all_access_sub_net',
                emptyText: '允许客户端访问所有私有子网',
                id: "server.allow_all_access_sub_net",
                allowBlank: false,
                blankText: "all access"
            }*/
            new Ext.form.Checkbox({
                inputValue: 1,
                boxLabel: '允许客户端访问所有私有子网',
                fieldLabel: '允许客户端访问所有私有子网',
                id: "server.allow_all_access_sub_net",
                regexText: '允许客户端访问所有私有子网',
                name: 'allow_all_access_sub_net',
                blankText: "允许客户端访问所有私有子网"
            })
        ]
    });

   /* var key_toolbar_render = new Ext.Toolbar({
        plain: true,
        //width: 350,
        //height: 30,
        items: [
            {
                xtype: 'checkbox',
                boxLabel: '允许客户端访问所有私有子网',
                inputValue:1,
                name: 'allow_all_access_sub_net',
                emptyText: '允许客户端访问所有私有子网',
                id: "server.allow_all_access_sub_net",
                allowBlank: false,
                blankText: "all access"
            }
        ]
    });*/

    var key_record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'net', mapping: 'net'},
        {name: 'net_mask', mapping: 'net_mask'}
    ]);
    var key_proxy = new Ext.data.HttpProxy({
        url: "../../PrivateNetAction_find.action"
    });
    var key_reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, key_record);
    var key_store = new Ext.data.GroupingStore({
        id: "private.store.info",
        proxy: key_proxy,
        reader: key_reader
    });
    //var key_boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var key_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var key_colM = new Ext.grid.ColumnModel([
        //key_boxM,
        key_rowNumber,
        {header: "网络地址", dataIndex: "net", align: 'center', sortable: true, menuDisabled: true},
        {header: "子网掩码", dataIndex: "net_mask", align: 'center', sortable: true, menuDisabled: true},
        {header: '操作标记', dataIndex: "flag", align: 'center', sortable: true, menuDisabled: true, renderer: show_key_flag, width: 100}
    ]);
    var key_page_toolbar = new Ext.PagingToolbar({
        pageSize: key_pageSize,
        store: key_store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var key_grid_panel = new Ext.grid.GridPanel({
        id: 'key.grid.info',
        plain: true,
        height: Ext.getBody().getHeight() / 3,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: key_colM,
        //sm: key_boxM,
        store: key_store,
        tbar: key_toolbar/*,
        listeners: {
            render: function () {
                key_toolbar_render.render(this.tbar);
            }
        }*/,/*
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         },*/
        bbar: key_page_toolbar
    });





    var formPanel = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:150,
        defaultType:'textfield',
        defaults: {
            anchor: '100%',
            allowBlank:false,
            blankText:'该项不能为空!'/*,
            labelStyle: 'padding-left:4px;'*/
        },
        items:[
           /* {
                xtype: 'fieldset',
                collapsible: true,
                title: 'IP地址网络配置',
                defaultType: 'textfield',
                labelWidth: 150,
                defaults: {
                    anchor: '100%',
                    allowBlank: false,
                    blankText: '该项不能为空!'*//*,
                    labelStyle: 'padding-left:4px;'*//*
                },
                items: [*/
                    {
                      /*  layout: 'column',
                        xtype: 'fieldset',
//                collapsible:true,
                        border: false,
                        items: [

                            {*/
                                //columnWidth: .4,
                                xtype: 'fieldset',
                                collapsible: true,
                                title: '动态IP地址网络',
                                defaultType: 'textfield',
                                items: [
                                    {
                                        labelAlign: 'right',
                                        anchor: '85%',
                                        xtype: 'textfield',
                                        id: 'server.dynamic.ip',
                                        name: 'dynamic_ip',
                                        regex: /^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                                        regexText: '这个不是IP',
                                        fieldLabel: '网络地址'
                                    },
                                    {
                                        fieldLabel: '子网掩码',
                                        anchor: '85%',
                                        xtype: 'textfield',
                                        id: 'server.dynamic.ip.mask',
                                        regex: /^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                                        name: 'dynamic_ip_mask'
                                    }
                                ]
                        /* },
                            {
                                xtype: 'displayfield',
                                columnWidth: .2
                            },
                            {
                                xtype: 'fieldset',
                                columnWidth: .4,
                                labelAlign: 'right',
                                collapsible: true,
                                title: '静态IP地址网络(可选)',
                                defaultType: 'textfield',
                                items: [
                                    {
                                        xtype: 'textfield',
                                        name: 'static_ip',
                                        id: 'server.static.ip',
                                        regex: /^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                                        regexText: '这个不是IP',
                                        fieldLabel: '网络地址'
                                    },
                                    {
                                        fieldLabel: '子网掩码',
                                        xtype: 'textfield',
                                        id: 'server.static.ip.mask',
                                        regex: /^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                                        name: 'static_ip_mask'
                                    }
                                ]
                            }*/
                        //]
                    //}
                //]
            },
          /*  {
            xtype: 'fieldset',
            collapsible:true,
            title: '组默认的IP地址网络（可选）',
            defaultType: 'textfield',
            labelWidth:150,
            defaults: {
                anchor: '100%',
                allowBlank:false,
                blankText:'该项不能为空!',
                labelStyle: 'padding-left:4px;'
            },
            items: [server_grid_panel]
            },*/
           /* {
            xtype: 'fieldset',
            collapsible:true,
            title: '可访问子网',
            defaultType: 'textfield',
            labelWidth:230,
            defaults: {
                anchor: '100%',
                allowBlank:false,
                blankText:'该项不能为空!',
                labelStyle: 'padding-left:4px;'
            },
            items: [
              *//*  new Ext.form.RadioGroup({
                fieldLabel : "允许访问子网",    //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
//                hideLabel : true,   //隐藏RadioGroup标签
                layout: 'anchor',
                    defaults: {
                        anchor: '100%',
                        allowBlank:false,
                        blankText:'该项不能为空!',
                        labelStyle: 'padding-left:4px;'
                    },
                columns: 1,
                collapsible: true,
                collapsed: true,
                items : [
                    new Ext.form.Radio({                          //三个必须项
                        checked : true,                       //设置当前为选中状态,仅且一个为选中.
                        boxLabel : "不允许",        //Radio标签
                        name : "access_sub_net",                   //用于form提交时传送的参数名
                        inputValue : "不允许",              //提交时传送的参数值
                        listeners : {
                            check : function(checkbox, checked) {        //选中时,调用的事件
                                if (checked) {

                                }
                            }
                        }
                    }),
                    new Ext.form.Radio({            //以上相同
                        boxLabel : "允许,使用NAT",
                        name : "access_sub_net",
                        inputValue : "允许,使用NAT",
                        listeners : {
                            check : function(checkbox, checked) {
                                if (checked) {
                                }
                            }
                        }
                    }),
                    new Ext.form.Radio({
                        boxLabel : "允许,使用路由",
                        name : "access_sub_net",
                        inputValue : "允许,使用路由",
                        listeners : {
                            check : function(checkbox, checked) {
                                if (checked) {
                                }
                            }
                        }
                    })]
            }),*/
                {
                xtype:'fieldset',
                collapsible:true,
                title:"子网配置",
                labelWidth:100,
//                    border:true,
                defaults: {
                    anchor: '100%',
                    allowBlank:false,
                    blankText:'该项不能为空!'/*,
                    labelStyle: 'padding-left:4px;'*/
                },
                    items:[
                        {
                            xtype:'fieldset',
                            labelWidth:100,
                            defaultType: 'textfield',
                            border:false,
                            layout: 'form',
                            items :[ key_grid_panel]
                        }
                    ]

            }

        ],
        buttons:[
            '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../ServerSetAction_updateSetConfig.action",
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
      /*  defaults: {
            anchor:"%100",
            labelStyle: 'padding-left:4px;'
        },*/
        items:[{
            id:'panel.info',
            xtype:'fieldset',
            collapsible:true,
            title:'基本配置',
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

    server_store.load({
        params:{
            start:server_start,limit:server_pageSize
        }
    });

    key_store.load({
        params:{
            start:key_start,limit:key_pageSize
        }
    });
});



function addGroupNet(grid,store){
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : '网络地址',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                name : 'groupNet.net',
                id:  'groupNet.net',
                allowBlank : false,
                emptyText:"网络地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '子网掩码',
                name : 'groupNet.net_mask',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入子网掩码',
                id:  'groupNet.net_mask',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"新增网络",
        width:500,
        layout:'fit',
        height:180,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'新增',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../GroupNetAction_add.action',
                            timeout: 20*60*1000,
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        store.reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
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
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}




function show_server_flag(){
    return String.format(
        '<a id="update_group.info" href="javascript:void(0);" onclick="update_group();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;'+
            '<a id="delete_group.info" href="javascript:void(0);" onclick="delete_group();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    );
}



function update_group(){
    var grid_panel = Ext.getCmp("server.grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : '网络地址',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                name : 'groupNet.net',
                id:  'groupNet.net',
                allowBlank : false,
                value:recode.get('net'),
                emptyText:"网络地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '子网掩码',
                value:recode.get('net_mask'),
                name : 'groupNet.net_mask',
                id:  'groupNet.net_mask',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入子网掩码',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"修改网络",
        width:500,
        layout:'fit',
        height:180,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'修改',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../GroupNetAction_modify.action',
                            timeout: 20*60*1000,
                            params:{id:recode.get('id')},
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        grid_panel.getStore().reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
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
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function delete_group(){
    var grid_panel = Ext.getCmp("server.grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确定删除这条记录？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../GroupNetAction_remove.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
                    },
                    failure : function(r,o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}

function show_key_flag(){
    return String.format(
            '<a id="update_private.info" href="javascript:void(0);" onclick="update_private();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;'+
            '<a id="delete_private.info" href="javascript:void(0);" onclick="delete_private();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    );
}

function addPrivateNet(grid,store){
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : '网络地址',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                name : 'privateNet.net',
                id:  'privateNet.net',
                allowBlank : false,
                emptyText:"网络地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '子网掩码',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入子网掩码',
                name : 'privateNet.net_mask',
                id:  'privateNet.net_mask',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"新增网络",
        width:500,
        layout:'fit',
        height:180,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'新增',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../PrivateNetAction_add.action',
                            timeout: 20*60*1000,
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        store.reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
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
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}


function update_private(){
    var grid_panel = Ext.getCmp("key.grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : '网络地址',
                name : 'privateNet.net',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                id:  'privateNet.net',
                value:recode.get('net'),
                allowBlank : false,
                emptyText:"网络地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '子网掩码',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入子网掩码',
                value:recode.get('net_mask'),
                name : 'privateNet.net_mask',
                id:  'privateNet.net_mask',
                allowBlank : false,
                emptyText:"子网掩码",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"修改网络",
        width:500,
        layout:'fit',
        height:180,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'修改',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../PrivateNetAction_modify.action',
                            timeout: 20*60*1000,
                            params:{id:recode.get('id')},
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        grid_panel.getStore().reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
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
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function delete_private(){
    var grid_panel = Ext.getCmp("key.grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确定删除这条记录？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../PrivateNetAction_remove.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
                    },
                    failure : function(r,o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}



