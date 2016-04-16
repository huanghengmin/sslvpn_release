Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'username', mapping:'username'} ,
         {name:'vpn_ip', mapping:'vpn_ip'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../IppAction_getAllIps.action" ,
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
        {header:"用户名称", dataIndex:"username", align:'center',sortable:true, menuDisabled:true,sort:true} ,
        {header:"分配IP地址", dataIndex:"vpn_ip", align:'center',sortable:true, menuDisabled:true}/*,
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
    } ;

   /* var tb = new Ext.Toolbar({
        autoWidth :true,
        autoHeight:true,
        items:[
            '用户记录'
        ]
    });*/

/*    var tbar = new Ext.Toolbar({
        autoWidth :true,
        autoHeight:true,
        items:[]
    });*/

    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
        title:'用户记录',
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
//        tbar : tb,
    /*    listeners:{
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

function show_flag(value, p, r){
    return String.format(
      ""
    );
}
