/**
 * Created by Yanlun on 2015/8/2.
 */

jQuery.fn.valid = function(base){
    return function(){
        var result = base.call(this);
        this.find("input").filter('.error:first').focus();
        return result;
    };
}(jQuery.fn.valid);