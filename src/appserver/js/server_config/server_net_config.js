Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name:'interface',mapping:'interface'} ,
        {name:'protocol',mapping:'protocol'} ,
        {name:'port',mapping:'port'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../NetConfigAction_findConfig.action"
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
        var protocol = store.getAt(0).get('protocol');
        var port = store.getAt(0).get('port');
        Ext.getCmp("server.interface_id").setValue(store.getAt(0).get('interface'));
        Ext.getCmp('server.radio_group').setValue(protocol);
        Ext.getCmp('server.port').setValue(port);
    });


    var interface = new Ext.data.JsonStore({
        fields:[ "eth", "interface" ],
        url:'../../InterfaceManagerAction_readVPNInterfaceCombox.action',
        autoLoad:true,
        root:"rows" ,
        listeners : {
            load : function(store, records, options) {// 读取完数据后设定默认值
                var value =Ext.getCmp("server.interface_id").getValue();
                Ext.getCmp("server.interface_id").setValue(value);
            }
        }
    });



    var grid_start = 0;
    var grid_pageSize = 5;
    var grid_toolbar = new Ext.Toolbar({
        plain: true,
        width: 350,
        height: 30,
        items: [
            {
                id: 'add_key.info',
                xtype: 'button',
                text: '新增负载配置',
                iconCls: 'add',
                handler: function () {
                    add_remote(grid_store);
                }
            }
        ]
    });

    var grid_record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'host', mapping: 'host'},
        {name: 'port', mapping: 'port'}
    ]);
    var grid_proxy = new Ext.data.HttpProxy({
        url: "../../BalancerAction_find.action"
    });
    var grid_reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, grid_record);
    var grid_store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: grid_proxy,
        reader: grid_reader
    });
    var grid_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var grid_colM = new Ext.grid.ColumnModel([
        grid_rowNumber,
        {header: "负载地址", dataIndex: "host", align: 'center', sortable: true, menuDisabled: true},
        {header: "负载端口", dataIndex: "port", align: 'center', sortable: true, menuDisabled: true},
        {header: '操作标记', dataIndex: "flag", align: 'center', sortable: true, menuDisabled: true, renderer: show_flag, width: 100}
    ]);
    var grid_page_toolbar = new Ext.PagingToolbar({
        pageSize: grid_pageSize,
        store: grid_store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        height: Ext.getBody().getHeight() / 3,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: grid_colM,
        store: grid_store,
        tbar: grid_toolbar,
        bbar: grid_page_toolbar
    });

    var formPanel = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:100,
        defaultType:'textfield',
        defaults:{
            anchor:'100%',
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[
            {
                xtype:'fieldset',
                //labelWidth:200,
                collapsible:true,
                defaultType:'textfield',
                defaults:{
                    width:280,
//                    anchor:'35%',
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                title: '网络接口',
                defaultType: 'textfield',
                layout: 'form',
                items :[
                   /* new Ext.form.DisplayField({
                        fieldLabel:'<font color="red">注意</font>',
                        name:'server.msg',
                        id:"msg",
                        value:'<font color="#9acd32">此处的配置信息是服务器内部监听地址,如需要在互联网访问则需要在防火墙或路由器上做相应的端口映射配置!</font>',
                        allowBlank:false
                    }),*/
                    new Ext.form.ComboBox({
                        hiddenName:'interface_box',
                        id:'server.interface_id',
                        fieldLabel:"接口",
                        emptyText:'接口',
                        store:interface,
                        valueField:"eth",
                        displayField:"interface",
                        triggerAction:"all",
                        allowBlank:false
                    })
                ]
            },
            {
                xtype:'fieldset',
                //labelWidth:200,
                collapsible:true,
                defaultType:'textfield',
                defaults:{
                    width:280,
//                    anchor:'35%',
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                title: '协议参数',
                defaultType: 'textfield',
                layout: 'form',
                items :[
                    new Ext.form.RadioGroup({
                        id:'server.radio_group',
                        fieldLabel : "协议",    //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
//                hideLabel : true,   //隐藏RadioGroup标签
                        layout: 'anchor',
                        defaults: {
                            anchor: '100%',
                            labelStyle: 'padding-left:4px;'
                        },
                        columns: 1,
//                collapsible: true,
//                collapsed: true,
                        items : [
                            new Ext.form.Radio({                          //三个必须项
                                checked : true,                       //设置当前为选中状态,仅且一个为选中.
                                boxLabel : "TCP",        //Radio标签
                                name : "protocol",                   //用于form提交时传送的参数名
                                inputValue : "tcp"/*,              //提交时传送的参数值
                                 listeners : {
                                 check : function(checkbox, checked) {        //选中时,调用的事件
                                 if (checked) {
                                 *//*  var tcp_server_num =  Ext.getCmp("tcp_server_num");
                                 if(tcp_server_num.isVisible()){
                                 hideField(tcp_server_num);
                                 }*//*

                                 var tcp_port =  Ext.getCmp("tcp_port");
                                 if(!tcp_port.isVisible()){
                                 showField(tcp_port);
                                 }
                                 //                                    var udp_server_num =  Ext.getCmp("udp_server_num");
                                 //                                    if(udp_server_num.isVisible()){
                                 //                                        hideField(udp_server_num);
                                 //                                    }
                                 var udp_port =  Ext.getCmp("udp_port");
                                 if(udp_port.isVisible()){
                                 hideField(udp_port);
                                 }
                                 }
                                 }
                                 }*/
                            }),
                            new Ext.form.Radio({            //以上相同
                                boxLabel : "UDP",
                                name : "protocol",
                                inputValue : "udp"/*,
                                 listeners : {
                                 check : function(checkbox, checked) {
                                 if (checked) {
                                 *//*  var tcp_server_num =  Ext.getCmp("tcp_server_num");
                                 if(tcp_server_num.isVisible()){
                                 hideField(tcp_server_num);
                                 }*//*

                                 var tcp_port =  Ext.getCmp("tcp_port");
                                 if(tcp_port.isVisible()){
                                 hideField(tcp_port);
                                 }
                                 *//*  var udp_server_num =  Ext.getCmp("udp_server_num");
                                 if(udp_server_num.isVisible()){
                                 hideField(udp_server_num);
                                 }*//*
                                 var udp_port =  Ext.getCmp("udp_port");
                                 if(!udp_port.isVisible()){
                                 showField(udp_port);
                                 }
                                 }
                                 }
                                 }*/
                            })/*,
                             new Ext.form.Radio({
                             boxLabel : "多协议接口配置",
                             name : "protocol",
                             inputValue : "BOTH",
                             listeners : {
                             check : function(checkbox, checked) {
                             if (checked) {
                             var tcp_server_num =  Ext.getCmp("tcp_server_num");
                             if(!tcp_server_num.isVisible()){
                             showField(tcp_server_num);
                             }

                             var tcp_port =  Ext.getCmp("tcp_port");
                             if(!tcp_port.isVisible()){
                             showField(tcp_port);
                             }
                             var udp_server_num =  Ext.getCmp("udp_server_num");
                             if(!udp_server_num.isVisible()){
                             showField(udp_server_num);
                             }
                             var udp_port =  Ext.getCmp("udp_port");
                             if(!udp_port.isVisible()){
                             showField(udp_port);
                             }
                             }
                             }
                             }
                             })*/]
                    }),
                    {
                        fieldLabel:'端口',
                        name:'port',
                        emptyText:'请输入端口号(0~65535)',
                        regexText:'请输入端口号(0~65535)',
                        id:"server.port",
                        allowBlank:false,
                        blankText:"请输入端口号(0~65535)" ,
                        regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                        allowBlank:false,
                        listeners:{
                            blur:function () {
                                var cmp = this;
                                var value = cmp.getValue();
                                if((0<=value&&value<=1024)||value==8000||value ==8080||value == 8443||value>=65535){
                                    Ext.MessageBox.show({
                                        title:'提示',
                                        width:400,
                                        msg:'0-1024端口可能被系统占用,且端口不能为8000,8080,8443出产服务已监听,且端口不能大于65535!',
                                        buttons:Ext.MessageBox.OK,
                                        buttons:{'ok':'确定'},
                                        icon:Ext.MessageBox.INFO,
                                        closable:false,
                                        fn:function(e){
                                            if(e=='ok'){
                                                cmp.setValue('');
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                ]
            },
            {
                xtype:'fieldset',
                collapsible:true,
                title:"负载配置",
                labelWidth:100,
                defaults: {
                    anchor: '100%',
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                items:[
                    {
                        xtype:'fieldset',
                        labelWidth:100,
                        defaultType: 'textfield',
                        border:false,
                        layout: 'form',
                        items :[ grid_panel]
                    }
                ]
            }
            /*{
                xtype:'fieldset',
                labelWidth:200,
                collapsible:true,
                defaultType:'textfield',
                defaults:{
                    width:250,
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                title: '服务协议参数',
                defaultType: 'textfield',
                layout: 'form',
                items :[*/
           /*{
                    fieldLabel:'TCP 服务数量',
                    name:'tcp_server_num',
                    emptyText:'TCP 服务数量',
                    id:"tcp_server_num",
                    allowBlank:false,
                    blankText:"TCP 服务数量"
                } ,*/
          /*   {
                        fieldLabel:'TCP端口号',
                        name:'tcp_port',
                        emptyText:'TCP端口号',
                        id:"tcp_port",
                        allowBlank:false,
                        blankText:"TCP端口号"
                    }   ,*/
         /*  {
                        fieldLabel:'UDP守护进程数量',
                        name:'udp_server_num',
                        emptyText:'UDP守护进程数量',
                        id:"udp_server_num",
                        allowBlank:false,
                        blankText:"UDP守护进程数量"
                    } ,*/
         /* {
                        fieldLabel:'UDP端口号',
                        name:'udp_port',
                        emptyText:'UDP端口号',
                        id:"udp_port",
                        allowBlank:false,
                        blankText:"UDP端口号"
                    }*/
         //]
            //}
        ],
        buttons:[
            '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../NetConfigAction_updateConfig.action",
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
        items:[{
            id:'panel.info',
            xtype:'fieldset',
            collapsible:true,
            title:'服务网络配置',
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

    grid_store.load({
        params:{
            start:grid_start,limit:grid_pageSize
        }
    });
});


function show_flag(){
    return String.format(
        '<a id="update_private.info" href="javascript:void(0);" onclick="update_remote();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="delete_private.info" href="javascript:void(0);" onclick="delete_remote();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    );
}

function add_remote(store){
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
                fieldLabel : '负载地址',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                name : 'balancer.host',
                id:  'balancer.host',
                allowBlank : false,
                emptyText:"负载地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '负载端口',
                name : 'balancer.port',
                id:  'balancer.port',
                allowBlank : false,
                emptyText:'请输入端口号(0~65535)',
                regexText:'请输入端口号(0~65535)',
                allowBlank:false,
                blankText:"请输入端口号(0~65535)" ,
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/
            })
        ]
    });
    var win = new Ext.Window({
        title:"新增负载配置",
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
                            url :'../../BalancerAction_add.action',
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

function update_remote(){
    var grid_panel = Ext.getCmp("grid.info");
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
                fieldLabel : '负载地址',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入正确的网络地址',
                name:  'balancer.host',
                value:recode.get('host'),
                allowBlank : false,
                emptyText:"负载地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                value:recode.get('port'),
                fieldLabel : '负载端口',
                name : 'balancer.port',
                allowBlank : false,
                emptyText:'请输入端口号(0~65535)',
                regexText:'请输入端口号(0~65535)',
                allowBlank:false,
                blankText:"请输入端口号(0~65535)" ,
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/
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
                            url :'../../BalancerAction_modify.action',
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

function delete_remote(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确定删除这条记录？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../BalancerAction_remove.action",
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
