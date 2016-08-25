/**
 * Created by Zale on 6/16/2014.
 */
define(['jquery', 'jquery-validate'], function ($) {
    //jquery.validate.js验证方法拓展
    $.validator.addMethod("positiveNumber", function (value, element) {
        var positiveNumberRegex = /^(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/;
        return this.optional(element) || positiveNumberRegex.test(value);
    }, "须输入非负数值");

    var mobile = /^(86){0,1}1\d{10}$/; //^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/
    //tel: "^[0-9\-()（）]{7,18}$", //电话号码的函数(包括验证国内区号,国际区号,分机号)
    var tel = /^(0[0-9]{2,3}\-())?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;

    //手机号码验证
    $.validator.addMethod("mobile", function (value, element) {
        var length = value.length;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "手机号码格式有误，请重新输入");

    //电话号码验证
    $.validator.addMethod("tel", function (value, element) {
        return this.optional(element) || (tel.test(value));
    }, '电话号码格式有误(固定电话输入格式为“区号-号码”,如:"0731-8888888")');

    //联系电话验证（可手机号，可固定电话号）
    $.validator.addMethod("phone", function (value, element) {
        var length = value.length;
        return this.optional(element) || (tel.test(value) || (length == 11 && mobile.test(value)));
    }, "联系电话格式有误（固定电话输入格式为“区号-号码”，如：“0731-8888888” ）");

    $.validator.addMethod("notEqualTo", function (value, element, params) {
        var notEqual = true;
        var paramsType = typeof params;
        if(paramsType == 'string'){//单值校验
            notEqual = (value === $( params ).val());
        }else if(paramsType == 'object'){//校验数组
            for(var index in params){
                var target = $( params[index] );
                if(value !== '' && value === target.val()){
                    notEqual = false;
                    return false;
                }
            }
        }
        return this.optional(element) || notEqual;
    }, "输入不能相同");


    //添加身份证号码验证规则
    $.validator.addMethod("isIdentityCardNumber", function (value, element) {
        return this.optional(element) || isIdentityCardNumber(value);
    }, "请正确输入您的身份证号码");

    // 验证值小数位数不能超过两位
    jQuery.validator.addMethod("decimal", function(value, element) {
        var decimal = /^-?\d+(\.\d{1,2})?$/;
        return this.optional(element) || (decimal.test(value));
    },"只能输入数字，并且小数位数不能超过两位");

    jQuery.validator.addMethod("regex", function(value, element, regexp) {
        var check = false;
        var re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, "输入值格式错误");

	function isValidDateRange(year, month, day){
		var iaMonthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        if (year < 1700 || year > 2500) return false;
        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1] = 29;
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > iaMonthDays[month - 1]) return false;
		return true;
	}
	
    /**
     * 判断是否为“YYMMDD”式的时期
     *
     */
    function isDate6(sDate) {
        if (!/^[0-9]{6}$/.test(sDate)) {
            return false;
        }
        var year, month, day;
        year = '19' + sDate.substring(0, 2);
        month = sDate.substring(2, 4);
        day = sDate.substring(4, 6);
		return isValidDateRange(year, month, day);
    }

    /**
     * 判断是否为“YYYYMMDD”式的时期
     *
     */
    function isDate8(sDate) {
        if (!/^[0-9]{8}$/.test(sDate)) {
            return false;
        }
        var year, month, day;
        year = sDate.substring(0, 4);
        month = sDate.substring(4, 6);
        day = sDate.substring(6, 8);
        return isValidDateRange(year, month, day);
    }

    //身份证规则验证
    function isIdentityCardNumber(identityCardNumber) {
        var factorArr = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1];
        var parityBit = ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"];
        var varArray = [];
        var intValue;
        var lngProduct = 0;
        var intCheckDigit;
        var intStrLen = identityCardNumber.length;
        var idNumber = identityCardNumber;
        // initialize
        if ((intStrLen != 15) && (intStrLen != 18)) {
            return false;
        }
        // check and set value
        for (i = 0; i < intStrLen; i++) {
            varArray[i] = idNumber.charAt(i);
            if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
                return false;
            } else if (i < 17) {
                varArray[i] = varArray[i] * factorArr[i];
            }
        }
        if (intStrLen == 18) {
            //check date
            var date8 = idNumber.substring(6, 14);
            if (isDate8(date8) == false) {
                return false;
            }
            // calculate the sum of the products
            for (i = 0; i < 17; i++) {
                lngProduct = lngProduct + varArray[i];
            }
            // calculate the check digit
            intCheckDigit = parityBit[lngProduct % 11];
            // check last digit
            if (varArray[17].toUpperCase() != intCheckDigit.toUpperCase()) {
                return false;
            }
        } else {//length is 15
            //check date
            var date6 = idNumber.substring(6, 12);
            if (isDate6(date6) == false) {
                return false;
            }
        }
        return true;
    }
    //判断是否为“HH:mm:ss”式的时间
    jQuery.validator.addMethod("isTime", function(value, element) {
        //不是必填字段
        if(value==""){
            return true;
        }

        var ereg = /^(20|21|22|23|[0-1]?\d)(\:)([0-5]?\d)(\:)([0-5]?\d)$/;
        var r = value.match(ereg);
        if (r == null) {
            return false;
        }
        var d = new Date();
        d.setHours(r[1]);
        d.setMinutes(r[3]);
        d.setSeconds(r[5]);
        var result = (d.getHours() == r[1] && d.getMinutes() == r[3]&& d.getSeconds() == r[5]);
        return this.optional(element) || (result);
    }, "请输入正确的时间");


    //比较两个时间大小
    jQuery.validator.addMethod("compareTime", function(value, element, param) {
        var ereg = /^(20|21|22|23|[0-1]?\d)(\:)([0-5]?\d)(\:)([0-5]?\d)$/;
        var startTime = jQuery(param).val();
        //不是必填字段
        if(value==""){
            return true;
        }
        if( !startTime ){
            return false;
        }
        var sc = startTime.match(ereg);
        if (sc == null) {
            return false;
        }
        var ec = value.match(ereg);
        if (ec == null) {
            return false;
        }
        if(sc[1]<ec[1]){
            return true;
        }else if(sc[1]==ec[1]&&sc[3]<=ec[3]){
            return true;
        }else{
            return false;
        }
    }, "开始时间不能大于结束时间");

    //判断是否为“HH:mm:ss”式的时间
    jQuery.validator.addMethod("isTime5", function(value, element) {
        //不是必填字段
        if(value==""){
            return true;
        }

        var ereg = /^(20|21|22|23|[0-1]?\d)(\:)([0-5]?\d)$/;
        var r = value.match(ereg);
        if (r == null) {
            return false;
        }
        var d = new Date();
        d.setHours(r[1]);
        d.setMinutes(r[3]);
        d.setSeconds(0);
        var result = (d.getHours() == r[1] && d.getMinutes() == r[3]);
        return this.optional(element) || (result);
    }, "请输入正确的时间");


    //比较两个时间大小
    jQuery.validator.addMethod("compareTime5", function(value, element, param) {
        var ereg = /^(20|21|22|23|[0-1]?\d)(\:)([0-5]?\d)$/;
        var startTime = jQuery(param).val();
        //不是必填字段
        if(value==""){
            return false;
        }
        if( !startTime ){
            return false;
        }
        var sc = startTime.match(ereg);
        if (sc == null) {
            return false;
        }
        var ec = value.match(ereg);
        if (ec == null) {
            return false;
        }
        if(sc[1]<ec[1]){
            return true;
        }else if(sc[1]==ec[1]&&sc[3]<=ec[3]){
            return true;
        }else{
            return false;
        }
    }, "开始时间不能大于结束时间");
});