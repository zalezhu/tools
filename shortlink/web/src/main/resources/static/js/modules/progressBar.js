/**
 * Created by Yanlun on 05/29/2015.
 */
define(['knockout', 'jquery','commonUtil', 'amplify'], function (ko, $, util) {
    var ProgressBarModel = function (callback,tipPrefix) {
        var me = this;
		var interval;
		this.waitUploadFilesTotal = ko.observable(-1);
		this.uploadFileTotal = ko.observable(-1);
		this.showProgressBar = ko.observable(false);
		this.progressPercent = ko.observable(0);
		this.tipPrefix = ko.computed(function(){
			return (typeof(tipPrefix) != 'undefined') ? tipPrefix : '已完成';
		});
		
		this.init = function(waitUploadFilesTotal){
			me.progressPercent(0);
			me.uploadFileTotal(waitUploadFilesTotal);
			me.showProgressBar(true);
			interval = setInterval(increment, 580);
		};
		
		function calcProgressPercent(){
			return parseInt(100.0 * (1.0 - (me.waitUploadFilesTotal() / me.uploadFileTotal())));
		}
		this.existsUploadSuccessCallBack = function(){
			var calcProgressPercentResult = calcProgressPercent();
			me.progressPercent(me.progressPercent()>calcProgressPercentResult ? me.progressPercent() : calcProgressPercentResult);
		};
		
		this.updateProgressBarCallback = function(waitUploadFilesTotal, showProgressBar){
			if(typeof(waitUploadFilesTotal) == 'undefined'){
				console.info('waitUploadFilesTotal须指定');
				return;
			}
			if(typeof(showProgressBar) != 'undefined' && showProgressBar){
				me.init(waitUploadFilesTotal);
			}
			if(me.waitUploadFilesTotal() > waitUploadFilesTotal){
				me.waitUploadFilesTotal(waitUploadFilesTotal);
				me.existsUploadSuccessCallBack();
			}else{
				me.waitUploadFilesTotal(waitUploadFilesTotal);	
			}
		};
		
		function increment(){
			if(me.waitUploadFilesTotal() == -1 && me.uploadFileTotal() == -1){
				return;
			}
			if(me.progressPercent() >= 100){
				clearInterval(interval);
			}else{
				me.progressPercent(me.progressPercent() + 1);
			}
		}
		callback(me.updateProgressBarCallback);
    };
	return ProgressBarModel;
});