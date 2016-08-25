/**
 * Created by Zale on 6/15/2014.
 */
requirejs(['jquery', 'knockout', 'knockout-mapping', 'moment','abstract', 'amplify','moment-locales','jquery-ui','utility'], function ($, ko, mapping, moment,abstract) {

    class IndexViewModel extends abstract.AbstractViewModel{
        constructor(){
            super();
           this.currentDate=ko.observable();
            this.init();
        }


        init() {
            super.init();
           
            //format当前日期
            moment.locale('zh-cn');
            this.currentDate(moment().format('YYYY年MM月DD日 dddd'));
            this.initEvents();
        };
       
    }
    const indexViewModel = new IndexViewModel();

    ko.applyBindings(indexViewModel);
});