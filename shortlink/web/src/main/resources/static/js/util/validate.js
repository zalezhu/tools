/**
 * 身份证号码验证
 * 
 */
function isIdCardNo(num) {
	var factorArr = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4,
			2, 1];
	var parityBit = ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3",
			"2"];
	var varArray = [];
	var intValue;
	var lngProduct = 0;
	var intCheckDigit;
	var intStrLen = num.length;
	var idNumber = num;
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
		// check date
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
		if (varArray[17] != intCheckDigit) {
			return false;
		}
	} else { // length is 15
		// check date
		var date6 = idNumber.substring(6, 12);
		if (isDate6(date6) == false) {

			return false;
		}
	}
	return true;

}
/**
 * 判断是否为“MMDD”式的时期
 * 
 */
function isDate4(sDate) {
	if (!/^[0-9]{4}$/.test(sDate)) {
		return false;
	}
	var  month, day;
	month = sDate.substring(0, 2);
	day = sDate.substring(2, 4);
	if (month < 1 || month > 12)
		return false;
	if (day < 1 || day > 31)
		return false;
	return true
}
/**
 * 判断是否为“YYYYMM”式的时期
 * 
 */
function isDate6(sDate) {
	if (!/^[0-9]{6}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	if (year < 1700 || year > 2500)
		return false;
	if (month < 1 || month > 12)
		return false;
	return true
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
	var iaMonthDays = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
	if (year < 1700 || year > 2500)
		return false;
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
		iaMonthDays[1] = 29;
	if (month < 1 || month > 12)
		return false;
	if (day < 1 || day > iaMonthDays[month - 1])
		return false;
	return true
}

//验证名称是否已存在
jQuery.validator.addMethod("existName", function(value, element) {
	return this.optional(element) ;
}, "该名称已存在!");

// 身份证号码验证
jQuery.validator.addMethod("idcardno", function(value, element) {
	return this.optional(element) || isIdCardNo(value);
}, "请正确输入身份证号码");

// 字母数字
jQuery.validator.addMethod("alnum", function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
}, "只能包括英文字母和数字");

// 邮政编码验证
jQuery.validator.addMethod("zipcode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写邮政编码");

// 汉字
jQuery.validator.addMethod("chcharacter", function(value, element) {
	var tel = /^[\u4e00-\u9fa5]+$/;
	return this.optional(element) || (tel.test(value));
}, "请输入汉字");

// 字符最小长度验证（一个中文字符长度为2）
jQuery.validator.addMethod("stringMinLength", function(value, element, param) {
	var length = value.length;
	for ( var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length >= param);
}, $.validator.format("长度不能小于{0}!"));

// 字符最大长度验证（一个中文字符长度为2）
jQuery.validator.addMethod("stringMaxLength", function(value, element, param) {
	var length = value.length;
	for ( var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length <= param);
}, $.validator.format("长度不能大于{0}!"));

// 字符验证
jQuery.validator.addMethod("string", function(value, element) {
	return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "不允许包含特殊符号!");

// 手机号码验证
jQuery.validator
		.addMethod(
				"mobile",
				function(value, element) {
					var length = value.length;
					return this.optional(element)
							|| (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/
									.test(value));
				}, "手机号码格式错误!");

// 电话号码验证
jQuery.validator.addMethod("mobilephone", function(value, element) {
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
    var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/g;
    return this.optional(element) || (tel.test(value)) || (mobile.test(value));
}, "电话号码格式错误!");


// 电话号码验证
jQuery.validator.addMethod("phone", function(value, element) {
	var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
	return this.optional(element) || (tel.test(value));
}, "电话号码格式错误!");

// 电话号码验证(纯数字)
jQuery.validator.addMethod("num_phone", function(value, element) {
    var tel = /^(\d{3,4})?\d{7,9}$/g;
    return this.optional(element) || (tel.test(value));
}, "电话号码格式错误!");

// 邮政编码验证
jQuery.validator.addMethod("zipCode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "邮政编码格式错误!");

// 必须以特定字符串开头验证
jQuery.validator.addMethod("begin", function(value, element, param) {
	var begin = new RegExp("^" + param);
	return this.optional(element) || (begin.test(value));
}, $.validator.format("必须以 {0} 开头!"));

// 验证两次输入值是否不相同
jQuery.validator.addMethod("notEqualTo", function(value, element, param) {
	return value != $(param).val();
}, $.validator.format("两次输入不能相同!"));

// 验证值不允许与特定值等于
jQuery.validator.addMethod("notEqual", function(value, element, param) {
	return value != param;
}, $.validator.format("输入值不允许为{0}!"));

// 验证值必须大于特定值(不能等于)
jQuery.validator.addMethod("gt", function(value, element, param) {
	return value > param;
}, $.validator.format("输入值必须大于{0}!"));

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

jQuery.validator.addMethod("isDate", function(value, element) {
	var ereg = /^(\d{1,4})(-|\/)(\d{1,2})(-|\/)(\d{1,2})$/;
	var r = value.match(ereg);
	if (r == null) {
		return false;
	}
	var d = new Date(r[1], r[3] - 1, r[5]);
	var result = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d
			.getDate() == r[5]);
	return this.optional(element) || (result);
}, "请输入正确的日期");

jQuery.validator.addMethod("isDateTime", function(value, element) {
//	var ereg = /^(\d{1,4})(-|\/)(\d{1,2})(-|\/)(\d{1,2})$/;
	var ereg = /^(\d{1,4})(-|\/)(\d{1,2})(-|\/)(\d{1,2}) (20|21|22|23|[0-1]?\d):[0-5]?\d:[0-5]?\d$/;  
	var r = value.match(ereg);
	if (r == null) {
		return false;
	}
	var d = new Date(r[1], r[3] - 1, r[5]);
	var result = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d
			.getDate() == r[5]);
	return this.optional(element) || (result);
}, "请输入正确的日期");


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

jQuery.validator.addMethod("compareDate", function(value, element, param) {
	var ereg = /^(\d{1,4})(-|\/)(\d{1,2})(-|\/)(\d{1,2})$/;

	var startDate = jQuery(param).val();
   
	var sc = startDate.match(ereg);
	if (sc == null) {
		return false;
	}
	var ec = value.match(ereg);
	if (ec == null) {
		return false;
	}
	
	var sd = new Date(sc[1], sc[3] - 1, sc[5]);
	var ed = new Date(ec[1], ec[3] - 1, ec[5]);
    return sd <= ed;
}, "开始日期不能大于结束日期");

//比较两个时间大小
jQuery.validator.addMethod("compareTime", function(value, element, param) {
	var ereg = /^(20|21|22|23|[0-1]?\d)(\:)([0-5]?\d)(\:)([0-5]?\d)$/;
	var startTime = jQuery(param).val();
	//不是必填字段
	if(value==""){
		return true;
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
	}else if(sc[1]==ec[1]&&sc[3]<ec[3]){
		return true;
	}else{
		return false;
	}
}, "开始时间不能大于结束时间");