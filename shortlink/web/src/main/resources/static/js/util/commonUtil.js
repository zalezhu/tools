/**
 * Created by Zale on 6/16/2014.
 */
define(['jquery', 'knockout', 'amplify'], function ($, ko) {
    class CommonUtil{
        constructor(){
            String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g, "")};
            this.picHandler = {
                /**
                 * Receives an Image Object (can be JPG OR PNG) and returns a new Image Object compressed
                 * @param {Image} source_img_obj The source Image Object
                 * @param {Integer} scaling The output scaling of Image Object
                 * @return {Image} result_image_obj The compressed Image Object
                 */
                compress:  (source_img_obj, scaling, output_format) => {
                    let mime_type = "image/jpeg";
                    if (output_format != undefined && output_format == "png") {
                        mime_type = "image/png";
                    }
                    let cvs = document.createElement('canvas');
                    //naturalWidth真实图片的宽度
                    cvs.width = source_img_obj.naturalWidth;
                    cvs.height = source_img_obj.naturalHeight;
                    let ctx = cvs.getContext("2d").drawImage(source_img_obj, 0, 0);
                    let newImageData = cvs.toDataURL(mime_type, scaling);
                    let result_image_obj = new Image();
                    result_image_obj.src = newImageData;
                    return result_image_obj;
                }
            };
        }

        hasText(data){
            return data != null && typeof data != 'undefined' && data !=='';
        };

        isNotEmpty(data){
            return typeof data != 'undefined' && data != null && data.length > 0;
        };

        isEmpty(data){
            return !this.isNotEmpty(data);
        };


        getQueryString(name) {
            let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            let r = window.location.search.substr(1).match(reg);
            if (r != null) return decodeURIComponent(r[2]);
            return null;
        };

        registerInputSelect(inputElementId, loadOptions) {
            $('#' + inputElementId + ' input').keydown( (event) => {
                if (event.keyCode != 40 && event.keyCode != 38) {
                    let options = loadOptions;
                    if (options.length > 0) {
                        $('#' + inputElementId + ' ul').html('');
                        $.each(options,  (index, option) => {
                            let li = '<li>' + option + '</li>';
                            $('#' + inputElementId + ' ul').append(li);
                            $('#' + inputElementId + ' .input-select-list').show();
                        });
                        $('#' + inputElementId + ' ul li').mouseover(() => {
                            $(this).addClass('selected');
                        });

                        $('#' + inputElementId + ' ul li').mouseout(() => {
                            $(this).removeClass('selected');
                        });
                        $('#' + inputElementId + ' ul li').click( () => {
                            let val = $(this).html();
                            $('#' + inputElementId + ' input').val(val);
                            $('#' + inputElementId + ' .input-select-list').hide();
                        });
                    }
                }

            });

            $('#' + inputElementId).keydown((event) => {
                let currObj = $('#' + inputElementId + ' ul li.selected');
                if (event.keyCode == 40) {
                    if (typeof currObj.html() == 'undefined') {
                        currObj = $('#' + inputElementId + ' ul li').first().addClass('selected');
                    } else {

                        if (typeof currObj.next().html() != 'undefined') {
                            currObj.removeClass('selected');
                            currObj.next().addClass('selected');

                            let val = currObj.next().html();
                            $('#' + inputElementId + ' input').val(val);
                        }
                    }
                } else if (event.keyCode == 38) {
                    if (typeof currObj.html() != 'undefined') {
                        if (typeof currObj.prev().html() != 'undefined') {
                            currObj.removeClass('selected');
                            currObj.prev().addClass('selected');
                            let val = currObj.prev().html();
                            $('#' + inputElementId + ' input').val(val);

                        }
                    }
                }

            });
            $('body').click(() => {
                $('#' + inputElementId + ' .input-select-list').hide();
            });
        };

        //统计有效字符个数，全角字符对应两个半角字符
        statisticsCharacter(str) {
            return str.replace(/[^\x00-\xff]/ig, '**').length;//全角字符转换成两个半角字符，方便统计
        };

        //内容长度限制检测，默认的methodName为“customMaxLength”
        addMaxLengthValidatorMethod(targetStr, maxLength, methodName) {
            if (typeof(methodName) === 'undefined') {
                methodName = "customMaxLength";
            }
            $.validator.addMethod(methodName, (value, element) => {
                let length = this.statisticsCharacter(targetStr);
                return this.optional(element) || length <= maxLength;
            }, "内容长度超限，请限制在 <strong>" + maxLength + "</strong> 字符内（中文字符占两个字节）");
        };

        /**
         * 文件格式验证
         * @type {string[]}
         */
        doFileTypeValid (filePath, validFileType){
            //filePath为空，证明未选定文件
            if(this.hasText(filePath.trim())){
                let fileType = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
                if($.inArray(fileType, validFileType) == -1){
                    amplify.publish('status.alerts', '文件类型验证','上传文件格式有误，指定格式：'+validFileType);
                    return false;
                }else{
                    return true;
                }
            }
            return true;
        };
        fileTypeValid(filePath, validFileType){
            let paramType = typeof filePath;
            if(paramType == 'object'){
                let isValid = true;
                for(let index in filePath){
                    if(!this.doFileTypeValid(filePath[index], validFileType)){
                        isValid = false;
                        return;
                    }
                }
                return isValid;
            }else if(paramType == 'string'){
                return this.doFileTypeValid(filePath, validFileType);
            }else if(paramType == 'undefined'){
                return true;
            }else{
                console.info('filePath参数格式有误');
            }
        };

        //startRecord重置
        resetStartRecord(dataTable) {
            let osettings = dataTable.fnSettings();
            osettings._iDisplayStart = 0;
            dataTable.fnDraw(osettings);
        };

        scrollTop  (top) {
            let defaultTop = 0;
            if (!(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(top))) {//是否数字检测
                top = defaultTop;
            }
            $(window.parent).scrollTop(top);//重新加载数据后，scrollTop
        };

        AutoResizeImage (maxWidth, maxHeight, objImg) {
            let img = new Image();
            img.src = objImg.src;
            let hRatio;
            let wRatio;
            let Ratio = 1;
            let w = (img.width == 0) ? objImg.width : img.width;
            let h = (img.height == 0) ? objImg.height : img.height;
            wRatio = maxWidth / w;
            hRatio = maxHeight / h;
            if (maxWidth == 0 && maxHeight == 0) {
                Ratio = 1;
            } else if (maxWidth == 0) {
                if (hRatio < 1)
                    Ratio = hRatio;
            } else if (maxHeight == 0) {
                if (wRatio < 1)
                    Ratio = wRatio;
            } else if (wRatio < 1 || hRatio < 1) {
                Ratio = (wRatio <= hRatio ? wRatio : hRatio);
            }
            if (Ratio < 1) {
                w = w * Ratio;
                h = h * Ratio;
            }
            objImg.height = h;
            objImg.width = w;
        };

        /**
         * 18位的身份证号自动解析，生成对应的省市县代码
         * @param ID
         */
        autoAnalysisRegionByID(ID){
            let region = [];//省市县对应
            let length = ID.length;
            if(ID && length == 18){
                region.push(ID.substring(0,2) + '0000');
                region.push(ID.substring(0,4) + '00');
                region.push(ID.substring(0,6));
            }
            return region;
        };
        /**
         * 18位的身份证号自动解析，判定性别
         * @param ID
         */
        autoAnalysisGenderByID(ID){
            let FEMALE = 0, MALE = 1;
            let length = ID.length;
            if(ID && length == 18){
                return ID.charAt(16) % 2 == 0 ? FEMALE : MALE;
            }
            return MALE;
        };

        procRspCode (code) {
            if (code == 97) {
                window.location = '/pages/error/403.html';
            }
        };

        /*
         *ignoreProperty为数组类型
         *通过返回的Json数据（source）copy属性，target是需要设置数据的对象
         */
        copyProperties (options) {
            let target = options.target;
            let source = options.source;
            let ignoreProperty = options.ignoreProperty;
            let defaultValue = options.defaultValue;
            let ignorePropertyType = typeof(ignoreProperty);

            if (ignorePropertyType != "undefined") {
                if (ignorePropertyType != 'object') {
                    console.info('ignoreProperty参数须为object类型');
                    return false;
                }
                for (let property in target) {
                    if ($.inArray(property, ignoreProperty) == -1) {
                        this.setProperty(target, property, source[property], defaultValue);
                    }
                }
            } else {
                for (let property in target) {
                    this.setProperty(target, property, source[property], defaultValue);
                }
            }
        };

        setProperty  (target, propertyName, propertyValue, defaultValue) {
            let propertySetter = target[propertyName];
            if (typeof(propertySetter) != 'undefined') {
                if (typeof(propertyValue) != 'undefined') {
                    propertySetter(propertyValue);
                } else {
                    if (typeof(defaultValue) != 'undefined') {
                        propertySetter(defaultValue);
                    }
                }
            }
        };

        /*START html5图片压缩处理*/
        fileSelectedHandler(evt, elementId, scaling) {
            if (typeof FileReader != 'undefined') {
                let files = evt.target.files;
                if (typeof scaling == 'undefined' || scaling) {
                    scaling = 50 / 100;
                }
                for (let i = 0, file; file = files[i]; i++) {
                    // Only process image files.
                    if (!file.type.match('image.*')) {
                        continue;
                    }
                    let reader = new FileReader();
                    // Closure to capture the file information.
                    reader.onload = (function (theFile) {
                        return function (e) {
                            // Render thumbnail.
                            let element = document.getElementById(elementId);
                            element.src = event.target.result;
                            element.src = this.picHandler.compress(element, scaling).src;
                            element.style.display = "block";
                        };
                    })(file);
                    // Read in the image file as a data URL.
                    reader.readAsDataURL(file);
                }
            }
        };


        /*END html5图片压缩处理*/

        /*START 文档高度相关的操作*/

        /********************

         * 取窗口滚动条高度

         ******************/

        getScrollTop(){

            let scrollTop = 0;

            if (document.documentElement && document.documentElement.scrollTop) {

                scrollTop = document.documentElement.scrollTop;

            } else if (document.body) {

                scrollTop = document.body.scrollTop;

            }

            return scrollTop;
        };

        /********************

         * 取窗口可视范围的高度

         *******************/

        getClientHeight() {

            let clientHeight = 0;

            if (document.body.clientHeight && document.documentElement.clientHeight) {

                clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;

            } else {

                clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight : document.documentElement.clientHeight;

            }

            return clientHeight;
        };

        /********************

         * 取文档内容实际高度

         *******************/

        getScrollHeight() {

            return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);

        };
        /*END 文档高度相关的操作*/

        bindSelectAllEvent (bindTarget, filterName){
            $("#" + bindTarget).bind("click",  () =>{
                let checked = this.checked;
                let chks = $('input[name='+filterName+']');
                for (let i = 0; i < chks.length; i++) {
                    if(!$(chks[i]).prop('disabled')){
                        $(chks[i]).prop('checked', checked);
                    }
                }
            });
        };

        bindSearchEvent(searchBtnId, formId, callback, unbind){
            if(typeof unbind != 'undefined' && unbind){
                $("#" + searchBtnId).unbind('click');
                $("#" + formId).unbind('submit');
            }
            $("#" + searchBtnId).click( () =>{
                callback();
                return false;
            });
            //ENTER表单提交处理
            $("#" + formId).submit((event)=>{
                callback();
                event.preventDefault();
                return false;
            });
        };


	 selectAll(element, filterName){
		let checked = element.checked;
		let chks = $('input[name="'+filterName+'"]');
		for (let i = 0; i < chks.length; i++){
			if (!$(chks[i]).prop('disabled'))
				$(chks[i]).prop('checked',checked);
		}
	};
	
	 getSelectedValues(filterName){
		let vals = [];
		let chks = $('input[name="'+filterName+'"]');
    	for (let i = 0; i < chks.length; i++){
			let $this = $(chks[i]);
    		if ($this.prop('checked')){
				vals.push($this.val());		    			 
    		}    			
    	}
		return vals.join(",");
	};

        /**
         * 根据屏幕当前位置自动调整panel距离顶部的高度
         * @param target
         * @param panel
         * @returns {*}
         */
        calcAdaptiveTop(target, panel){
            let offset = target.offsetTop;
            let box = target.getBoundingClientRect();
            if((this.getClientHeight() - box.top) < panel.offsetHeight){
                return offset - panel.offsetHeight;
            }
            return offset + box.height;
        };
	
        go(frame,url){

        }

        init (){
            /*if (parent.$("#wndModal").attr("aria-hidden") === 'true') {
                commonUtil.scrollTop();
                commonUtil.adjustIframeHeight();
            }

            $(window).resize(function () {
                commonUtil.adjustIframeHeight();
            });
            amplify.subscribe('iframe.resize', function () {
                commonUtil.adjustIframeHeight();
            });*/
            commonUtil.scrollTop();
            let url = window.location.pathname;
            //console.log(url);
            if ((url.indexOf('login') != -1 || url.indexOf('403.html') != -1) && window.frameElement != null) {
                parent.window.location = window.location;
            }

           /* $(document).click(function () {
                if (parent && parent.$('.dropdown-toggle').length > 0) {
                    parent.$('.dropdown-toggle').dropdown('close');
                }
            });*/
        }
    }
    let commonUtil = new CommonUtil();
    $(document).ready(function () {
        commonUtil.init();
    });
    return commonUtil;
});