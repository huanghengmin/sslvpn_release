Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name: 'gps', mapping: 'gps'},
        {name: 'gps_interval', mapping: 'gps_interval'},
        {name: 'terminal', mapping: 'terminal'},
        {name: 'terminal_interval', mapping: 'terminal_interval'},
        {name: 'view', mapping: 'view'},
        {name: 'view_interval', mapping: 'view_interval'},
        {name: 'threeyards', mapping: 'threeyards'},
        {name: 'strategy_interval', mapping: 'strategy_interval'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../../ClientStrategyAction_find.action"
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
        //var gps = store.getAt(0).get('gps');
        //var gps_interval = store.getAt(0).get('gps_interval');
        //var view = store.getAt(0).get('view');
        //var view_interval = store.getAt(0).get('view_interval');
        //var terminal = store.getAt(0).get('terminal');
        //var terminal_interval = store.getAt(0).get('terminal_interval');
        var threeyards = store.getAt(0).get('threeyards');
        var strategy_interval = store.getAt(0).get('strategy_interval');
        //Ext.getCmp('strategy.gps').setValue(gps);
        //Ext.getCmp('strategy.gps_interval').setValue(gps_interval);
        //Ext.getCmp('strategy.view').setValue(view);
        //Ext.getCmp('strategy.view_interval').setValue(view_interval);
        //Ext.getCmp('strategy.terminal').setValue(terminal);
        //Ext.getCmp('strategy.terminal_interval').setValue(terminal_interval);
        Ext.getCmp('strategy.threeyards').setValue(threeyards);
        Ext.getCmp('strategy.strategy_interval').setValue(strategy_interval);
    });


    var formPanel = new Ext.form.FormPanel({
        plain: true,
        width: 500,
        labelAlign: 'right',
        labelWidth: 220,
        defaultType: 'textfield',
        defaults: {
            width: 250,
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            /*{
             xtype: 'fieldset',
             title: '三码合一校验',
             width: 500,
             items: [*/new Ext.form.Checkbox({
                inputValue: 1,
                fieldLabel: '启用三码合一校验',
                id: "strategy.threeyards",
                regexText: '启用三码合一校验',
                name: 'threeyards',
                blankText: "启用三码合一校验"
            })/*]
             }*/,
            /*new Ext.form.Checkbox({
                inputValue: 1,
                fieldLabel: '启用 GPS',
                id: "strategy.gps",
                regexText: '启用 GPS',
                name: 'gps',
                blankText: "启用 GPS"
            }),
            new Ext.form.TextField({
                fieldLabel: 'GPS上报间隔(单位(/秒))',
                id: "strategy.gps_interval",
                regexText: 'GPS上报间隔(单位(/秒))',
                name: 'gps_interval',
                blankText: "GPS上报间隔(单位(/秒))"
            }),*/
           /* new Ext.form.Checkbox({
                inputValue: 1,
                fieldLabel: '启用 截屏',
                id: "strategy.view",
                regexText: '启用 截屏',
                name: 'view',
                blankText: "启用 截屏"
            }),
            new Ext.form.TextField({
                fieldLabel: '截屏上报间隔(单位(/秒))',
                id: "strategy.view_interval",
                regexText: '截屏上报间隔(单位(/秒))',
                name: 'view_interval',
                blankText: "截屏上报间隔(单位(/秒))"
            }),*/
            /*new Ext.form.Checkbox({
                inputValue: 1,
                fieldLabel: '上报终端信息',
                id: "strategy.terminal",
                regexText: '上报终端信息',
                name: 'terminal',
                blankText: "上报终端信息"
            }),
            new Ext.form.TextField({
                fieldLabel: '上报终端信息(单位(/秒))',
                id: "strategy.terminal_interval",
                regexText: '上报终端信息(单位(/秒))',
                name: 'terminal_interval',
                blankText: "上报终端信息(单位(/秒))"
            }),*/
            new Ext.form.TextField({
                fieldLabel: '策略更新间隔(单位(/秒))',
                id: "strategy.strategy_interval",
                regexText: '策略更新间隔(单位(/秒))',
                name: 'strategy_interval',
                blankText: "策略更新间隔(单位(/秒))"
            })
        ],
        buttons: [
            '->',
            {
                id: 'insert_win.info',
                text: '保存配置',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: "../../ClientStrategyAction_save.action",
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
            }
        ]
    });

    var panel = new Ext.Panel({
        plain: true,
        width: 800,
        border: false,
        items: [{
            id: 'panel.info',
            xtype: 'fieldset',
            title: '客户端策略配置',
            width: 600,
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


