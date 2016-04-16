Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

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
                text: '添加可访问子网',
                iconCls: 'add',
                handler: function () {
                    addPrivateNet(key_grid_panel, key_store);
                }
            },
            {
                xtype: 'checkbox',
                boxLabel: '允许所有用户访问',
                name: 'allow_all_access_sub_net',
                emptyText: '允许所有用户访问',
                id: "server.allow_all_access_sub_net",
                allowBlank: false,
                blankText: "all access"
            }
        ]
    });
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
    var key_boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var key_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var key_colM = new Ext.grid.ColumnModel([
        key_boxM,
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
//        height: Ext.getBody().getHeight() / 3,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: key_colM,
        sm: key_boxM,
        store: key_store,
        tbar: key_toolbar, /*
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         },*/
        bbar: key_page_toolbar
    });

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [key_grid_panel]
    });
});

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