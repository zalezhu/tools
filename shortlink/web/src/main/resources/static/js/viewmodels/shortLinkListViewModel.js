requirejs(['jquery', 'knockout', 'shortLinkService', 'commonUtil', 'abstract', 'jquery-datatable', 'dialog', 'bootstrap', 'utility'], function ($, ko, shortLinkService, util, abstract) {

    class SLSearchForm{
        constructor(){
            this.slink = ko.observable('');
            this.llink = ko.observable('');
            this.createDate = ko.observable('');
            this.expireDate = ko.observable('');
        }
    }
    class SLAddForm{
        constructor(){
            this.llink = ko.observable('');
            this.expireDate = ko.observable('');
        }
    }
    class SLUpdateForm{
        constructor(){
            this.slink = ko.observable('');
            this.llink = ko.observable('');
            this.expireDate = ko.observable('');
        }
    }
    class ShortLinkListViewModel extends  abstract.AbstractViewModel{
        constructor(){
            super();
            this.slSearchForm = new SLSearchForm();
            this.slAddForm = new SLAddForm();
            this.slUpdateForm = new SLUpdateForm();
        }
        

        init(){
            super.init();
                this.setDataTables();

        };
        setDataTables(){
            let settings = SL.dataTable.defaultSetting();
            settings['sAjaxSource'] = SL.serverName + '/rest/shortlinks/';
            settings['fnServerParams'] = (aoData) => {
                SL.dataTable.setDefaultServerParams(aoData, this.slSearchForm);
            };

            settings['fnDrawCallback'] = ()=>{
                let self = this;
                $('.delete-shortlink').click(function(){
                    var $this = $(this);
                    var id = $this.data('id');
                    self.delShortlink(id);
                });
                $('.modify-shortlink').click(function(){
                    let $this = $(this);
                    let id = $this.data('id');
                    let llink = $this.data('llink');
                    let expireDate = $this.data('expiredate');
                    self.preUptShortlink(id,llink,expireDate);
                });
            };

            settings['fnErrorCallback'] = (xhr)=>{
                if(xhr.status != 200){
                    this.logout();
                }
            };
            let columns = [];
            columns.push(
                { //短裢
                    'sDefaultContent': '',
                    'fnRender': function (obj) {
                        let link= SL.shortDomain+obj.aData.slink;
                        let html = '<a href="'+link+'">'+link+'</a>';
                        return html;
                    }
                },
                {   'sDefaultContent': '', //长裢
                    'sClass': 'wordbreak',
                    'fnRender': function (obj) {
                        return obj.aData.llink;
                    }
                },
                { 	//创建时间
                    'sDefaultContent': '',
                    'fnRender': function (obj) {
                        return SL.DPGlobal.formatDateTime(obj.aData.createDate, 'yyyy-MM-dd');
                    }
                },{ 	//过期时间
                    'sDefaultContent': '',
                    'fnRender': function (obj) {
                        return SL.DPGlobal.formatDateTime(obj.aData.expireDate, 'yyyy-MM-dd');
                    }
                },
                { 	//操作
                    'sDefaultContent': '',
                    'fnRender': function (obj) {
                            var op = '<a href="javascript:void(0)" data class="modify-shortlink"  data-id="' + obj.aData.slink + '" data-llink="' + obj.aData.llink + '" data-expiredate="' + SL.DPGlobal.formatDateTime(obj.aData.expireDate, 'yyyy-MM-dd') + '">修改</a>';
                            op += '<a href="javascript:void(0)" data class="delete-shortlink" data-id="' + obj.aData.slink + '" >&nbsp;删除</a>';
                            return op;
                    }
                }
            );
            settings['aoColumns'] = columns;
            var dataTable = $('#shortlink-dynamic-table').dataTable(settings);
            util.bindSearchEvent("shortlinkSearch", "shortlinkSearchForm", () => {
                dataTable.fnDraw();
            });

        };

        // 添加跳转
        preAddShortlink(){
            $('#addShortlink').modal('show');
        };
        addShortlink(){
            var payload = ko.toJSON(this.slAddForm);
            var deferred = shortLinkService.addShortLink(payload);
            $.when(deferred).done(function (response) {
                if (response) {
                    amplify.publish('status.success', '成功', "新增短裢成功");
                    window.location = '/pages/list.html';
                } else {
                    amplify.publish('status.alerts', '失败', response.msg);
                }
            }).fail(function (error) {
                amplify.publish('status.alerts', '失败', error.responseJSON.msg);
            });
        };
        updateShortlink(){
            var payload = ko.toJSON(this.slUpdateForm);
            var deferred = shortLinkService.uptShortLink(this.slUpdateForm.slink(),payload);
            $.when(deferred).done(function (response) {
                if (response) {
                    amplify.publish('status.success', '成功', "更新短裢成功");
                    window.location = '/pages/list.html';
                } else {
                    amplify.publish('status.alerts', '失败', response.msg);
                }
            }).fail(function (error) {
                amplify.publish('status.alerts', '失败', error.responseJSON.msg);
            });
        }

        preUptShortlink(slink,llink,expireDate){
            this.slUpdateForm.slink(slink);
            this.slUpdateForm.llink(llink);
            this.slUpdateForm.expireDate(expireDate);
            $('#updateShortlink').modal('show');
        };
        delShortlink(id){
            $.dialog.confirm('删除短裢', '您确认要删除当前短裢'+SL.shortDomain+id+'吗？', function (res) {
                if (res) {
                    var deferred = shortLinkService.delShortLink(id);
                    $.when(deferred).done(function (response) {
                        if (response) {
                            amplify.publish('status.success', '成功', "删除成功");
                            window.location = '/pages/list.html';
                        } else {
                            amplify.publish('status.alerts', '删除失败', response.msg);
                        }
                    }).fail(function (error) {
                        amplify.publish('status.alerts', '删除失败', error.responseJSON.msg);
                    });
                }
            });
        };
    }

    const shortlinkListViewModel = new ShortLinkListViewModel();
    ko.applyBindings(shortlinkListViewModel);
    $(()=>{shortlinkListViewModel.init()});
});