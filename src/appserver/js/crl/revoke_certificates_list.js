Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'username', mapping:'username'} ,
        {name:'id_card', mapping:'id_card'} ,
        {name:'serial', mapping:'serial'} ,
        {name:'revoke_time', mapping:'revoke_time'} ,
        {name:'iss_name', mapping:'iss_name'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../RevokeAction_get_revoke.action" ,
        timeout: 10*1000
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

    store.load({
        params:{
            start:start, limit:pageSize
        }
    });

//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true});   //复选框单选

    var rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"通用名", dataIndex:"username",align:'center', sortable:true, menuDisabled:true,sort:true} ,
//        {header:"身份证号", dataIndex:"id_card",align:'center', sortable:true, menuDisabled:true} ,
        {header:"签发者", dataIndex:"iss_name", align:'center',sortable:true, menuDisabled:true},
        {header:"吊销时间", dataIndex:"revoke_time",align:'center', sortable:true, menuDisabled:true},
        {header:"证书序列号", dataIndex:"serial",align:'center', sortable:true, menuDisabled:true}
      /*,
        {header:'操作标记', dataIndex:'flag', align:'center',sortable:true, menuDisabled:true, renderer:show_flag, width:300}*/
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        pageSize:pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });

    function setHeight() {
        var h = document.body.clientHeight - 8;
        return h;
    };

    var tb = new Ext.Toolbar({
        autoWidth :true,
        autoHeight:true,
        items:[
            {
                id:'update_crl.info',
                xtype:'button',
                text:'更新CRL',
                iconCls:'upload',
                handler:function () {
                    updateCRL(grid_panel,store);
                }
            }, {
                id:'auto_down_crl.info',
                xtype:'button',
                text:'自动更新CRL',
                iconCls:'download',
                handler:function () {
                    autoDownCRL(grid_panel,store);
                }
            }, {
                id:'down_crl.info',
                xtype:'button',
                text:'手动更新CRL',
                iconCls:'download',
                handler:function () {
                    downCRL(grid_panel,store);
                }
            }]
    });

    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
//        title:'吊销列表',
        plain:true,
        height:setHeight(),
        viewConfig:{
            forceFit:true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle:'width:100%',
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        cm:colM,
//        sm:boxM,
        store:store,
        tbar : tb,
        /*listeners:{
            render:function(){
                tbar.render(this.tbar);
            }
        },*/
        bbar:page_toolbar
    });

    var port = new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        items:[grid_panel]
    });
});

function updateCRL(grid,store){
    var uploadForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:150,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            xtype:'displayfield',
            fieldLabel:'注意',
            value:'更新文件必须为CRL列表'
        },{
            id:'crlFile',
            name : 'crlFile',
            xtype:'textfield',
            inputType:'file',
            fieldLabel:"更新CRL",
            listeners:{
                render:function(){
                    Ext.get('crlFile').on("change",function(){
                        var file = Ext.get('crlFile').getValue();
                        var fs = file.split('.');
                        if(fs[fs.length-1].toLowerCase()=='crl'|fs[fs.length-1].toLowerCase()=='pem'){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                                width:300,
                                buttons:{'ok':'确定','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e == 'ok'){
                                        if (uploadForm.form.isValid()) {
                                            uploadForm.getForm().submit({
                                                url :'../../RevokeAction_update_crl.action',
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
                                                                store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('crlFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                },
                                                failure : function(form, action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title:'信息',
                                                        width:200,
                                                        msg:msg,
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
                                        Ext.getCmp('crlFile').setValue('');
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
        title:'更新CRL',
        width:500,
        height:150,
        layout:'fit',
        modal:true,
        items:[uploadForm],
        bbar:[
            '->',
            {
                text:'关闭',
                handler:function(){
                    win.close();
                }
            }
        ]
    }).show();
}

function downCRL(grid,store){

    var record = new Ext.data.Record.create([
        {name:'url', mapping:'url'} /*,
        {name:'second', mapping:'second'} ,
        {name:'hour', mapping:'hour'},
        {name:'day', mapping:'day'}*/
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../CRLConfigAction_find.action"
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
        var url = store.getAt(0).get('url');
//        var second = store.getAt(0).get('second');
//        var hour = store.getAt(0).get('hour');
//        var day = store.getAt(0).get('day');
        Ext.getCmp('crl.url').setValue(url);
//        Ext.getCmp('crl.second').setValue(second);
//        Ext.getCmp('crl.hour').setValue(hour);
//        Ext.getCmp('crl.day').setValue(day);
    });

    var downForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:150,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
//            width : 250,
            anchor:'%95',
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            {
                xtype:'displayfield',
                fieldLabel:'<font color="red">注意</font>',
                value:'HTTP请求CRL下载地址'
            } ,
            new Ext.form.TextField({
                fieldLabel:'URL地址',
                name:'url',
                id:"crl.url",
                allowBlank:false,
                blankText:"URL地址"
            })]
    });
    var win = new Ext.Window({
        title:'下载CRL',
        width:500,
        height:150,
        layout:'fit',
        modal:true,
        items:[downForm],
        bbar:[
            '->',
            {
                text:'下载',
                handler:function(){
                    if (downForm.form.isValid()) {
                        downForm.getForm().submit({
                            url :'../../RevokeAction_down_crl.action',
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在下载,请稍后...',
                            success : function(form,action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:{'ok':'确定','no':'取消'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            },failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:200,
                                    msg:msg,
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
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            },
            {
                text:'关闭',
                handler:function(){
                    win.close();
                }
            }
        ]
    }).show();
}

function autoDownCRL(grid,store){

    var record = new Ext.data.Record.create([
        {name:'url', mapping:'url'} ,
        {name:'second', mapping:'second'} ,
        {name:'hour', mapping:'hour'},
        {name:'day', mapping:'day'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../CRLConfigAction_find.action"
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
        var url = store.getAt(0).get('url');
        var second = store.getAt(0).get('second');
        var hour = store.getAt(0).get('hour');
        var day = store.getAt(0).get('day');
        Ext.getCmp('crl.auto.url').setValue(url);
        Ext.getCmp('crl.second').setValue(second);
        Ext.getCmp('crl.hour').setValue(hour);
        Ext.getCmp('crl.day').setValue(day);
    });

    var downForm = new Ext.form.FormPanel({
        frame:true,
        labelWidth:150,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            anchor:'%95',
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            {
                xtype:'displayfield',
                fieldLabel:'<font color="red">注意</font>',
                value:'HTTP请求CRL下载地址'
            } ,
            new Ext.form.TextField({
                fieldLabel:'URL地址',
                name:'url',
                id:"crl.auto.url",
                allowBlank:false,
                blankText:"URL地址"
            }),
            {
                layout: 'column',
                border: false,
                fieldLabel:'更新周期',
                items: [
                    new Ext.form.NumberField({
                        columnWidth: .2,
                        name:'second',
                        id:"crl.second",
                        nanText:'只能输入0-59之间的数字',//无效数字提示
                        maxValue:59,//最大值
                        minValue:0, //最小值
                        allowBlank:true
                    }),
                    new Ext.form.DisplayField({
                        columnWidth: .1,
                        value:'分'
                    }),
                    new Ext.form.NumberField({
                        fieldLabel:'时',
                        columnWidth: .2,
                        nanText:'只能输入0-23之间的数字',//无效数字提示
                        maxValue:23,//最大值
                        minValue:0, //最小值
                        name:'hour',
                        id:"crl.hour",
                        allowBlank:true
                    }),
                    new Ext.form.DisplayField({
                        columnWidth: .1,
                        value:'时'
                    }),
                    new Ext.form.NumberField({
                        fieldLabel:'天',
                        columnWidth: .2,
                        name:'day',
                        id:"crl.day",
                        allowBlank:true
                    }),
                    new Ext.form.DisplayField({
                        columnWidth: .1,
                        value:'天'
                    })
                ]
            }
            ]
    });
    var win = new Ext.Window({
        title:'自动更新CRL配置',
        width:500,
        height:200,
        layout:'fit',
        modal:true,
        items:[downForm],
        bbar:[
            '->',
            {
                text:'保存配置',
                handler:function(){
                    if (downForm.form.isValid()) {
                        downForm.getForm().submit({
                            url :'../../CRLConfigAction_save.action',
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在下载,请稍后...',
                            success : function(form,action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:{'ok':'确定','no':'取消'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            store.reload();
                                            win.close();
                                        }
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:200,
                                    msg:msg,
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
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            },
            {
                text:'关闭',
                handler:function(){
                    win.close();
                }
            }
        ]
    }).show();
}



