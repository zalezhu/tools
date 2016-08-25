/**
 * Created by Zale on 7/17/2014.
 */
define(['jquery','knockout','moment'],
function($,ko,moment){
  var Months = function(startDate, endDate, callback, selectedDate){
    var me = this;
    this.older_disabled = ko.observable(false);
    this.newer_disabled = ko.observable(false);
    this.selectedDate =ko.observable();
    this.month_chooses = ko.observableArray([]);
    this.month_options = [];
    this.currentMonth=ko.observable('');

    this.selectedDate.subscribe(function(newValue){
      if (typeof newValue!='undefined')
        me.currentMonth(newValue.year()+'年'+(newValue.months()+1)+'月');
    });

    this.clickMonth = function(item){
      var current_date = item.start_date.startOf('month');
      me.selectedDate(current_date);
      for (var i = 0; i < me.month_options.length; i++){
        var op = me.month_options[i];
        if (op.disabled){
          op.disabled = false;
          break;
        }
      }
      item.disabled = true;
      var options = me.month_chooses();
      me.month_chooses([]);
      me.month_chooses(options);
      if(typeof callback == 'function') {
        callback(current_date._d);
      }
    };

    this.renderMonth = function(index){
      if (index < 0)
        index = 0;
      else if (index > me.month_options.length)
        index = me.month_options.length-1;
      var start = Math.floor(index / 6) * 6;
      var end = start + 6;
      if (end > me.month_options.length)
        end = me.month_options.length;
      me.older_disabled(start == 0);
      me.newer_disabled(end >= me.month_options.length);
      me.month_chooses(me.month_options.slice(start, end));
    };

    this.clickOlder = function(){
      var current = moment(me.selectedDate()).format('YYYY年MM月');
      for (var i = 0; i < me.month_options.length; i++){
        var item = me.month_options[i];
        if (current == item.text){
          me.renderMonth(i-6);
          break;
        }
      }
    };
    
    this.clickNewer = function(){
      var current = moment(me.selectedDate()).format('YYYY年MM月');
      for (var i = 0; i < me.month_options.length; i++){
        var item = me.month_options[i];
        if (current == item.text){
          me.renderMonth(i+6);
          break;
        }
      }
    };

    this.init = function(){
      var matched = false;
      var index =0;
      
      if (typeof startDate == 'undefined' && typeof endDate == 'undefined'){
        startDate = new Date();
        endDate = new Date();
        endDate.setMonth(startDate.getMonth()+7,1);
        endDate.setDate(endDate.getDate()-1);
      }
      if (!selectedDate)
    	  selectedDate = new Date(startDate.getTime());
      else
    	  selectedDate = new Date(selectedDate.getFullYear(), selectedDate.getMonth(),1);

      me.selectedDate(moment(selectedDate));
      while (startDate <= endDate){
        var break_date = moment(startDate).endOf('month');
        var item = {
          'text' : moment(startDate).format('YYYY年MM月'),
          'start_date': moment(startDate),
          'end_date': break_date.isAfter(moment(endDate)) ?moment(endDate) : break_date,
          'disabled': (me.selectedDate().month() == startDate.getMonth())
        };
        me.month_options.push(item);
        startDate.setMonth(startDate.getMonth()+1, 1);
        if (!matched && item.disabled)
          matched = true;
        if (!matched)
          index++;
      }
      //if(!matched){}
      me.renderMonth(index);
    }();
  };
  return Months;
});