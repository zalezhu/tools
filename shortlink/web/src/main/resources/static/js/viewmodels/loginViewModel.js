requirejs(
    ['jquery', 'knockout', 'commonUtil','loginService', 'bootstrap-popup', 'jquery-validate-extends'],
    function ($, ko, util,loginService) {

    class LoginViewModel{
        constructor(){
            this.userName = ko.observable('');
            this.password = ko.observable('');
            this.logging = ko.observable(false);
            this.error=ko.observable('');
            this.init();
            
        }
        init() {
            this.initValidator();
        };

        login() {
            if($(".form-signin").valid()) {

                this.logging(true);
                let json = {"userName": this.userName(), "password": this.password()};

                var payload = ko.toJSON(json);
                var deferred = loginService.login(payload);
                $.when(deferred).done((response) => {
                    if (response.token) {
                        localStorage["token"] = response.token;
                        localStorage["aid"] = this.userName();
                        window.location = '/pages/index.html';
                    } else {
                        this.error(response.error);
                        this.logging(false);
                    }
                }).fail((error) => {
                    this.error(error.responseJSON.msg);
                    this.logging(false);
                });
            }
        };


        initValidator(){
            let self = this;
            $(".form-signin").validate({
                rules: {
                    userName: {
                        required: true
                    },
                    password: {
                        required: true
                    }
                },
                messages: {
                    userName: {
                        required: '请输入用户名'
                    },
                    password: {
                        required: '请输入密码'
                    }
                },
                showErrors:  function(errorMap, errorList){
                    this.defaultShowErrors();
                }
            });

        };
    }
        let loginViewModel = new LoginViewModel();
    ko.applyBindings(loginViewModel);
});