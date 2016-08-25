/**
 * Created by TR on 2015/9/5.
 */
define(['jquery', 'knockout'], function ($, ko) {
    var Options = {
        url: '',
        urlModel: true, //传递URL的模式
        deferredModel: false, //传递ajax请求的deferred模式（数据处理完成后回调）
        pagingCallback: null,
        opCallback: null,
        pageSize: 20,
        total: 0,
        postChange: function (data,callback) {
            console.log(data);
        },
        serverParam:{}

    };
    var Page = function (options) {
        var self = this;
        this.startRec = ko.observable(0);
        this.startPageRec = 0;
        this.endRec = ko.observable(0);
        this.pageSize = ko.observable(Options.pageSize);
        this.total = ko.observable(0);
        this.pageNo = ko.observableArray([]);
        this.activePage = ko.observable(0);
        this.postChange = Options.postChange;
        this.serverParam = Options.serverParam;
        this.firstPage = ko.observable(0);
        this.url = '';
        this.deferredModel = Options.deferredModel;
        this.opCallback = null;


        this.prePageDisabled = ko.computed(function(){
            return self.activePage() == 1 || self.total() == 0;
        },this);
        this.nextPageDisabled = ko.computed(function(){
            return self.pageNo().length == self.activePage() || self.total()==0;
        },this);

        this.active = function(page) {
            if(page != self.activePage()){
                self.activePage(page);
                if(self.url!='') {//传递URL，所有数据经过回调处理
//                self.startRec((page- 1) * self.pageSize() + 1);
                    self.startPageRec = (page- 1) * self.pageSize();
                    self.loadTableData(self.url, self.postChange);
                }else if(self.deferredModel){
                    self.startPageRec = (page- 1) * self.pageSize();
                    var page = {'startRecord':self.startPageRec,'maxRecords':self.pageSize()};
                    self.opCallback(page);
                }
            }
        };
        this.prev=function(){
            if(self.prePageDisabled()) {
                return false;
            }else{
                self.active(self.activePage()-1);
            }
        };
        this.next=function(){
            if(self.nextPageDisabled()){
                return false;
            }else{
                self.active(self.activePage()+1);
            }
        };

        this.loadTableData = function (url,postChange) {
            var serverParams = self.serverParam;
            for(var property in serverParams){
                var propertyValue = serverParams[property]();
                //非数据转换方法即可走下一步验证
                if (typeof propertyValue != 'undefined'
                    && propertyValue != null && propertyValue !== '') {
                    url +=  property + '=' + propertyValue +"&";
                }
            }
            url += 'iDisplayStart=' +self.startPageRec+'&iDisplayLength='+self.pageSize();
            var deferred = $.getJSON(url);
            $.when(deferred).done(function (response) {
                self.paging(response);
                postChange(response,self.reset);
            }).fail(function (error) {
                console.log(error);
            });
        };

        this.paging = function (page) {
            self.total(parseInt(page.total));
            if(self.total()>0) {
                self.pageSize(parseInt(page.pageSize));
                self.startRec(parseInt(page.page) * self.pageSize() + 1 );
                self.startPageRec = (parseInt(page.page)+1) * self.pageSize();
                self.endRec(Math.min((parseInt(page.page) + 1) * self.pageSize(), self.total()));
                self.activePage(parseInt(page.page) + 1);
                var pages = Math.ceil(self.total() / self.pageSize());
                if (pages <= 5) {
                    self.firstPage(1);
                } else {
                    if (self.activePage() + 2 <= pages) {
                        self.firstPage(self.activePage() - 2);
                    } else {
                        self.firstPage(self.activePage() - (4- (pages - self.activePage())));
                    }
                }

                self.pageNo([]);
                for (var i = 1; i <= pages; i++) {
                    self.pageNo.push(i);
                }
            }else if(self.total() == 0){
                self.startRec(0);
                self.endRec(0);
                self.pageNo([]);
            }
        };

        this.init = function () {
            if(typeof options.postChange!='undefined'){
                self.postChange = options.postChange;
            }
            if(typeof options.pageSize!='undefined'){
                self.pageSize(options.pageSize);
            }
            if(typeof options.serverParam!='undefined'){
                self.serverParam = options.serverParam;
            }
            if(typeof options.deferredModel!='undefined'){
                self.deferredModel = options.deferredModel;
            }
            if (options.url && options.url != '') {
                self.url=options.url;
                self.loadTableData(self.url,self.postChange);
            }else if(self.deferredModel
                && typeof options.pagingCallback == 'function' && typeof options.opCallback == 'function'){
                options.pagingCallback(self.paging);
                self.opCallback = options.opCallback;
//                self.opCallback();
            }
        }();

        this.reset = function(rsOp){
            self.startPageRec = 0;
            if(typeof rsOp.postChange!='undefined'){
                self.postChange = rsOp.postChange;
            }
            if(typeof rsOp.pageSize!='undefined'){
                self.pageSize(rsOp.pageSize);
            }
            if(typeof options.serverParam!='undefined'){
                self.serverParam = options.serverParam;
            }
            if(typeof options.deferredModel!='undefined'){
                self.deferredModel = options.deferredModel;
            }
            if (rsOp.url && rsOp.url != '') {

                self.url=rsOp.url;
                self.loadTableData(self.url,self.postChange);
            }else{
                //预留另一种实现
            }
        }

    };
    return Page;
});