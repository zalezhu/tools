var bootstrapPopup = {
	_id 			: '_popup' + parseInt((1 + Math.random()) * 1000000, 10),
	_options 	: {
		sure : {
			text 		: '确定',
			onclick 	: function(){bootstrapPopup.close();}
		}, //OK键参数
		ignore: {
			text 		: '忽略',
			onclick 	: function(){bootstrapPopup.close();}
		}, //忽略键参数
		cancel: {
			text 		: '取消',
			onclick 	: function(){bootstrapPopup.close();}
		}, //取消键参数
		loading	: false, //读取键参数
		processingModel : false, //处理中，弹出loading，默认弹出框模式
		backgroundModel : "white", //背景模式，默认白色
		text		: '', //弹出信息框
		time		: 3, //多少秒后自动消失
		mask		: false,
		cls 		: 'modal-md'//尺寸，modal-md, modal-lg, modal-sm
	},
	backdrop:	null,//遮罩层
	backgroundModel: {
		white : 'white',
		black : 'black'
	},
	cls: {
		modal_md: "modal-md",
		modal_lg: "modal-lg",
		modal_sm: "modal-sm"
	},
	btnSuffix: {
		sure	: '_sure',
		ignore	: '_ignore',
		cancel	: '_cancel'
	}
};

bootstrapPopup.close = function(){
	if (bootstrapPopup._handle) {
		clearTimeout(bootstrapPopup._handle);
		bootstrapPopup._handle = null;
	}
	var box = $('#'+bootstrapPopup._id);
	if (box[0]) {
		box.modal('hide');
	}
};

bootstrapPopup.closeAfter = function(time) {
	if (time > 0) {
		bootstrapPopup._handle = setTimeout(
			function(){
				bootstrapPopup.close();
			}, time * 1000);
	}
};

bootstrapPopup.show = function(options) {
	if (bootstrapPopup._handle){
		bootstrapPopup.close();
		bootstrapPopup._handle = setTimeout(
			function(){
				bootstrapPopup.show(options);
			}, 500);
	}
	if (!options) {
		options = bootstrapPopup._options;
	}
	var box = bootstrapPopup.create(options);
	if (options.sure || options.ignore || options.cancel || options.loading) {
		options.time = 0;
	}
	box.modal({backdrop : options.mask ? 'static' : false, show : true});
	if (options.time > 0) {
		bootstrapPopup._handle = setTimeout(
			function(){
				bootstrapPopup.close();
			}, options.time * 1000);
	}
};

bootstrapPopup.create = function(options){
	if (!options) {
		options = bootstrapPopup._options;
	} else {
		for (var name in bootstrapPopup._options) {
			var org = bootstrapPopup._options[name];
			var opt = options[name];
			if (!opt ){
				//未配置时，附上默认值
				if (typeof(org) != 'object')
					options[name] = org;
			} else {
				if (typeof(org) == 'object'){
					for (var arg in org) {
						if (!opt[arg]) pt[arg] = org[arg];
					}
				}
			}
		}
	}

	//由于有提示框和loading框的切换，重构弹窗DIV容器，清除影响
	bootstrapPopup.restructurePopup();

	var box;
	if(!options.processingModel){
		var top = ($(window).outerHeight(true) - 140) / 2 - 100;
		box = $(
			'<div class="modal fade" id="' + bootstrapPopup._id + '" tabindex="-1" role="dialog" class="hidden" style="top: ' + top + 'px">'
			+'  <div class="modal-dialog" role="document">'
			+'    <div id="modal-content" class="modal-content modal-radius">'
			+'      <div class="modal-body" style="padding-bottom: 15px; padding-top: 25px">'
			+'      	<div class="container-fluid">'
			+'      	<div id="' + bootstrapPopup._id + '_body" class="row">'
			+'      		<div class="col-md-12 text-center">'
			+'      			<p id="' + bootstrapPopup._id + '_loading"><img src="../../static/images/loading/loading_60.gif"/></p>'
			+'					<p style="font-size: 16px" id="' + bootstrapPopup._id + '_text"></p>'
			+'      		</div>'
			+'      	</div>'
			+'      	</div>'
			+'      </div>'
			+'      	<div class="modal-footer" style="margin-top: 0; padding: 15px 20px 15px 20px">'
			+'      		<div class="text-center">'
			+'		        <button type="button" class="btn btn-success btn-sm" style="width: 160px" id="' + bootstrapPopup._id + '_sure">'
			+'		        	<i class="fa fa-check"></i>'
			+'		        	&nbsp;'
			+'		        	<span class="popup-btn-text" style="font-size: 14px; color:white"></span>'
			+'		        </button>'
			+'		        <button type="button" class="btn btn-warning btn-sm" id="' + bootstrapPopup._id + '_ignore">'
			+'		        	<span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span>'
			+'		        	&nbsp;'
			+'		        	<span class="popup-btn-text"></span>'
			+'		        </button>'
			+'		        <button type="button" class="btn btn-warning btn-sm" id="' + bootstrapPopup._id + '_cancel">'
			+'		        	<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>'
			+'		        	&nbsp;'
			+'		        	<span class="popup-btn-text"></span>'
			+'		        </button>'
			+'		    	</div>'
			+'      	</div>'
			+'    </div>'
			+'  </div>'
			+'</div>');
	} else {
		var top = ($(window).outerHeight(true) - 100) / 2 - 100;
		options.cls = bootstrapPopup.cls.modal_sm;
		var modelContentClass = options.text != '' ? ('modal-content-' + options.backgroundModel) : '';
		var loadingClass = "modal-spinner-" + options.backgroundModel;
		box = $(
			'<div class="modal fade" id="' + bootstrapPopup._id + '" tabindex="-1" role="dialog" class="hidden" style="top: ' + top + 'px">'
			+'  <div class="modal-dialog" role="document" style="min-width: 150px; max-width: 380px;">'
			+'    <div id="modal-content" class="modal-radius '+modelContentClass+'">'
			+'      <div class="modal-body" style="padding:5px;">'
			+'      	<div id="' + bootstrapPopup._id + '_body" class="row">'
			+'      		<div class="col-sm-12 col-md-12 col-lg-12 text-center">'
			+'      			<div class="modal-spinner '+loadingClass+'" id="' + bootstrapPopup._id + '_loading"></div>'
			+' 					<p class="tip" id="' + bootstrapPopup._id + '_text"></p>'
			+'      		</div>'
			+'      	</div>'
			+'      </div>'
			+'    </div>'
			+'  </div>'
			+'</div>');
	}
	box.on('hidden.bs.modal', function (event) {
		if (bootstrapPopup._handle){
			clearTimeout(bootstrapPopup._handle);
			bootstrapPopup._handle = null;
		}
	});
	$(document.body).append(box);

	var modal = box.find('div.modal-dialog');
	modal.removeClass();
	modal.addClass('modal-dialog ' + options.cls);

	bootstrapPopup.btnPreHandle(options.sure, bootstrapPopup.btnSuffix.sure);
	bootstrapPopup.btnPreHandle(options.ignore, bootstrapPopup.btnSuffix.ignore);
	bootstrapPopup.btnPreHandle(options.cancel, bootstrapPopup.btnSuffix.cancel);

	if (options.sure || options.ignore || options.cancel){
		box.find('div.modal-footer').show();
	}else{
		box.find('div.modal-footer').hide();
	}

	if (options.loading){
		if (options.text == bootstrapPopup._options.text){
			box.find('#' + bootstrapPopup._id + '_text').hide();
		}else{
			box.find('#' + bootstrapPopup._id + '_text').show();
		}
		box.find('#' + bootstrapPopup._id + '_body').click(null);
		box.find('#' + bootstrapPopup._id + '_loading').show();
	}else{
		if (!options.sure && !options.ignore && !options.cancel){
			box.find('div.modal-body').click(function(){
				bootstrapPopup.close();
			});
		}
		box.find('#' + bootstrapPopup._id + '_loading').hide();
		box.find('#' + bootstrapPopup._id + '_text').show();
	}
	box.find("#"+bootstrapPopup._id + '_text').text(options.text);

	return box;
};

bootstrapPopup.restructurePopup = function(){
	var box = $('#'+bootstrapPopup._id);
	bootstrapPopup.backdrop = box.next();
	bootstrapPopup.backdrop.remove();
	box.remove();
};

bootstrapPopup.changeTips = function(tips){
	$('#'+bootstrapPopup._id).find("#"+bootstrapPopup._id + '_text').text(tips);
};

bootstrapPopup.btnPreHandle = function(btnOptions, btnSuffix){
	var btn = $('#'+bootstrapPopup._id).find('#' + bootstrapPopup._id + btnSuffix);
	if (btnOptions){
		btn.show();
		btn.find('.popup-btn-text').html(btnOptions.text);
		btn.click(btnOptions.onclick);
	}else{
		btn.hide();
	}
};