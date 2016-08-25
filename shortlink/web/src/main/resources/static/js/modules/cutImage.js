/**
 * Created by Zale on 12/11/2014.
 */
define(['knockout', 'jquery', 'jcrop', 'amplify'], function (ko, $) {
    var CutImageModel = function (id, preImgId, imgId, callback) {

        var CutPicModel = function (x, y, width, height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        };

        var me = this;
        this.id = ko.observable(id);
        this.preImgId = preImgId;
        this.imgId = imgId;
        this.callback = callback;
        this.cutPic = ko.observable();
        this.xsize = ko.observable();
        this.ysize = ko.observable();
        this.boundx = ko.observable();
        this.boundy = ko.observable();

        this.initView = function () {
            var parent = $('#' + me.preImgId).parent().parent();
            var url = parent.find(".fileupload-preview img").attr("src");

            var img = new Image();
            img.src = url;

            var xRatio = 640 / img.width,
                yRatio = 480 / img.height;

            if (img.width > 640) {
                img.width = img.width * xRatio;
                img.height = img.height * xRatio;
            }
            if (img.height > 480) {
                img.width = img.width * yRatio;
                img.height = img.height * yRatio;
            }
            var html = '<img src="' + url + '" id="' + me.id() + 'target" alt="" style="width:' + img.width + 'px;height:' + img.height + 'px"/>';

            $('#' + me.id() + ' .modal-body .image-area').html(html);
            me.printscreen();
        };

        this.printscreen = function () {
            var jcrop_api;
            $('#' + me.id() + 'target').Jcrop({
                //setSelect: [0, 0, 640, 480],
                //keySupport: false,
                //dragEdges: false,
                //allowSelect: false,
                //allowResize: false,
                onSelect: function (c) {
                    me.cutPic(new CutPicModel(c.x, c.y, c.w, c.h));
                },
                boxHeight: 480,
                boxWidth: 640,
                aspectRatio: 4 / 3,
                borderOpacity: 1
                //aspectRatio: xsize / ysize
            }, function () {
                // Use the API to get the real image size
                var bounds = this.getBounds();

                me.boundx(bounds[0]);
                me.boundy(bounds[1]);
                // Store the API in the jcrop_api variable
                jcrop_api = this;
            });
        };
        this.init = function () {
            var parent = $('#' + me.preImgId).parent().parent();
            var $pcnt = parent.find(".fileupload-preview");

            me.xsize($pcnt.width());
            me.ysize($pcnt.height());
            amplify.subscribe(me.id() + '.show', function () {
                $('#' + me.id()).modal({
                    backdrop: 'static'
                });
                me.initView();
            });
        }();

        this.updatePreview = function () {
            if (typeof me.cutPic() != 'undefined' && 
            		typeof me.cutPic().width != 'undefined' && parseInt(me.cutPic().width) > 0) {
                var parent = $('#' + me.preImgId).parent().parent(),
                    $pimg = parent.find(".fileupload-preview img");
                var rx = me.xsize() / me.cutPic().width;
                var ry = me.ysize() / me.cutPic().height;

                $pimg.css({
                    'max-width': 'none',
                    'max-height': 'none',
                    width: Math.round(rx * me.boundx()) + 'px',
                    height: Math.round(ry * me.boundy()) + 'px',
                    marginLeft: '-' + Math.round(rx * me.cutPic().x) + 'px',
                    marginTop: '-' + Math.round(ry * me.cutPic().y) + 'px'
                });
            }
            me.closeWin();
            me.callback(me.cutPic(), me.imgId);
        };
        this.closeWin = function () {
            $('#' + me.id()).modal('hide');
        }

    };

    return CutImageModel;
});