Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 5;
    var toolbar = new Ext.Toolbar({
        plain:true,
        width:350,
        height:30,
        items:[
            {
                id:'add_ca.info',
                xtype:'button',
                text:'添加信任证书链证书',
                iconCls:'add',
                handler:function () {
                    addCa(grid_panel, store);
                }
            },
            {
                id:'author_ca.info',
                xtype:'button',
                text:'信任证书链认证配置',
                iconCls:'add',
                handler:function () {
                    auth_ca(grid_panel, store);
                }
            }
        ]
    });
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'ca_file',			mapping:'ca_file'},
        {name:'ca_name',			mapping:'ca_name'} ,
        {name:'not_before',			mapping:'not_before'} ,
        {name:'not_after',			mapping:'not_after'},
        {name:'cn',			mapping:'cn'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findCaConfig.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"/*,
         id:'id'*/
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });
//    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
//        {header:"证书文件",			dataIndex:"ca_name",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书通用名",			dataIndex:"cn",  align:'center',sortable:true,menuDisabled:true},
        {header:"有效起始日期",			dataIndex:"not_before",  align:'center',sortable:true,menuDisabled:true},
        {header:"有效截止日期",			dataIndex:"not_after",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书内容",		dataIndex:"context",      align:'center',sortable:true,menuDisabled:true,renderer:show_ca},
        {header:'操作',		dataIndex:"flag",	  align:'center',sortable:true,menuDisabled:true, renderer:show_ca_flag,width:100}
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id:'ca.grid.info',
        title:'信任证书链',
        height:Ext.getBody().getHeight()/2,
        plain:true,
//        autoHeight:true,
//        height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
//        sm:boxM,
        store:store,
         tbar : toolbar/*,
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         }*/,
        bbar:page_toolbar
    });

    <!-- server -->
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
                text:'增加网关证书',
                iconCls:'add',
                handler:function () {
                    addServer(server_grid_panel, server_store);
                }
            }
        ]
    });
    var server_record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'server_file',			mapping:'server_file'},
        {name:'server_name',			mapping:'server_name'},
        {name:'not_before',			mapping:'not_before'} ,
        {name:'not_after',			mapping:'not_after'},
        {name:'cn',			mapping:'cn'}
    ]);
    var server_proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findServerConfig.action"
    });
    var server_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"/*,
         id:'id'*/
    },server_record);
    var server_store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : server_proxy,
        reader : server_reader
    });
//    var server_boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var server_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var server_colM = new Ext.grid.ColumnModel([
//        server_boxM,
        server_rowNumber,
//        {header:"证书文件",			dataIndex:"server_name",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书通用名",			dataIndex:"cn",  align:'center',sortable:true,menuDisabled:true},
        {header:"有效起始日期",			dataIndex:"not_before",  align:'center',sortable:true,menuDisabled:true},
        {header:"有效截止日期",			dataIndex:"not_after",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书内容",		dataIndex:"context",      align:'center',sortable:true,menuDisabled:true,renderer:show_server},
        {header:'操作',		dataIndex:"flag",	  align:'center',sortable:true,menuDisabled:true, renderer:show_server_flag,	width:100}
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
//        autoHeight:true,
//        height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:server_colM,
//        sm:server_boxM,
        store:server_store,
        tbar : server_toolbar,/*
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         },*/
        bbar:server_page_toolbar
    });

    <!--pkcs-->
    var pkcs_start =0;
    var pkcs_pageSize = 5;
    var pkcs_toolbar = new Ext.Toolbar({
        plain:true,
        width:350,
        height:30,
        items:[
            {
                id:'add_server.info',
                xtype:'button',
                text:'服务器证书管理'/*,
                iconCls:'add',
                handler:function () {
                    addServer(server_grid_panel, server_store);
                }*/
            }
        ]
    });
    var pkcs_record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'pkcs_file',			mapping:'pkcs_file'},
        {name:'pkcs_name',			mapping:'pkcs_name'} ,
        {name:'not_before',			mapping:'not_before'} ,
        {name:'not_after',			mapping:'not_after'},
        {name:'cn',			mapping:'cn'}
    ]);
    var pkcs_proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findPkcsConfig.action"
    });
    var pkcs_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"/*,
         id:'id'*/
    },pkcs_record);
    var pkcs_store = new Ext.data.GroupingStore({
        id:"pkcs.store.info",
        proxy : pkcs_proxy,
        reader : pkcs_reader
    });
//    var pkcs_boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var pkcs_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var pkcs_colM = new Ext.grid.ColumnModel([
//        pkcs_boxM,
        pkcs_rowNumber,
//        {header:"证书文件",			dataIndex:"pkcs_name",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书通用名",			dataIndex:"cn",  align:'center',sortable:true,menuDisabled:true},
        {header:"有效起始日期",			dataIndex:"not_before",  align:'center',sortable:true,menuDisabled:true},
        {header:"有效截止日期",			dataIndex:"not_after",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书内容",		dataIndex:"context",      align:'center',sortable:true,menuDisabled:true,renderer:show_pkcs},
        {header:'动作',		dataIndex:"flag",	  align:'center',sortable:true,menuDisabled:true, renderer:show_pkcs_flag,	width:100}
    ]);
    var pkcs_page_toolbar = new Ext.PagingToolbar({
        pageSize : pkcs_pageSize,
        store:pkcs_store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var pkcs_grid_panel = new Ext.grid.GridPanel({
        id:'pkcs.grid.info',
        title:'服务器证书',
        plain:true,
        height:Ext.getBody().getHeight()/2,
//        autoHeight:true,
//        height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:pkcs_colM,
//        sm:pkcs_boxM,
        store:pkcs_store,
        /*tbar : pkcs_toolbar,*//*
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         },*/
        bbar:pkcs_page_toolbar
    });

    <!--key -->
    var key_start =0;
    var key_pageSize = 5;
    var key_toolbar = new Ext.Toolbar({
        plain:true,
        width:350,
        height:30,
        items:[
            {
                id:'add_key.info',
                xtype:'button',
                text:'添加私钥',
                iconCls:'add',
                handler:function () {
                    addKey(key_grid_panel, key_store);
                }
            }
        ]
    });
    var key_record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'key_file',			mapping:'key_file'},
        {name:'key_name',			mapping:'key_name'}
    ]);
    var key_proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findServerKeyConfig.action"
    });
    var key_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"/*,
         id:'id'*/
    },key_record);
    var key_store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : key_proxy,
        reader : key_reader
    });
//    var key_boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var key_rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var key_colM = new Ext.grid.ColumnModel([
//        key_boxM,
        key_rowNumber,
        {header:"私钥文件",			dataIndex:"key_name",  align:'center',sortable:true,menuDisabled:true},
//        {header:"私钥内容",		dataIndex:"context",      align:'center',sortable:true,menuDisabled:true,renderer:show_key},
        {header:'动作',		dataIndex:"flag",	  align:'center',sortable:true,menuDisabled:true, renderer:show_key_flag,	width:100}
    ]);
    var key_page_toolbar = new Ext.PagingToolbar({
        pageSize : key_pageSize,
        store:key_store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var key_grid_panel = new Ext.grid.GridPanel({
        id:'key.grid.info',
        plain:true,
        height:Ext.getBody().getHeight()/3,
//        autoHeight:true,
//        height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:key_colM,
//        sm:key_boxM,
        store:key_store,
       tbar : key_toolbar, /*
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         },*/
        bbar:key_page_toolbar
    });
 /*   var formPanel = new Ext.form.FormPanel({
        baseCls:'x-plain',
        labelWidth:120,
        layout:'form',
        fileUpload:true,
        defaultType:'textfield',
        items:[
            {
                xtype:'fieldset',
                labelWidth:200,
                defaultType:'textfield',
                defaults:{
                    width:250,
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                title:'CA证书',
                layout:'form',
                items:[
                    {
                        xtype:'textfield',
                        fieldLabel:'Select CA Bundle file',
                        name:'userfile',
                        inputType:'file',
                        allowBlank:false,
                        blankText:'File can\'t not empty.',
                        anchor:'90%'  // anchor width by percentage
                    }
                ]},
            {
                xtype:'fieldset',
                labelWidth:200,
                defaultType:'textfield',
                defaults:{
//                    width:250,
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                title:'网关证书',
                layout:'form',
                items:[
                    {
                        xtype:'textfield',
                        fieldLabel:'Select Certificate file',
                        name:'userfile',
                        inputType:'file',
                        allowBlank:false,
                        blankText:'File can\'t not empty.',
                        anchor:'90%'  // anchor width by percentage
                    }
                ]},
            {
                xtype:'fieldset',
                labelWidth:200,
                defaults:{
//                    width:250,
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                title:'网关私钥',
                layout:'form',
                items:[
                    {
                        xtype:'textfield',
                        fieldLabel:'Select Private Key file',
                        name:'userfile',
                        inputType:'file',
                        allowBlank:false,
                        blankText:'File can\'t not empty.',
                        anchor:'90%'  // anchor width by percentage
                    }
                ]}*/
/*,
            {
                xtype:'fieldset',
                labelWidth:200,
                defaultType:'textfield',
                defaults:{
//                    width:250,
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                title:'校验网关证书是否为根证书签发证书',
                defaultType:'textfield',
                layout:'form',
                items:[
                    {
                        xtype:'button',
//            fieldLabel: 'File Name',
                        name:'userfile',
                        text:'校验网关证书',
//            inputType: 'file',
//            allowBlank: false,
                        blankText:'File can\'t not empty.'*/
/*,
                     anchor: '90%'  // anchor width by percentage*/
/*
                    }
                ]}*/
/*
        ],
        buttons:[
            '->',
            {
                id:'insert_win.info',
                text:'保存配置',
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
    });*/

    var panel = new Ext.Panel({
        plain:true,
        autoWidth:true,
        autoHeight:true,
        border:false,
        items:[
            {
                id:'certificate.info',
                xtype:'fieldset',
                title:'证书管理',
                items:[{
                    id:'ca.info',
                    xtype:'fieldset',
                    title:'信任证书链管理',
                    height:Ext.getBody().getHeight()/2,
                    items:[grid_panel]
                },
                    {
                        id:'pkcs.info',
                        xtype:'fieldset',
                        title:'服务器证书管理',
                        height:Ext.getBody().getHeight()/2,
                        items:[pkcs_grid_panel]
                    }]
            }
        /*,
         {
             id:'key.info',
             xtype:'fieldset',
             height:Ext.getBody().getHeight()/3,
             title:'私钥配置',
             items:[key_grid_panel]
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

    store.load({
        params:{
            start:start,limit:pageSize
        }
    });

    pkcs_store.load({
        params:{
            start:pkcs_start,limit:pkcs_pageSize
        }
    });

    /*server_store.load({
        params:{
            start:server_start,limit:server_pageSize
        }
    });

    key_store.load({
        params:{
            start:key_start,limit:key_pageSize
        }
    });*/

    Model.add_pfx_server =   function add_pfx_server(){
        var upload_pfx_form = new Ext.form.FormPanel({
            frame:true,
            labelWidth:150,
            labelAlign:'right',
            fileUpload:true,
            border:false,
            defaults : {
                width : 200,
                allowBlank : false,
                blankText : '该项不能为空！'
            },
            items:[{
                id:'serverPfxFile',
                fieldLabel:"(注:格式为PKCS)",
                width:200,
                name : 'serverPfxFile',
                xtype:'textfield',
                inputType:'file',
                allowBlank:false,
                regexText:'(注:格式为PKCS)',
                listeners:{
                    render:function () {
                        Ext.get('serverPfxFile').on("change", function () {
                            var file = this.getValue();
                            var fs = file.split('.');
                            if (fs[fs.length - 1].toLowerCase() != 'p12' && fs[fs.length - 1].toLowerCase() != 'pfx') {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:200,
                                    msg:'上传文件格式不对,请重新选择!',
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false,
                                    fn:function (e) {
                                        if (e == 'ok') {
                                            Ext.getCmp('serverPfxFile').setValue('');
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            },{
                fieldLabel:'证书密码',
                name:'pwd',
                xtype:'textfield',
                width:200,
                allowBlank:true,
                inputType:'password'/*,
                regex:/^\S{4,20}$/*/
            }]
        });
        var win = new Ext.Window({
            title:'更新网关证书',
            width:500,
            height:200,
            layout:'fit',
            modal:true,
            items:[upload_pfx_form],
            bbar:['->',{
                id:'addpfx_win.info',
                text:'更新',
                width:50,
                handler:function(){
                    if (upload_pfx_form.form.isValid()) {
                        upload_pfx_form.getForm().submit({
                            url :'../../PkiAction_update_server_cert.action',
                            timeout: 20*60*1000,
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function() {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'更新成功,点击返回页面!',
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        pkcs_store.reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function() {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'更新失败，请与管理员联系!',
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
                    upload_pfx_form.getForm().reset();
                }
            }]
        }).show();
    }
});

function addCa(grid_panel,store){
    var uploadWarForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:150,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            xtype:'displayfield',
            fieldLabel:'注意',
            value:'上传文件为pem格式证书文件'
        },{
            id:'crtFile',
            name : 'crtFile',
            xtype:'textfield',
            inputType:'file',
            fieldLabel:"导入CA",
            listeners:{
                render:function(){
                    Ext.get('crtFile').on("change",function(){
                        var file = this.getValue();
//                        var fso = new ActiveXObject("Scripting.FileSystemObject");
//                        var f = fso.GetFile(file);
//                        var f = new File(file);
//                        var isSize = true;
//                        if(f.size>1024*1024*2){
//                            alert(f.size+" Bytes");
//                            isSize = false;
//                        }
                        var fs = file.split('.');
                        if(fs[fs.length-1].toLowerCase()=='pem'||fs[fs.length-1].toLowerCase()=='cer'||fs[fs.length-1].toLowerCase()=='crt'){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                                width:300,
                                buttons:{'ok':'确定','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e == 'ok'){
                                        if (uploadWarForm.form.isValid()) {
                                            uploadWarForm.getForm().submit({
                                                url :'../../PkiAction_upload_ca.action',
                                                method :'POST',
                                                waitTitle :'系统提示',
                                                waitMsg :'正在上传,请稍后...',
                                                success : function(form,action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title:'信息',
                                                        width:250,
                                                        msg:msg,
//                                                    animEl:'insert.win.info',
                                                        buttons:{'ok':'确定','no':'取消'},
                                                        icon:Ext.MessageBox.INFO,
                                                        closable:false,
                                                        fn:function(e){
                                                            if(e=='ok'){
                                                                grid_panel.render();
                                                                store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('crtFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:200,
                                                msg:'请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                                buttons:{'ok':'确定'},
                                                icon:Ext.MessageBox.ERROR,
                                                closable:false
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'上传文件格式不对,请重新选择!',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        Ext.getCmp('crtFile').setValue('');
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }]
    });
    var win = new Ext.Window({
        title:'上传CA证书',
        width:400,
        height:150,
        layout:'fit',
        modal:true,
        items:[uploadWarForm],
        bbar:['->',{
//    		id:'insert.win.info',
//    		text:'上传',
//    		handler:function(){
//
//    		}
//    	},{
            text:'关闭',
            handler:function(){
                win.close();
            }
        }]
    }).show();
}

function addServer(server_grid_panel, server_store){
    if(server_store.getCount()>=1){
          Ext.MessageBox.alert("提示","已配置有网关证书!请更改网关证书")
    } else{
        var uploadWarForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:150,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            xtype:'displayfield',
            fieldLabel:"注意",
            value:'上传文件为pem证书证书'
        },{
            id:'serverFile',
            name : 'serverFile',
            xtype:'textfield',
            inputType:'file',
            fieldLabel:"导入网关证书文件",
            listeners:{
                render:function(){
                    Ext.get('serverFile').on("change",function(){
                        var file = this.getValue();
//                        var fso = new ActiveXObject("Scripting.FileSystemObject");
//                        var f = fso.GetFile(file);
//                        var f = new File(file);
//                        var isSize = true;
//                        if(f.size>1024*1024*2){
//                            alert(f.size+" Bytes");
//                            isSize = false;
//                        }
                        var fs = file.split('.');
                        if(fs[fs.length-1].toLowerCase()=='pem'||fs[fs.length-1].toLowerCase()=='cer'||fs[fs.length-1].toLowerCase()=='crt'){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                                width:300,
                                buttons:{'ok':'确定','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e == 'ok'){
                                        if (uploadWarForm.form.isValid()) {
                                            uploadWarForm.getForm().submit({
                                                url :'../../PkiAction_upload_server.action',
                                                method :'POST',
                                                waitTitle :'系统提示',
                                                waitMsg :'正在上传,请稍后...',
                                                success : function(form,action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title:'信息',
                                                        width:250,
                                                        msg:msg,
//                                                    animEl:'insert.win.info',
                                                        buttons:{'ok':'确定','no':'取消'},
                                                        icon:Ext.MessageBox.INFO,
                                                        closable:false,
                                                        fn:function(e){
                                                            if(e=='ok'){
                                                                server_grid_panel.render();
                                                                server_store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('serverFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:200,
                                                msg:'请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                                buttons:{'ok':'确定'},
                                                icon:Ext.MessageBox.ERROR,
                                                closable:false
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'上传文件格式不对,请重新选择!',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        Ext.getCmp('serverFile').setValue('');
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }]
    });
        var win = new Ext.Window({
        title:'上传网关证书文件',
        width:400,
        height:150,
        layout:'fit',
        modal:true,
        items:[uploadWarForm],
        bbar:['->',{
//    		id:'insert.win.info',
//    		text:'上传',
//    		handler:function(){
//
//    		}
//    	},{
            text:'关闭',
            handler:function(){
                win.close();
            }
        }]
    }).show();
    }
}

function addKey(key_grid_panel, key_store){
    if(key_store.getCount()>=1){
        Ext.MessageBox.alert("提示","已配置有网关私钥!请更改网关私钥")
    } else{
        var uploadWarForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:100,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            xtype:'displayfield',
            fieldLabel:"注意",
            value:'上传私钥文件'
        },{
            id:'keyFile',
            name : 'keyFile',
            xtype:'textfield',
            inputType:'file',
            fieldLabel:"导入私钥文件",
            listeners:{
                render:function(){
                    Ext.get('keyFile').on("change",function(){
                        var file = this.getValue();
//                        var fso = new ActiveXObject("Scripting.FileSystemObject");
//                        var f = fso.GetFile(file);
//                        var f = new File(file);
//                        var isSize = true;
//                        if(f.size>1024*1024*2){
//                            alert(f.size+" Bytes");
//                            isSize = false;
//                        }
//                        var fs = file.split('.');
//                        if(fs[fs.length-1].toLowerCase()=='key'){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                                width:300,
                                buttons:{'ok':'确定','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e == 'ok'){
                                        if (uploadWarForm.form.isValid()) {
                                            uploadWarForm.getForm().submit({
                                                url :'../../PkiAction_upload_key.action',
                                                method :'POST',
                                                waitTitle :'系统提示',
                                                waitMsg :'正在上传,请稍后...',
                                                success : function(form,action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title:'信息',
                                                        width:250,
                                                        msg:msg,
//                                                    animEl:'insert.win.info',
                                                        buttons:{'ok':'确定','no':'取消'},
                                                        icon:Ext.MessageBox.INFO,
                                                        closable:false,
                                                        fn:function(e){
                                                            if(e=='ok'){
                                                                key_grid_panel.render();
                                                                key_store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('keyFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:200,
                                                msg:'请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                                buttons:{'ok':'确定'},
                                                icon:Ext.MessageBox.ERROR,
                                                closable:false
                                            });
                                        }
                                    }
                                }
                            });
                     /*   } else {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'上传文件格式不对,请重新选择!',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        Ext.getCmp('formFile').setValue('');
                                    }
                                }
                            });
                        }*/
                    });
                }
            }
        }]
    });
        var win = new Ext.Window({
        title:'上传私钥文件',
        width:400,
        height:150,
        layout:'fit',
        modal:true,
        items:[uploadWarForm],
        bbar:['->',{
//    		id:'insert.win.info',
//    		text:'上传',
//    		handler:function(){
//
//    		}
//    	},{
            text:'关闭',
            handler:function(){
                win.close();
            }
        }]
    }).show()
    };
}

function show_ca(){
    return String.format(
        '<a id="show_ca.info" href="javascript:void(0);" onclick="show_ca_fun();return false;" style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_server(){
    return String.format(
        '<a id="show_server.info" href="javascript:void(0);" onclick="show_server_fun();return false;" style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_key(){
    return String.format(
        '<a id="show_key.info" href="javascript:void(0);" onclick="show_key_fun();return false;" style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_ca_flag(){
    return String.format(
        '<a id="show_ca_flag.info" href="javascript:void(0);" onclick="show_ca_flag_fun();return false;" style="color: green;">下载</a>&nbsp;&nbsp;&nbsp;'+
        '<a id="remove_ca_flag_fun.info" href="javascript:void(0);" onclick="remove_ca_flag_fun();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_server_flag(){
    return String.format(
        '<a id="show_server_flag.info" href="javascript:void(0);" onclick="show_server_flag_fun();return false;" style="color: green;">下载</a>&nbsp;&nbsp;&nbsp;'+
//        '<a id="remove_server_flag_fun.info" href="javascript:void(0);" onclick="remove_server_flag_fun();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
    '<a id="update_server_flag_fun.info" href="javascript:void(0);" onclick="update_server_flag_fun();return false;" style="color: green;">更新</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_key_flag(){
    return String.format(
        '<a id="show_key_flag.info" href="javascript:void(0);" onclick="show_key_flag_fun();return false;" style="color: green;">下载</a>&nbsp;&nbsp;&nbsp;'+
//        '<a id="remove_key_flag_fun.info" href="javascript:void(0);" onclick="remove_key_flag_fun();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'
            '<a id="update_key_flag_fun.info" href="javascript:void(0);" onclick="update_key_flag_fun();return false;" style="color: green;">更新</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_ca_fun(){

    var grid = Ext.getCmp('ca.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var ca_file  = recode.get("ca_file");

    var record = new Ext.data.Record.create([
        {name:'name', mapping:'name'},
        {name:'content', mapping:'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findTrustCrt.action?ca_file="+ca_file
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });
    store.load();


//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"证书项", dataIndex:"name",align:'center',width:50, sortable:true, menuDisabled:true} ,
        {header:"详细信息",dataIndex:'content',align:'center', sortable:true, menuDisabled:true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id:'grid.downloadList.info',
        plain:true,
        height:250,
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
//        sm:boxM,
        store:store,
        //tbar : tb,
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:"证书信息",
        width:500,
        layout:'fit',
        height:250,
        modal:true,
        items:list_grid_panel
    }).show();
}

function show_server_fun(){
    var grid = Ext.getCmp('server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var server_file  = recode.get("server_file");

    var record = new Ext.data.Record.create([
        {name:'name', mapping:'name'},
        {name:'content', mapping:'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findServerCrt.action?server_file="+server_file
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });
    store.load();


    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"证书项", dataIndex:"name", sortable:true, menuDisabled:true} ,
        {header:"详细信息",dataIndex:'content', sortable:true, menuDisabled:true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id:'grid.downloadList.info',
        plain:true,
        height:500,
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
        sm:boxM,
        store:store,
        //tbar : tb,
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:"CA证书信息",
        width:800,
        layout:'fit',
        height:300,
        modal:true,
        items:list_grid_panel
    }).show();
}

function show_key_fun(){

    var grid = Ext.getCmp('key.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var key_file  = recode.get("key_file");

    var record = new Ext.data.Record.create([
        {name:'name', mapping:'name'},
        {name:'content', mapping:'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findKey.action?key_file="+key_file
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });
    store.load();


    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"证书项", dataIndex:"name",width:50, sortable:true, menuDisabled:true} ,
        {header:"详细信息",dataIndex:'content', sortable:true, menuDisabled:true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id:'grid.downloadList.info',
        plain:true,
        height:300,
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
        sm:boxM,
        store:store,
        //tbar : tb,
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:"CA证书信息",
        width:500,
        layout:'fit',
        height:300,
        modal:true,
        items:list_grid_panel
    }).show();
}

function show_ca_flag_fun(){
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    } ;
    var grid = Ext.getCmp('ca.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var id =recode.get("id");
    Ext.Ajax.request({
        url: '../../PkiAction_downloadCa.action',
        timeout: 20*60*1000,
        params:{id:id},
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function show_server_flag_fun(){
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    } ;
    var grid = Ext.getCmp('server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var id =recode.get("id");
    Ext.Ajax.request({
        url: '../../PkiAction_downloadServer.action',
        timeout: 20*60*1000,
        params:{id:id},
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function show_key_flag_fun(){
    if (!Ext.fly('MergeFiles')) {
        var frm = document.createElement('form');
        frm.id = 'MergeFiles';
        frm.name = id;
        frm.style.display = 'none';
        document.body.appendChild(frm);
    } ;
    var grid = Ext.getCmp('key.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var id =recode.get("id");
    Ext.Ajax.request({
        url: '../../PkiAction_downloadKey.action',
        timeout: 20*60*1000,
        params:{id:id},
        form: Ext.fly('MergeFiles'),
        method: 'POST',
        isUpload: true
    });
}

function remove_ca_flag_fun(){
    var grid = Ext.getCmp('ca.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确认删除吗？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../PkiAction_remover_ca.action',
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function remove_server_flag_fun(){
    var grid = Ext.getCmp('server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确认删除吗？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../PkiAction_remover_server.action',
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function remove_key_flag_fun(){
    var grid = Ext.getCmp('key.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确认删除吗？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: '../../PkiAction_remover_key.action',
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid.getStore().reload();
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

function auth_ca(grid,store){
    var start = 0;
    var pageSize = 5;
  /*  var toolbar = new Ext.Toolbar({
        plain:true,
        width:350,
        height:30,
        items:[
            {
                id:'add_ca.info',
                xtype:'button',
                text:'添加CA证书',
                iconCls:'add',
                handler:function () {
                    addCa(grid_panel, store);
                }
            },
            {
                id:'author_ca.info',
                xtype:'button',
                text:'CA认证配置',
                iconCls:'add',
                handler:function () {
                    auth_ca(grid_panel, store);
                }
            }
        ]
    });*/
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'ca_file',			mapping:'ca_file'},
        {name:'ca_name',			mapping:'ca_name'},
        {name:'status',			mapping:'status'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_getAuthConfig.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"/*,
         id:'id'*/
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader,
        listeners:{
            load:function(){
                var records=[];//存放选中记录
                for(var i=0;i<store.getCount();i++){
                    var record = store.getAt(i);
                    if(record.data.status == 1){//根据后台数据判断那些记录默认选中
                        records.push(record);
                    }
                }
                boxM.selectRecords(records);//执行选中记录
            }
        }
    });
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"证书名称",			dataIndex:"ca_name",  align:'center',sortable:true,menuDisabled:true},
        {header:"证书信息",		dataIndex:"context",      align:'center',sortable:true,menuDisabled:true,renderer:auth_ca_config}/*,
        {header:'动作',		dataIndex:"flag",	  align:'center',sortable:true,menuDisabled:true, renderer:show_ca_flag,width:100}*/
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var auth_panel = new Ext.grid.GridPanel({
        id:'auth.grid.info',
        title:'认证配置',
//        height:Ext.getBody().getHeight()/3,
        plain:true,
//        autoHeight:true,
//        height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
        sm:boxM,
        store:store/*,
        tbar : toolbar,
         listeners:{
         render:function(){
         tbar.render(this.tbar);
         }
         }*/,
        bbar:page_toolbar
    });

    var win = new Ext.Window({
        width:600,
        layout:'fit',
        title:'证书链认证配置',
        height:300,
        modal:true,
        items:auth_panel,
        bbar:[
            '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function(){
                    var selected=new Array();
                    var recode =  Ext.getCmp("auth.grid.info").getSelectionModel().getSelections();
                    for (var i=0;i<recode.length;i++){
                        selected[i]=recode[i].get("id") ;
                    }
                    Ext.Ajax.request({
                        url : '../../PkiAction_updateAuthConfig.action',
                        params:{ids:selected},
                        timeout: 20*60*1000,
                        method : "POST",
                        success : function(form, action) {
                            Ext.Msg.alert("提示", "更新配置成功!");
                            store.reload();
                            win.close();
                        },
                        failure : function(result) {
                            Ext.Msg.alert("提示", "更新配置失败!");
                        }
                    });
                }
            }
        ]
    }).show();

    store.load();
}

function setHeight(){
    var h = document.body.clientHeight-8;
    return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}

function  auth_ca_config(){
    return String.format(
        '<a id="show_auth_ca_config.info" href="javascript:void(0);" onclick="show_auth_ca_config();return false;" style="color: green;">查看详细</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_auth_ca_config(){
    var grid = Ext.getCmp('auth.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var ca_file  = recode.get("ca_file");

    var record = new Ext.data.Record.create([
        {name:'name', mapping:'name'},
        {name:'content', mapping:'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findTrustCrt.action?ca_file="+ca_file
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });
    store.load();


//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"证书项", dataIndex:"name",width:50, sortable:true, menuDisabled:true} ,
        {header:"详细信息",dataIndex:'content', sortable:true, menuDisabled:true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id:'grid.downloadList.info',
        plain:true,
        height:250,
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
//        sm:boxM,
        store:store,
        //tbar : tb,
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:"证书信息",
        width:500,
        layout:'fit',
        height:250,
        modal:true,
        items:list_grid_panel
    }).show();
}

function update_key_flag_fun(){
    var grid = Ext.getCmp('key.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
      var uploadWarForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:100,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            xtype:'displayfield',
            fieldLabel:"注意",
            value:'上传私钥文件'
        },{
            id:'keyFile',
            name : 'keyFile',
            xtype:'textfield',
            inputType:'file',
            fieldLabel:"导入私钥文件",
            listeners:{
                render:function(){
                    Ext.get('keyFile').on("change",function(){
                        var file = this.getValue();
//                        var fso = new ActiveXObject("Scripting.FileSystemObject");
//                        var f = fso.GetFile(file);
//                        var f = new File(file);
//                        var isSize = true;
//                        if(f.size>1024*1024*2){
//                            alert(f.size+" Bytes");
//                            isSize = false;
//                        }
//                        var fs = file.split('.');
//                        if(fs[fs.length-1].toLowerCase()=='key'){
                        Ext.MessageBox.show({
                            title:'信息',
                            msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                            width:300,
                            buttons:{'ok':'确定','no':'取消'},
                            icon:Ext.MessageBox.WARNING,
                            closable:false,
                            fn:function(e){
                                if(e == 'ok'){
                                    if (uploadWarForm.form.isValid()) {
                                        uploadWarForm.getForm().submit({
                                            url :'../../PkiAction_update_key.action',
                                            method :'POST',
                                            waitTitle :'系统提示',
                                            params:{id:recode.get('id')},
                                            waitMsg :'正在上传,请稍后...',
                                            success : function(form,action) {
                                                var msg = action.result.msg;
                                                Ext.MessageBox.show({
                                                    title:'信息',
                                                    width:250,
                                                    msg:msg,
//                                                    animEl:'insert.win.info',
                                                    buttons:{'ok':'确定','no':'取消'},
                                                    icon:Ext.MessageBox.INFO,
                                                    closable:false,
                                                    fn:function(e){
                                                        if(e=='ok'){
                                                            grid.render();
                                                            grid.getStore().reload();
                                                            win.close();
                                                        } else {
                                                            Ext.getCmp('keyFile').setValue('');
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Ext.MessageBox.show({
                                            title:'信息',
                                            width:200,
                                            msg:'请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                            buttons:{'ok':'确定'},
                                            icon:Ext.MessageBox.ERROR,
                                            closable:false
                                        });
                                    }
                                }
                            }
                        });
                        /*   } else {
                         Ext.MessageBox.show({
                         title:'信息',
                         width:200,
                         msg:'上传文件格式不对,请重新选择!',
                         buttons:{'ok':'确定'},
                         icon:Ext.MessageBox.ERROR,
                         closable:false,
                         fn:function(e){
                         if(e=='ok'){
                         Ext.getCmp('formFile').setValue('');
                         }
                         }
                         });
                         }*/
                    });
                }
            }
        }]
    });
      var win = new Ext.Window({
        title:'上传私钥文件',
        width:400,
        height:150,
        layout:'fit',
        modal:true,
        items:[uploadWarForm],
        bbar:['->',{
//    		id:'insert.win.info',
//    		text:'上传',
//    		handler:function(){
//
//    		}
//    	},{
            text:'关闭',
            handler:function(){
                win.close();
            }
        }]
    }).show();
    }
}

function update_server_flag_fun(){
    var grid = Ext.getCmp('server.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        var uploadWarForm = new Ext.form.FormPanel({
            frame:true,
            labelWidth:150,
            labelAlign:'right',
            fileUpload:true,
            border:false,
            defaults : {
                width : 200,
                allowBlank : false,
                blankText : '该项不能为空！'
            },
            items:[{
                xtype:'displayfield',
                fieldLabel:"注意",
                value:'上传文件为pem证书证书'
            },{
                id:'serverFile',
                name : 'serverFile',
                xtype:'textfield',
                inputType:'file',
                fieldLabel:"导入网关证书文件",
                listeners:{
                    render:function(){
                        Ext.get('serverFile').on("change",function(){
                            var file = this.getValue();
//                        var fso = new ActiveXObject("Scripting.FileSystemObject");
//                        var f = fso.GetFile(file);
//                        var f = new File(file);
//                        var isSize = true;
//                        if(f.size>1024*1024*2){
//                            alert(f.size+" Bytes");
//                            isSize = false;
//                        }
                            var fs = file.split('.');
                            if(fs[fs.length-1].toLowerCase()=='pem'||fs[fs.length-1].toLowerCase()=='cer'||fs[fs.length-1].toLowerCase()=='crt'){
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                                    width:300,
                                    buttons:{'ok':'确定','no':'取消'},
                                    icon:Ext.MessageBox.WARNING,
                                    closable:false,
                                    fn:function(e){
                                        if(e == 'ok'){
                                            if (uploadWarForm.form.isValid()) {
                                                uploadWarForm.getForm().submit({
                                                    url :'../../PkiAction_update_server.action',
                                                    method :'POST',
                                                    params:{id:recode.get('id')},
                                                    waitTitle :'系统提示',
                                                    waitMsg :'正在上传,请稍后...',
                                                    success : function(form,action) {
                                                        var msg = action.result.msg;
                                                        Ext.MessageBox.show({
                                                            title:'信息',
                                                            width:250,
                                                            msg:msg,
//                                                    animEl:'insert.win.info',
                                                            buttons:{'ok':'确定','no':'取消'},
                                                            icon:Ext.MessageBox.INFO,
                                                            closable:false,
                                                            fn:function(e){
                                                                if(e=='ok'){
                                                                    grid.render();
                                                                    grid.getStore().reload();
                                                                    win.close();
                                                                } else {
                                                                    Ext.getCmp('serverFile').setValue('');
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                Ext.MessageBox.show({
                                                    title:'信息',
                                                    width:200,
                                                    msg:'请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                                    buttons:{'ok':'确定'},
                                                    icon:Ext.MessageBox.ERROR,
                                                    closable:false
                                                });
                                            }
                                        }
                                    }
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:200,
                                    msg:'上传文件格式不对,请重新选择!',
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            Ext.getCmp('serverFile').setValue('');
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }]
        });
        var win = new Ext.Window({
            title:'上传网关证书文件',
            width:400,
            height:150,
            layout:'fit',
            modal:true,
            items:[uploadWarForm],
            bbar:['->',{
//    		id:'insert.win.info',
//    		text:'上传',
//    		handler:function(){
//
//    		}
//    	},{
                text:'关闭',
                handler:function(){
                    win.close();
                }
            }]
        }).show();
    }
}


function show_pkcs(){
    return String.format(
        '<a id="show_server.info" href="javascript:void(0);" onclick="show_pkcs_fun();return false;" style="color: green;">查看</a>&nbsp;&nbsp;&nbsp;'
    );
}

function show_pkcs_flag(){
    return String.format(
            '<a id="update_server_flag_fun.info" href="javascript:void(0);" onclick="update_pkcs_flag_fun();return false;" style="color: green;">更新</a>&nbsp;&nbsp;&nbsp;'
    );
}

function  show_pkcs_fun(){
    var grid = Ext.getCmp('pkcs.grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var server_file  = recode.get("pkcs_file");

    var record = new Ext.data.Record.create([
        {name:'name', mapping:'name'},
        {name:'content', mapping:'content'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../PkiAction_findServerCrt.action?server_file="+server_file
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });
    store.load();


//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"证书项", dataIndex:"name", align:'center',width:50, sortable:true, menuDisabled:true} ,
        {header:"详细信息",dataIndex:'content',align:'center', sortable:true, menuDisabled:true}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var list_grid_panel = new Ext.grid.GridPanel({
        id:'grid.downloadList.info',
        plain:true,
        height:250,
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
//        sm:boxM,
        store:store,
        //tbar : tb,
        bbar:page_toolbar
    });
    var win = new Ext.Window({
        title:"服务器证书信息",
        width:500,
        layout:'fit',
        height:250,
        modal:true,
        items:list_grid_panel
    }).show();
}

var Model = new Object;
//function importCsr(type) {Model.importCsr(type);}
function update_pkcs_flag_fun() {Model.add_pfx_server();}

