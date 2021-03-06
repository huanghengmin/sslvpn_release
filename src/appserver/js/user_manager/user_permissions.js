Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'id', mapping:'id'} ,
        {name:'username', mapping:'username'} ,
        {name:'id_card', mapping:'id_card'} ,
//        {name:'group_id', mapping:'group_id'} ,
        {name:'deny_access', mapping:'deny_access'} ,
        {name:'dynamic_ip', mapping:'dynamic_ip'},
        {name:'static_ip', mapping:'static_ip'},
        {name:'allow_all_subnet', mapping:'allow_all_subnet'},
        {name:'allow_all_client', mapping:'allow_all_client'},
        {name:'create_time', mapping:'create_time'},
        {name:'count_bytes_cycle', mapping:'count_bytes_cycle'} ,
        {name:'max_bytes', mapping:'max_bytes'},
        {name:'active', mapping:'active'},
        {name:'email', mapping:'email'},
        {name:'phone', mapping:'phone'},
        {name:'address', mapping:'address'},
        {name:'serial_number', mapping:'serial_number'},
        {name:'type', mapping:'type'},
//        {name:'key_size', mapping:'key_size'},
        {name:'revoked', mapping:'revoked'},
        {name:'enabled', mapping:'enabled'},
        {name:'real_address', mapping:'real_address'},
        {name:'byte_received', mapping:'byte_received'},
        {name:'byte_send', mapping:'byte_send'},
        {name:'connected_since', mapping:'connected_since'},
        {name:'virtual_address', mapping:'virtual_address'},

        {name:'net_id', mapping:'net_id'},
        {name:'terminal_id', mapping:'terminal_id'},

        {name:'last_ref', mapping:'last_ref'}

    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../UserPermissionAction_find.action" ,
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
        {header:"用户名", dataIndex:"username",align:'center', sortable:true, menuDisabled:true,sort:true} ,
        {header:"身份证", dataIndex:"id_card",align:'center', sortable:true, menuDisabled:true} ,
        {header:"电子邮件", dataIndex:"email",align:'center', sortable:true, menuDisabled:true} ,
        {header:"状态", dataIndex:"status", align:'center',sortable:true, menuDisabled:true,renderer:show_enabled},
        {header:'操作标记', dataIndex:'flag', align:'center',sortable:true, menuDisabled:true, renderer:show_flag, width:300}
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
    } ;

    var tb = new Ext.Toolbar({
        autoWidth :true,
        autoHeight:true,
        items:[
            {
                id:'import_resource.info',
                xtype:'button',
                text:'导入用户信息',
                iconCls:'add',
                handler:function () {
                      importUser(grid_panel,store);
//                    addUser(grid_panel, store);
                }
            }]
    });

    var typeData = [
        [0,'禁止'],[1,'允许']
    ];
    var typeDataStore = new Ext.data.SimpleStore({
        fields:['id','name'],
        data:typeData
    });

    var tbar = new Ext.Toolbar({
        autoWidth :true,
        autoHeight:true,
        items:[
            '用户名', new Ext.form.TextField({
                name : 'username',
                id:'hzih.tbar.user.cn'
            }),
            '身份证号码',
            new Ext.form.TextField({
                id:'hzih.tbar.user.id_card',
                regex:/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})([0-9xX])$/,
                regexText:'请输入有效的身份证号',
                name : 'id_card'
            }),
            '电子邮件',
            new Ext.form.TextField({
                id:'hzih.tbar.user.email',
                name : 'email'
            }),
            '状态',
            new Ext.form.ComboBox({
                typeAhead: true,
                triggerAction: 'all',
                forceSelection: true,
                mode: 'local',
                hiddenName:"status",
                id:'hzih.tbar.user.enabled',
                store: typeDataStore,
                valueField: 'id',   //下拉框具体的值（例如值为SM，则显示的内容即为‘短信’）
                displayField: 'name'//下拉框显示内容
            }) ,
            {
                id:'tbar.tbar.raUser.info',
                xtype:'button',
                iconCls:'select',
                text:'查询',
                handler:function () {
                    var cn = Ext.getCmp('hzih.tbar.user.cn').getValue();
                    var email = Ext.getCmp('hzih.tbar.user.email').getValue();
                    var id_card = Ext.getCmp('hzih.tbar.user.id_card').getValue();
                    var enable = Ext.getCmp('hzih.tbar.user.enabled').getValue();
                    store.setBaseParam('cn', cn);
                    store.setBaseParam('email', email);
                    store.setBaseParam('id_card', id_card);
                    store.setBaseParam('status', enable);
                    store.load({
                        params : {
                            start : start,
                            limit : pageSize
                        }
                    });
                }
            }]
    });

    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
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
        listeners:{
            render:function(){
                tbar.render(this.tbar);
            }
        },
        bbar:page_toolbar
    });

    var port = new Ext.Viewport({
        layout:'fit',
        renderTo:Ext.getBody(),
        items:[grid_panel]
    });
});

function show_flag(value, p, r){
    if(parseInt(r.get("enabled"))==1){
        return String.format(
//            '<a id="revokeUser.info" href="javascript:void(0);" onclick="revokeUser();return false;"style="color: green;">吊销</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="disableUser.info" href="javascript:void(0);" onclick="disableUser();return false;"style="color: green;">阻断</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="permission.info" href="javascript:void(0);" onclick="permission();return false;" style="color: green;">权限</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="deleteUser.info" href="javascript:void(0);" onclick="deleteUser();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="updateUser.info" href="javascript:void(0);" onclick="updateUser();return false;" style="color: green;">更新</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="cleanThreeYards.info" href="javascript:void(0);" onclick="cleanThreeYards();return false;" style="color: green;">清除三码绑定</a> &nbsp;&nbsp;&nbsp;'+
                //'<a id="findrouteusers.info" href="javascript:void(0);" onclick="findrouteusers();return false;" style="color: green;">路由器用户</a> &nbsp;&nbsp;&nbsp;'+
                '<a id="viewUser.info" href="javascript:void(0);" onclick="viewUser();return false;" style="color: green;">查看</a> &nbsp;&nbsp;&nbsp;'

        );
    }else if(parseInt(r.get("enabled"))==0){
        return String.format(
//            '<a id="revokeUser.info" href="javascript:void(0);" onclick="revokeUser();return false;"style="color: green;">吊销</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="enableUser.info" href="javascript:void(0);" onclick="enableUser();return false;"style="color: green;">恢复接入</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="permission.info" href="javascript:void(0);" onclick="permission();return false;" style="color: green;">权限</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="deleteUser.info" href="javascript:void(0);" onclick="deleteUser();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="updateUser.info" href="javascript:void(0);" onclick="updateUser();return false;" style="color: green;">更新</a>&nbsp;&nbsp;&nbsp;'  +
                '<a id="cleanThreeYards.info" href="javascript:void(0);" onclick="cleanThreeYards();return false;" style="color: green;">清除三码绑定</a> &nbsp;&nbsp;&nbsp;'+
                //'<a id="findrouteusers.info" href="javascript:void(0);" onclick="findrouteusers();return false;" style="color: green;">路由器用户</a> &nbsp;&nbsp;&nbsp;'+
                '<a id="viewUser.info" href="javascript:void(0);" onclick="viewUser();return false;" style="color: green;">查看</a> &nbsp;&nbsp;&nbsp;'
        );
    }
};

function show_enabled(value, p, r){
    if(r.get("enabled")=="1"){
        return String.format('<img src="../../img/icon/ok.png" alt="启用" title="启用" />');
    }else if(r.get("enabled")=="0"){
        return String.format('<img src="../../img/icon/off.gif" alt="禁用" title="禁用" />');
    }
}


/*var revokeUser = function(){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var CN =recode.get("cn");
    var DN  = recode.get("hzihdn");
    Ext.Msg.confirm("警告", "确认吊销证书,吊销后证书不可用!", function (sid) {
        if (sid == "yes") {
            Ext.Ajax.request({
                url: '../../CaUserAction_revokeCa.action',
                timeout: 20*60*1000,
                params:{DN:DN,CN:CN},
                method: 'POST',
                success : function(form, action) {
                    Ext.Msg.alert("提示", "吊销证书成功!");
                    grid.getStore().reload();
                },
                failure : function(result) {
                    Ext.Msg.alert("提示", "吊销证书失败!");
                    grid.getStore().reload();
                }
            });
        }
    });
};             //吊销证书*/

function viewUser(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var status =recode.get("enabled")
    var statusValue;
    if(status=="0"){
        statusValue= '<img src="../../img/icon/off.gif" alt="禁用" title="禁用" />'
    } else if(status=="1"){
        statusValue= '<img src="../../img/icon/ok.png" alt="启用" title="启用" />'
    }

    var formPanel = new Ext.form.FormPanel({
        frame:true,
        width:800,
        autoScroll:true,
        baseCls : 'x-plain',
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        layout:'form',
        border:false,
        defaults:{
            width:250
        },
        items:[
            new Ext.form.DisplayField({
                fieldLabel:'用户名',
                value:recode.get("username")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'身份证',
                value:recode.get("id_card")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'联系电话',
                value:recode.get("phone")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'联系地址',
                value:recode.get("address")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'电子邮件',
                value:recode.get("email")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'状态',
                value:statusValue
            }),
            new Ext.form.DisplayField({
                fieldLabel:'证书序列号',
                value:recode.get("serial_number")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'设备终端编号',
                value:recode.get("terminal_id")
            }),
            new Ext.form.DisplayField({
                fieldLabel:'SIM唯一编号',
                value:recode.get("net_id")
            })
        ]
    });

    var select_Win = new Ext.Window({
        title:"用户资料",
        width:800,
        layout:'fit',
        height:380,
        modal:true,
        items:formPanel
    });
    select_Win.show();
};

function deleteUser() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确认删除吗？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UserPermissionAction_remove.action",
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
}  ;

function updateUser() {
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
                name : 'user.cn',
                fieldLabel : '用户名',
                readOnly:true,
                value: recode.get('username'),
                regex:/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
                regexText:'只能输入数字,字母,中文,下划线,不能以下划线开头和结尾!',
                id:  'user.username',
                allowBlank : false,
                emptyText:"请输入您的姓名",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '身份证号码',
                value: recode.get('id_card'),
                regex:/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})([0-9xX])$/,
                regexText:'请输入有效的身份证号',
                allowBlank : false,
                emptyText:"请输入您的身份证号码",
                blankText : "请填写数字 ，不能为空，请正确填写",
                name : 'user.id_card'
            }),
            new Ext.form.TextField({
                fieldLabel : '联系电话',
                name : 'user.phone',
                value: recode.get('phone'),
                regex:/^(1[3,5,8,7]{1}[\d]{9})|(((400)-(\d{3})-(\d{4}))|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{3,7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/,
                regexText:'请输入正确的电话号或手机号',
                allowBlank : false,
                emptyText:"请输入您的手机号码",
                blankText : "联系电话"
            }),
            new Ext.form.TextField({
                fieldLabel : '联系地址',
                value: recode.get('address'),
                emptyText:"请输入您具体联系地址",
                name : 'user.address',
                allowBlank : false,
                blankText : "联系地址"
            }),
            new Ext.form.TextField({
                fieldLabel : '电子邮件',
                regex:/^[0-9a-zA-Z_\-\.]+@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/,
                regexText:'请输入您有效的邮件地址',
                name : 'user.email',
                emptyText:"请输入电子邮件地址",
                value: recode.get('email'),
                allowBlank : false,
                blankText : "电子邮件"
            }),
            new Ext.form.TextField({
                fieldLabel : '证书序列号',
                name : 'user.serial_number',
                emptyText:"请输入证书序列号",
                value: recode.get('serial_number'),
                allowBlank : false,
                blankText : "证书序列号"
            }),
            new Ext.form.TextField({
                fieldLabel : '设备终端编号',
                name : 'user.terminal_id',
                emptyText:"请输入设备终端编号",
                value: recode.get('terminal_id'),
                allowBlank : false,
                blankText : "设备终端编号"
            }),
            new Ext.form.TextField({
                fieldLabel : 'SIM唯一编号',
                name : 'user.net_id',
                emptyText:"请输入SIM唯一编号",
                value: recode.get('net_id'),
                allowBlank : false,
                blankText : "SIM唯一编号"
            })
        ]
    });
    var win = new Ext.Window({
        title:"更新授权用户",
        width:500,
        layout:'fit',
        height:340,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'insert_win.info',
                text:'更新',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../UserPermissionAction_modify.action',
                            timeout: 20*60*1000,
                            method :'POST',
                            waitTitle :'系统提示',
                            params:{
                                id:recode.get("id")
                            },
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
};

function disableUser(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "阻断用户？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UserPermissionAction_disable.action",
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
};

function enableUser(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "恢复接入？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UserPermissionAction_enable.action",
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
};

function addUser(grid_panel, store){
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
                fieldLabel : '用户名',
                name : 'user.cn',
                regex:/^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
                regexText:'只能输入数字,字母,中文,下划线,不能以下划线开头和结尾!',
                id:  'user.username',
                allowBlank : false,
                emptyText:"请输入您的姓名",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '身份证号码',
                regex:/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})([0-9xX])$/,
                regexText:'请输入有效的身份证号',
                allowBlank : false,
                emptyText:"请输入您的身份证号码",
                blankText : "请填写数字 ，不能为空，请正确填写",
                name : 'user.id_card'
            }),
            new Ext.form.TextField({
                fieldLabel : '联系电话',
                name : 'user.phone',
                regex:/^(1[3,5,8,7]{1}[\d]{9})|(((400)-(\d{3})-(\d{4}))|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{3,7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/,
                regexText:'请输入正确的电话号或手机号',
                emptyText:"请输入您的手机号码",
                allowBlank : false,
                blankText : "联系电话"
            }),
            new Ext.form.TextField({
                fieldLabel : '联系地址',
                name : 'user.address',
                allowBlank : false,
                emptyText:"请输入您具体联系地址",
                blankText : "联系地址"
            }),
            new Ext.form.TextField({
                fieldLabel : '电子邮件',
                regex:/^[0-9a-zA-Z_\-\.]+@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/,
                regexText:'请输入有效的邮件地址',
                name : 'user.email',
                emptyText:"请输入您有效的邮件地址",
                allowBlank : false,
                blankText : "电子邮件"
            })
        ]
    });
    var win = new Ext.Window({
        title:"新增授权用户",
        width:500,
        layout:'fit',
        height:340,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'insert_win.info',
                text:'新增',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../UserPermissionAction_add.action',
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
};

function permission(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();

    var record = new Ext.data.Record.create([
        {name:'allow_client_to_client',mapping:'allow_client_to_client'} ,
        {name:'allow_all_nets',mapping:'allow_all_nets'} ,
        {name:'dynamic_ip',mapping:'dynamic_ip'} ,
        {name:'static_ip',mapping:'static_ip'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../UserPermissionAction_findUserPermission.action?id="+recode.get('id')
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
        var allow_client_to_client = store.getAt(0).get('allow_client_to_client');
        var allow_all_nets = store.getAt(0).get('allow_all_nets');
        var dynamic_ip = store.getAt(0).get('dynamic_ip');
        var static_ip = store.getAt(0).get('static_ip');
        Ext.getCmp('user.allow_client_to_client').setValue(allow_client_to_client);
        Ext.getCmp('user.allow_all_nets').setValue(allow_all_nets);
        Ext.getCmp('user.ip').setValue(dynamic_ip);
        Ext.getCmp('user.static_ip').setValue(static_ip);
    });



    <!--server-->
    var server_start =0;
    var server_pageSize = 3;
    var server_toolbar = new Ext.Toolbar({
        plain:true,
        width:350,
        height:30,
        items:[
            {
                id:'add_server.info',
                xtype:'button',
                text:'添加子网',
                iconCls:'add',
                handler:function () {
                    addAccessNet(server_grid_panel, server_store);
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
        url:"../../UserPermissionAction_findUserNets.action?id="+recode.get('id')
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
//        bodyStyle:'width:100%',
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

    var formPanel = new Ext.form.FormPanel({
        plain:true,
        autoScroll:true,
        labelAlign:'right',
        labelWidth:50,
        defaultType:'textfield',
        defaults: {
            anchor: '98%',
            allowBlank:false,
            blankText:'该项不能为空!',
            labelStyle: 'padding-left:4px;'
        },
        items:[
            {
                xtype:'fieldset',
                layout:'column',
                title:'用户权限列表',
                collapsible:true,
//                border:false,
                items:[
                {
                xtype:'fieldset',
                border:false,
                columnWidth:.5,
//                collapsible:true,
                title:'IP规则(动态分配/静态分配):',
                defaultType:'textfield',
                labelWidth:150,
                defaults:{
                    //                width:250,
                    allowBlank:false,
                    blankText:'该项不能为空!'
                },
                items:[
                    new Ext.form.RadioGroup({
                        id:'user.ip',
                        fieldLabel:"用户IP", //RadioGroup.fieldLabel 标签与 Radio.boxLabel 标签区别
                        //                hideLabel : true,   //隐藏RadioGroup标签
                        layout:'anchor',
                        defaults:{
                            anchor:'90%',
                            labelStyle:'padding-left:4px;'
                        },
                        columns:1,
                        collapsible:true,
                        collapsed:true,
                        items:[
                            new Ext.form.Radio({                          //三个必须项
                                boxLabel:"动态IP", //Radio标签
                                name:"dynamic_ip", //用于form提交时传送的参数名
                                inputValue:1, //提交时传送的参数值
                                listeners:{
                                    check:function (checkbox, checked) {
                                        if (checked) {
                                            var user_static_ip = Ext.getCmp("user.ip.fieldset");
                                            if (user_static_ip.isVisible()) {
                                                user_static_ip.hide();
                                            }
                                        }

                                    }
                                }
                            }),
                            new Ext.form.Radio({            //以上相同
                                boxLabel:"静态IP",
                                name:"dynamic_ip",
                                inputValue:0,
                                listeners:{
                                    check:function (checkbox, checked) {
                                        if(checked){
                                            var user_static_ip = Ext.getCmp("user.ip.fieldset");
                                            if (!user_static_ip.isVisible()) {
                                                user_static_ip.show();
                                            }
                                        }
                                    }
                                }
                            })
                        ]
                    }),
                    {
                        xtype:'fieldset',
                        id:'user.ip.fieldset',
                        border:false,
                        hidden:true,
                        defaultType:'textfield',
                        defaults:{
//                            allowBlank:false,
                            blankText:'该项不能为空!'
                        },
                        defaultType:'textfield',
                        layout:'form',
                        items:[
                            {
                                fieldLabel:'静态IP',
                                name:'static_ip',
                                id:'user.static_ip',
                                blankText:"静态IP"
                            }
                        ]
                    }
                ]
            },
                {columnWidth:.1,border:false},
                {
                xtype:'fieldset',
                border:false,
                columnWidth:.4,
                id:'user.other.permission',
                defaultType:'textfield',
                title:'高级权限:',
                defaults:{
//                  allowBlank:false,
                    blankText:'该项不能为空!'
                },
                defaultType:'textfield',
                layout:'form',
                items:[
                    {
                        xtype:'checkbox',
//                        fieldLabel:'允许访问所有私有网络',
                        name:'allow_all_nets',
                        boxLabel:'允许访问所有私有子网',
                        id:'user.allow_all_nets'
                    },
                    {
//                        fieldLabel:'允许访问VPN客户端',
                        xtype:'checkbox',
                        boxLabel:'允许访问VPN客户端',
                        name:'allow_client_to_client',
                        id:'user.allow_client_to_client'
                    }
                ]
            }
                ]
            },
            {
                xtype: 'fieldset',
                collapsible:true,
                title: '允许子网',
                defaultType: 'textfield',
                labelWidth:150,
                defaults: {
                    anchor: '100%',
                    allowBlank:false,
                    blankText:'该项不能为空!',
                    labelStyle: 'padding-left:4px;'
                },
                items: [server_grid_panel]
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
                            url:"../../UserPermissionAction_updateUserPermission.action",
                            method:'POST',
                            params:{id:recode.get('id')},
                            waitTitle:'系统提示',
                            waitMsg:'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:500,
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
                                    width:500,
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
            }
        ]
    });

    var win = new Ext.Window({
        title:"用户权限配置",
        layout:'fit',
        width:800,
        height:420,
        modal:true,
        autoScroll:true,
        items:formPanel
    }).show();

    server_store.load({
        params:{
            start:server_start,limit:server_pageSize
        }
    });
}

function show_server_flag(){
    return String.format(
        '<a id="remove_net.info" href="javascript:void(0);" onclick="remove_net();return false;" style="color: green;">移除</a>&nbsp;&nbsp;&nbsp;'
    );
}

function addAccessNet(grid,store){
        var grid = Ext.getCmp('grid.info');
        var recode = grid.getSelectionModel().getSelected();

        var record = new Ext.data.Record.create([
            {name:'id', mapping:'id'} ,
            {name:'net', mapping:'net'} ,
            {name:'net_mask', mapping:'net_mask'}
        ]);
        var proxy = new Ext.data.HttpProxy({
            url:"../../UserPermissionAction_findOtherUserIdNets.action?id="+recode.get("id")
        });
        var reader = new Ext.data.JsonReader({
            totalProperty:"total",
            root:"rows",
            id:'id'
        },record);
        var storeWin = new Ext.data.GroupingStore({
            proxy : proxy,
            reader : reader
        });
        var start = 0;
        var pageSize = 10;
//    var SelectArray = new Array();//记录集:所有选中的行号
        var boxM = new Ext.grid.CheckboxSelectionModel({
//        listeners:{
//            rowselect:function(obj,rowIndex,record){
//                SelectArray[record.data.id] = record.data.id;//往记录集中添加选中的行号,我这里直接保存了一个值
//            },
//            rowdeselect:function(obj,rowIndex,record){
//                delete SelectArray[record.data.id];
//            }
//        }
        });   //复选框
        var colM = new Ext.grid.ColumnModel([
            boxM,
            {header:"网络地址",dataIndex:"net", align:'center',menuDisabled:true},
            {header:"子网掩码",dataIndex:"net_mask",align:'center',menuDisabled:true}
        ]);

        var permission= new Ext.grid.GridPanel({
            id:'grid.addNets.info',
            plain:true,
            autoScroll:true,
            height:230,
            loadMask:{msg:'正在加载数据，请稍后...'},
            border:false,
            cm:colM,
            sm:boxM,
            store:storeWin,
            stripeRows:true,
            autoExpandColumn:2,
            disableSelection:true,
            bodyStyle:'width:100%',
            selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
            bbar:new Ext.PagingToolbar({
                pageSize : pageSize,
                store:storeWin,
                displayInfo:true,
                displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
                emptyMsg:"没有记录",
                beforePageText:"当前页",
                afterPageText:"共{0}页"
            })
        });
        var win = new Ext.Window({
            title:"子网",
            width:530,
            layout:'fit',
            height:330,
            modal:true,
            items:[{
                frame:true,
                items:[{
                    layout:'fit',
                    items:[{
                        xtype:'fieldset',
                        title:'子网地址',
                        height:230,
                        items:[permission]
                    }]
                }]
            }],
            bbar:[
                '->',
                {
                    id:'insert_addNets.info',
                    text:'添加',
                    handler:function(){
                        var allSelect =  Ext.getCmp("grid.addNets.info").getSelectionModel().getSelections();
                        var pIds = new Array();
                        for(var i = 0; i < allSelect.length; i++){
                            pIds[i] = allSelect[i].get('id');
                        }
                        Ext.Ajax.request({
                            url : '../../UserPermissionAction_addNetsToUser.action',             // 删除 连接 到后台
                            params :{pIds:pIds,userId:recode.get("id")},
                            method:'POST',
                            success : function(r,o){
                                var respText = Ext.util.JSON.decode(r.responseText);
                                var msg = respText.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            grid.render();
                                            store.reload();
                                            storeWin.reload()
                                        }
                                    }
                                });
                            }
                        });
                    }
                },{
                    text:'关闭',
                    handler:function(){
                        win.close();
                    }
                }
            ]
        }).show();
        storeWin.load({params:{start:start,limit:pageSize}});
}

function remove_net(){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();

    var server_grid = Ext.getCmp('server.grid.info');
    var server_recode = server_grid.getSelectionModel().getSelected();
    if(!server_recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "是否移除子网？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UserPermissionAction_removeNetForUser.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{userId:recode.get("id"),pId:server_recode.get('id')},
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        server_grid.getStore().reload();
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

function importUser(){
    var grid = Ext.getCmp('grid.info');
    Ext.Msg.confirm("提示", "确认导入用户？", function(sid) {
        if (sid == "yes") {
            var myMask = new Ext.LoadMask(Ext.getBody(),{
                msg : '正在导入,请稍后...',
                removeMask : true
            });
            myMask.show();
            Ext.Ajax.request({
                url : "../../ImPortUserAction_ImportUser.action",
                timeout: 20*60*1000,
                method : "POST",
                success : function(r,o){
                    var respText = Ext.util.JSON.decode(r.responseText);
                    var msg = respText.msg;
                    myMask.hide();
                    Ext.Msg.alert("提示", msg);
                    grid.getStore().reload();
                },
                failure : function(r,o) {
                    var respText = Ext.util.JSON.decode(r.responseText);
                    var msg = respText.msg;
                    myMask.hide();
                    Ext.Msg.alert("提示", msg);
                }
            });
        }
    });
}

function cleanThreeYards(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "清除用户三码状态绑定？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UserPermissionAction_cleanThreeYards.action",
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

function findrouteusers(){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'user_name',		mapping:'user_name'},
        {name:'user_idcard',		mapping:'user_idcard'},
        {name:'user_province',		mapping:'user_province'},
        {name:'user_city',		mapping:'user_city'},
        {name:'user_organization',		mapping:'user_organization'},
        {name:'user_institution',		mapping:'user_institution'},
        {name:'user_phone',		mapping:'user_phone'},
        {name:'user_address',		mapping:'user_address'},
        {name:'user_email',		mapping:'user_email'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../UserPermissionAction_findRouteUsers.action?id="+recode.get("id")
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },record);
    var storeWin = new Ext.data.GroupingStore({
        proxy : proxy,
        reader : reader
    });
    var start = 0;
    var pageSize = 10;
    //var SelectArray = new Array();//记录集:所有选中的行号
    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect:true
//        listeners:{
//            rowselect:function(obj,rowIndex,record){
//                SelectArray[record.data.id] = record.data.id;
//                //往记录集中添加选中的行号,我这里直接保存了一个值
//            },
//            rowdeselect:function(obj,rowIndex,record){
//                delete SelectArray[record.data.id];
//            }
//        }
    });   //复选框
    var colM = new Ext.grid.ColumnModel([
        boxM,
        {header:"标识",   	 dataIndex:"id", hidden:true,align:'center',menuDisabled:true},
        {header:"用户名",  dataIndex:"user_name",	     align:'center',menuDisabled:true},
        {header:"身份证书",dataIndex:"user_idcard",	 align:'center',menuDisabled:true},
        {header:'操作标记',dataIndex:'show',            align:'center',sortable:true,menuDisabled:true,renderer:show,width:100}
    ]);

    /**
     * 操作标记
     * @param value
     */
    function show(value, p, r){
        return String.format(
            '<a id="terminal_user.info" href="javascript:void(0);" onclick="terminal_user();return false;" style="color: green;">终端信息</a>&nbsp;&nbsp;&nbsp;'
        );
    }

    var routeUserGrid = new Ext.grid.GridPanel({
        id:'grid.routeUserGrid.info',
        plain:true,
        autoScroll:true,
        height:330,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        cm:colM,
        sm:boxM,
        store:storeWin,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        bbar:new Ext.PagingToolbar({
            pageSize : pageSize,
            store:storeWin,
            displayInfo:true,
            displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
            emptyMsg:"没有记录",
            beforePageText:"当前页",
            afterPageText:"共{0}页"
        })
    });
    var win = new Ext.Window({
        title:"路由器用户",
        width:630,
        layout:'fit',
        height:430,
        modal:true,
        items:[{
            frame:true,
            items:[{
                layout:'fit',
                items:[{
                    xtype:'fieldset',
                    title:'路由器用户',
                    height:330,
                    items:[routeUserGrid]
                }]
            }]
        }]
    }).show();
    storeWin.load({params:{start:start,limit:pageSize}});
}


function terminal_user(){

    var grid_panel = Ext.getCmp("grid.routeUserGrid.info");
    var recode = grid_panel.getSelectionModel().getSelected();

    var record = new Ext.data.Record.create([
        {name:'id', mapping:'id'} ,
        {name:'terminal_name', mapping:'terminal_name'} ,
        {name:'terminal_type', mapping:'terminal_type'},
        {name:'user_name', mapping:'user_name'},
        {name:'terminal_status', mapping:'terminal_status'},
        {name:'terminal_desc', mapping:'terminal_desc'},
        {name:'ip', mapping:'ip'},
        {name:'mac', mapping:'mac'},
        {name:'on_line', mapping:'on_line'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../UserPermissionAction_findTerminalByRouteUser.action?id="+recode.get("id")
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.terminal.info",
        proxy:proxy,
        reader:reader
    });

    store.load();
    store.on('load',function(){
        var terminal_name = store.getAt(0).get('terminal_name');
        var terminal_type = store.getAt(0).get('terminal_type');
        var user_name = store.getAt(0).get('user_name');
        var terminal_status = store.getAt(0).get('terminal_status');
        var terminal_desc = store.getAt(0).get('terminal_desc');
        var ip = store.getAt(0).get('ip');
        var mac = store.getAt(0).get('mac');
        var on_line = store.getAt(0).get('on_line');

        var online_text;
        if(on_line=="0"){
            online_text = "离线";
        }else{
            online_text = "在线";
        }

        var terminal_status_text;
        if(terminal_status=="0"){
            terminal_status_text = "停用";
        }else{
            terminal_status_text = "启用";
        }
        Ext.getCmp('terminal.terminal_name').setValue(terminal_name);
        Ext.getCmp('terminal.terminal_type').setValue(terminal_type);
        Ext.getCmp('terminal.user_name').setValue(user_name);
        Ext.getCmp('terminal.terminal_status').setValue(terminal_status_text);
        Ext.getCmp('terminal.terminal_desc').setValue(terminal_desc);
        Ext.getCmp('terminal.ip').setValue(ip);
        Ext.getCmp('terminal.mac').setValue(mac);
        Ext.getCmp('terminal.on_line').setValue(online_text);
    });


    var formPanel = new Ext.form.FormPanel({
        frame:true,
        width:800,
        autoScroll:true,
        baseCls : 'x-plain',
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        layout:'form',
        border:false,
        defaults:{
            width:250
        },
        items:[
            new Ext.form.DisplayField({
                fieldLabel:'终端',
                id:'terminal.terminal_name'
            }),
            new Ext.form.DisplayField({
                fieldLabel:'终端类型',
                id:'terminal.terminal_type'
            }),
            new Ext.form.DisplayField({
                fieldLabel:'用户名',
                id:'terminal.user_name'
            }),
            new Ext.form.DisplayField({
                fieldLabel:'终端状态',
                id:'terminal.terminal_status'
            }),
            new Ext.form.DisplayField({
                fieldLabel:'终端描述',
                id:'terminal.terminal_desc'
            }),
            new Ext.form.DisplayField({
                fieldLabel:'IP',
                id:'terminal.ip'
            }),
            new Ext.form.DisplayField({
                fieldLabel:'MAC',
                id:'terminal.mac'
            }),
            new Ext.form.DisplayField({
                fieldLabel:'状态',
                id:'terminal.on_line'
            })
        ]
    });

    var select_Win = new Ext.Window({
        title:"终端信息",
        width:800,
        layout:'fit',
        height:380,
        modal:true,
        items:formPanel
    });
    select_Win.show();
}





