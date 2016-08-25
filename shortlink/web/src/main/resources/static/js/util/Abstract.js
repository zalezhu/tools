define(['jquery','loginService','amplify','gritter'],function($,loginService){
    return {
        AbstractViewModel: class AbstractViewModel {  
           constructor() {
               this.screenCheck = $.fullscreen.isNativelySupported();
           };
           fullscreen(){
               if(this.screenCheck){
                   if($.fullscreen.isFullScreen()){
                       $.fullscreen.exit();
                   }else{
                       $('html').fullscreen({overflow:'auto'});}
               }else{
                   alert('Your browser does not support fullscreen mode.')
               }
           };
           init(){
               if(!localStorage.getItem("token")){
                   window.location = "/pages/login.html";
               }
               this.initEvents();
           };


           initEvents() {
               
               amplify.subscribe('status.alerts', function (title, content) {
                   if (typeof content == 'undefined') {
                       content = title;
                   }
                   title = title || '操作失败';
                   $.gritter.add({
                       // (string | mandatory) the heading of the notification
                       title: title,
                       // (string | mandatory) the text inside the notification
                       text: content,
                       class_name: 'error'
                   });
               });
               
               amplify.subscribe('status.success', function (title, content) {
                   if (typeof content == 'undefined') {
                       content = title;
                   }
                   title = title || '操作成功';
                   $.gritter.add({
                       // (string | mandatory) the heading of the notification
                       title: title,
                       // (string | mandatory) the text inside the notification
                       text: content,
                       class_name: 'success'
                   });
               });
               
           };
           
           
           logout(){
               var deferred = loginService.logout();
               $.when(deferred).done(function (response) {
                   if (response) {
                       localStorage.removeItem("token");
                       amplify.publish('status.success', '成功', "更新短裢成功");
                       window.location = '/pages/login.html';
                   } else {
                       amplify.publish('status.alerts', '失败', response.msg);
                   }
               }).fail(function (error) {
                   amplify.publish('status.alerts', '失败', error.responseJSON.msg);
               });
           }
       }
    }
});