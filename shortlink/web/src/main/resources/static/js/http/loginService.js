define(['jquery'],function($){
    class LoginService{
        constructor(){
            this.service = SL.serverName + "/user";
        }
        getHead(){
            let token = localStorage.getItem("token");
            let aid = localStorage.getItem("aid");
            return {'token':token,'aid':aid};
        };
        login(payLoad){
        var url = this.service+'/login';
        var d =  $.ajax({
            url: url,
            type: "POST",
            data: payLoad,
            headers:this.getHead(),
            contentType: 'application/json'
        });
        return d;
    };
        logout(){
        var url = this.service+'/logout';
        var d =  $.ajax({
            url: url,
            type: "DELETE",
            headers:this.getHead(),
            
            contentType: 'application/json'
        });
        return d;
    }
    }
    
    return new LoginService();
});