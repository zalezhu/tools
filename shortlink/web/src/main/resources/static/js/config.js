/**
 * Created by Zale on 6/12/2014.
 */
const SL = {
    serverName: "",
    shortDomain: "http://222.240.1.16:5099/"
};

requirejs.config({
    baseUrl: SL.serverName + '/',
    paths: {
        'jquery': 'shared/vendor/jquery/jquery-1.11.1.min',
        'jquery-ui': 'shared/vendor/jquery/jquery_ui/jquery-ui.min',
        'jquery-validate': 'shared/js/jquery.validate.min',
        'additional-methods': 'shared/js/additional-methods.min',
        'bootstrap': 'shared/js/bootstrap.min',
        'knockout': 'shared/js/knockout-3.3.0',
        'knockout-mapping': 'shared/js/knockout.mapping-latest',
        'knockout-amd-helpers': 'shared/js/knockout-amd-helpers.min',//template用
        'amplify': 'shared/js/amplify.core',  //http://amplifyjs.com/api/pubsub/
        'text': 'shared/js/text',
        'utility': 'shared/assets/js/utility/utility',
       //  'DB_gallery': 'shared/js/jquery.DB_gallery',
       //  'jquery-nicescroll': 'shared/js/jquery.nicescroll',//滚动条
       //  'jquery-stepy': 'shared/js/jquery.stepy',
       //  'jquery-nanoscroller': 'shared/js/jquery.nanoscroller.min',
       //  'jquery-migrate': 'shared/js/jquery-migrate-1.2.1.min',
       //  'jquery-ui-custom-192': 'shared/js/jquery-ui-1.9.2.custom.min',
       //  'scripts': 'shared/js/scripts',
        'bootstrap-popup': 'shared/js/bootstrap-popup',
       //  'jquery-sparkline':'shared/js/jquery.sparkline.min',
       //
       //  //Components
       //  'jquery.multi-select': 'shared/components/jquery-multi-select/js/jquery.multi-select', //多选
        'jquery-datatable': 'shared/js/advanced-datatable/js/jquery.dataTables',
        'DT-bootstrap': "shared/js/data-tables/DT_bootstrap",     //datatable要用
       //  'bootstrap-datepicker': 'shared/components/bootstrap-datepicker/js/bootstrap-datepicker',
       //  'bootstrap-datepicker-zh': 'shared/components/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN',
       //  'bootstrap-timepicker': 'shared/components/bootstrap-timepicker/js/bootstrap-timepicker',
       //  'bootstrap-datetimepicker': 'shared/components/bootstrap-datetimepicker/js/bootstrap-datetimepicker',
       //  'bootstrap-datetimepicker-zh': 'shared/components/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN',
       //  'daterangepicker': 'shared/components/bootstrap-daterangepicker/daterangepicker',
       //  'moment.min': 'shared/components/bootstrap-daterangepicker/moment.min',
       //  'bootstrap-wysihtml5': 'shared/components/bootstrap-wysihtml5/bootstrap-wysihtml5', //editor
       //  'wysihtml5': 'shared/components/bootstrap-wysihtml5/wysihtml5-0.3.0',
       //  'ckeditor': 'shared/components/ckeditor/ckeditor',
       //  'jquery-ckeditor': 'shared/components/ckeditor/adapters/jquery',
       //  'jquery-tagsinput': 'shared/components/jquery-tags-input/jquery.tagsinput',
        'gritter': 'shared/js/gritter/js/jquery.gritter',
        'moment-locales': 'shared/vendor/plugins/moment/moment-with-locales',
        'moment': 'shared/vendor/plugins/moment/moment.min',
       //  'fullcalendar': 'shared/components/fullcalendar/fullcalendar',
       //  'fullcalendar_lang': 'shared/components/fullcalendar/lang-all',
       //  'morris': 'shared/components/morris-chart/morris.min',
       //  'raphael': 'shared/components/morris-chart/raphael-min',
       //  'jcrop': 'shared/components/printscreen/jquery.Jcrop.min',
       //  'iosSwitch': 'shared/components/ios-switch/switchery',
       //  'pinyin': 'shared/components/pinying/pinyinEngine.full',
       //  'dhtmlxtree':'shared/components/dhtmlxtree/dhtmlxtree',
       //  'bootstrap-wizard': 'shared/components/twitter-wizard/jquery.bootstrap.wizard.min',
       //  'range-slide': 'shared/components/ion.rangeSlider-2.0.12/js/ion-rangeSlider/ion.rangeSlider.min',
       //  'pace':'shared/components/pace/pace.min',
       //  'flot':'shared/components/flot-chart/jquery.flot',
       //  'flot-tooltip':'shared/components/flot-chart/jquery.flot.tooltip',
       //  'flot-resize':'shared/components/flot-chart/jquery.flot.resize',
       //  'flot-categories':'shared/components/flot-chart/jquery.flot.categories',
       //  'flot-time':'shared/components/flot-chart/jquery.flot.time',
       //  'spinner': 'shared/components/fuelux/js/spinner.min',
       //  'iCheck': 'shared/components/iCheck/jquery.icheck',
       //  'raty': 'shared/components/raty/lib/jquery.raty',
       //
       //  //custom validate rules
        'jquery-validate-rules': 'js/custom/jquery.validate.rules',
        'jquery-validate-extends': 'js/custom/jquery.validate.extends',
       //  'jquery-ui-custom': 'js/custom/jquery-ui-custom',
        'jquery-validate-message': 'js/util/message_cn',//jquery validator 中文message
       //
       //  //配置Util
        'dialog': 'js/util/dialog',
        'abstract': 'js/util/Abstract',
        'commonUtil': 'js/util/commonUtil', //通用工具
       //  'map': 'js/util/map',
       //
       // // 配置echarts
       //  'echarts':'shared/components/echarts/build/dist',
       //  'financialChartUtil':'js/util/chart/financialChartUtil',
       //
       //
       //  //配置http service
        'shortLinkService' : 'js/http/shortLinkService',
        'loginService' : 'js/http/loginService'

    },
    shim: {
        'jquery-ui':['jquery'],
        'utility':['jquery'],
        'jquery-validate': ['jquery'],
        'jquery-validate-rules': ['jquery','jquery-validate'],
        'jquery-validate-message': ['jquery','jquery-validate'],
        'jquery-validate-extends': ['jquery', 'jquery-validate', 'jquery-validate-message', 'jquery-validate-rules','additional-methods'],
        'additional-methods': ['jquery', 'jquery-validate'],
        'amplify': {
            deps: ['jquery']
            , exports: 'amplify'
        },
        'gritter': ['jquery'],
        // 'jquery-datatable': ['jquery'],
        // 'jquery-form': ['jquery'],
        // 'DT-bootstrap': ['jquery', 'jquery-datatable'],
        // 'ajaxfileupload': ['jquery'],
        'bootstrap': ['jquery']
        // 'bootstrap2': ['jquery'],
        // 'bootstrap-fileupload': ['jquery'],
        // 'bootstrap-fileupload-old': ['jquery'],
        // 'jquery-nicescroll': ['jquery'],
        // 'jquery-ui-custom-192': ['jquery'],
        // 'fullcalendar': ['jquery'],
        // 'jquery-stepy': ['jquery'],
        // 'jquery-migrate': ['jquery'],
        // 'scripts': ['jquery', 'jquery-ui', 'bootstrap','jquery-migrate','jquery-nicescroll'],//'jquery-nicescroll'
        // 'bootstrap-popup': ['jquery', 'bootstrap'],
        // 'jquery-ui': ['jquery'],
        // 'jquery-ui-custom': ['jquery','jquery-ui'],
        // 'bootstrap-datepicker': ['jquery'],
        // 'bootstrap-datepicker-zh': ['jquery','bootstrap-datepicker'],
        // 'bootstrap-timepicker': ['jquery'],
        // 'bootstrap-datetimepicker': ['jquery'],
        // 'bootstrap-datetimepicker-zh': ['jquery','bootstrap-datetimepicker'],
        // 'jquery.multi-select': ['jquery'],
        // 'bootstrap-wysihtml5': ['jquery', 'wysihtml5'],
        // 'jquery-tagsinput': ['jquery'],
        // 'jquery-ckeditor': ['jquery', 'ckeditor'],
        // 'iosSwitch': ['jquery'],
        // 'morris': ['jquery', 'raphael'],
        // 'datepicker_lang': ['jquery', 'datepicker'],
        // 'jcrop': ['jquery'],
        // 'range-slide': ['jquery'],
        // 'flot':['jquery'],
        // 'flot-tooltip':['flot'],
        // 'flot-resize':['flot'],
        // 'flot-categories':['flot'],
        // 'flot-time':['flot'],
        // 'raty':['jquery']
    }
    
});
//datatable配置
SL.dataTable = {
    defaultSetting: function () {
        return {
            'bFilter': false, 					//是否使用内置的过滤功能
            'bLengthChange': false, 	//是否允许自定义每页显示条数.
            'iDisplayLength': 20, 			//最大显示多少条记录
            'bProcessing': true, 			//当datatable获取数据时候是否显示正在处理提示信息。
            'bSort': false,					//是否显示排序
            'bServerSide': true,
            'sAjaxDataProp': 'result',
            'sServerMethod': 'GET',
            'autoScroll' : true,
            'fnPreSearchCallback': function (setting) {
                setting._iDisplayStart = 0;
            },
            'fnPreDrawCallback': function (setting) {
                $("#" + setting.sTableId + ' tbody').empty();
                if(setting.autoScroll){
                    if(typeof setting.scrollTarget === 'undefined'){
                        //default
                        $(window.parent).scrollTop(0);//重新加载数据后，scrollTop
                    }else{
                        // jQuery("html,body").animate({scrollTop: jQuery("#" + setting.scrollTarget).offset().top - jQuery('.header-section').height()}, 100);
                        $(window.parent).scrollTop(jQuery("#" + setting.scrollTarget).offset().top - jQuery('.header-section').height());
                    }
                }
            },
            'oLanguage': {
                'sProcessing':
                    ('<div style="position: relative; height: 32px; margin-bottom: 10px">' +
                    '<img src="/images/loading/loading_orange.gif" style="width: 32px; height: 32px; position: absolute; left: 0; top: 0;">' +
                    '<p style="margin: 0 0 0 40px; font-size: 17px; position: relative; top: 7px; padding: 0">正在努力加载...</p>' +
                    '</div>'),
                'sLengthMenu': '_MENU_ 记录/页',
                'sZeroRecords': '没有匹配的记录',
                'sInfo': '显示第 _START_ 至 _END_ 项记录，共 _TOTAL_ 项',
                'sInfoEmpty': '显示第 0 至 0 项记录，共 0 项',
                'sInfoFiltered': '(由 _MAX_ 项记录过滤)',
                'sInfoPostFix': '',
                'sSearch': '过滤:',
                'sUrl': '',
                'oPaginate': {
                    'sFirst': '首页',
                    'sPrevious': '上页',
                    'sNext': '下页',
                    'sLast': '末页'
                }
            }
        };
    },
    /*
     *DataTables配置
     *为DataTables组件设置默认的服务参数
     *aoData.push({ "name": property, "value": propertyValue});
     *name值与propertyValue的key一致
     * var defaultProperty = 'convertData';数据加工，写在属性之前
     */
    setDefaultServerParams: function (aoData, searchParams) {
        //初始化检测
        if (typeof(searchParams) != 'object') {
            console.info('searchParams参数必须为对象');
            return false;
        }
        //自定义数据转换，如time的转换等
        var defaultDataConversionFuncName = 'dataConversion';
        var dataConversion = searchParams[defaultDataConversionFuncName];
        if (typeof dataConversion == 'function') {
            dataConversion();
        }
        //param初始化配置
        var propertyValue;
        for (var property in searchParams) {
            propertyValue = searchParams[property]();
            //非数据转换方法即可走下一步验证
            if (property !== defaultDataConversionFuncName
                && typeof propertyValue != 'undefined'
                && propertyValue != null && propertyValue !== '') {
                aoData.push({
                    'name': property,
                    'value': propertyValue
                });
            }
        }
    }
};
//日期工具类
SL.DPGlobal = {
    modes: [
        {
            clsName: 'days',
            navFnc: 'Month',
            navStep: 1
        },
        {
            clsName: 'months',
            navFnc: 'FullYear',
            navStep: 1
        },
        {
            clsName: 'years',
            navFnc: 'FullYear',
            navStep: 10
        }],
    dates: {
        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
        daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
        months: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
        monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
    },
    isLeapYear: function (year) {
        return (((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0))
    },
    getDaysInMonth: function (year, month) {
        return [31, (BCar.DPGlobal.isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month]
    },
    parseFormat: function (format) {
        var separator = format.match(/[.\/\-\\s].*?/),
            parts = format.split(/\W+/);
        if (!separator || !parts || parts.length === 0) {
            throw new Error("Invalid date format.");
        }
        return {separator: separator, parts: parts};
    },
    parseDate: function (date, format) {
        if (typeof format === 'string') {
            format = this.parseFormat(format);
        }
        var parts = date.split(format.separator),
            date = new Date(),
            val;
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        if (parts.length === format.parts.length) {
            for (var i = 0, cnt = format.parts.length; i < cnt; i++) {
                val = parseInt(parts[i], 10) || 1;
                switch (format.parts[i]) {
                    case 'dd':
                    case 'd':
                        date.setDate(val);
                        break;
                    case 'mm':
                    case 'm':
                        date.setMonth(val - 1);
                        break;
                    case 'yy':
                        date.setFullYear(2000 + val);
                        break;
                    case 'yyyy':
                        date.setFullYear(val);
                        break;
                }
            }
        }
        return date;
    },
    formatDate: function (date, format) {
        if (typeof format === 'string') {
            if (!format || format == '') {
                format = 'yyyy-MM-dd hh:mm:ss';
            }
            format = this.parseFormat(format);
        }
        if (typeof date === 'string' || typeof date === 'number') {
            date = new Date(date);
        }

        var val = {
            d: date.getDate(),
            m: date.getMonth() + 1,
            yy: date.getFullYear().toString().substring(2),
            yyyy: date.getFullYear()
        };
        val.dd = (val.d < 10 ? '0' : '') + val.d;
        val.mm = (val.m < 10 ? '0' : '') + val.m;
        var date = [];
        for (var i = 0, cnt = format.parts.length; i < cnt; i++) {
            date.push(val[format.parts[i]]);
        }
        return date.join(format.separator);
    },
    formatDateTime: function (date, format) {
        if (!format || format == '')
            format = 'yyyy-MM-dd hh:mm:ss';
        if (typeof date === 'string' || typeof date === 'number')
            date = new Date(date);
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var days = date.getDate();
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        var res = format.match(/([yY]+)/);
        if (res) format = format.replace(res[0], year.toString().substr(res[0].length - 4, 4 - (res[0].length - 4)));
        res = format.match(/(M+)/);
        if (res) format = format.replace(res[0], (res[0].length == 2 && month < 10 ? '0' + month : month));
        res = format.match(/([dD]+)/);
        if (res) format = format.replace(res[0], (res[0].length == 2 && days < 10 ? '0' + days : days));
        res = format.match(/([hH]+)/);
        if (res) format = format.replace(res[0], (res[0].length == 2 && hours < 10 ? '0' + hours : hours));
        res = format.match(/([m]+)/);
        if (res) format = format.replace(res[0], (res[0].length == 2 && minutes < 10 ? '0' + minutes : minutes));
        res = format.match(/([Ss]+)/);
        if (res) format = format.replace(res[0], (res[0].length == 2 && seconds < 10 ? '0' + seconds : seconds));
        return format;
    },
    checkDateTime: function (value) {
        var pattern = /^\d{4}-\d{1,2}-\d{1,2}$/;
        if (pattern.exec(value)) {
            return true;
        }
        return false;
    },
    getTimeStr: function (str) {
        return new Date(str).getTime();
    }
};