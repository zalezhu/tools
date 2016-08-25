define(['jquery'], function ($) {
    class ShortLinkService {
        constructor() {
            this.service = SL.serverName + "/rest";
        };
        getHead(){
            let token = localStorage.getItem("token");
            let aid = localStorage.getItem("aid");
            return {'token':token,'aid':aid};
        };
        addShortLink(payLoad) {
            var url = this.service + '/shortlink';
            var d = $.ajax({
                url: url,
                type: "POST",
                data: payLoad,
                headers: this.getHead(),
                contentType: 'application/json'
            });
            return d;
        };

        delShortLink(id) {
            var url = this.service + '/shortlink/' + id;
            var d = $.ajax({
                url: url,
                type: "DELETE",
                headers: this.getHead(),
                contentType: 'application/json'
            });
            return d;
        };

        uptShortLink(id, payLoad) {
            var url = this.service + '/shortlink/' + id;
            var d = $.ajax({
                url: url,
                type: "PUT",
                data: payLoad,
                headers: this.getHead(),
                contentType: 'application/json'
            });
            return d;
        };

        getShortLink(id) {
            var url = this.service + '/shortlink/' + id;
            var d = $.ajax({
                url: url,
                type: "GET",
                headers: this.getHead(),
                contentType: 'application/json'
            });
            return d;
        };
    }
    return new ShortLinkService();
});