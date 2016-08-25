/**
 * Created by Zale on 6/20/2014.
 */
define(['knockout'],function(ko){

  var SearchEPModel = function(id,title,conditionLabel,searchFunction,selectedFunction){
    var me = this;
    this.id = ko.observable(id);

    this.title= ko.observable(title);
    this.results = ko.observableArray([]);
    this.condition = ko.observable();
    this.conditionLabel = ko.observable(conditionLabel);
    this.searchLoading = ko.observable(false);
    this.selectedFunction = selectedFunction;
    this.init = function(){
      me.results([]);
      me.condition('');
    };

    this.afterSelected = function(element){
      var selected = $(element);
      me.init();
      me.selectedFunction(element.id,selected.text());
    };
    this.search = function(){
      me.searchLoading(true);
      searchFunction(me.condition(),me.searchCallBack)
    };

    this.searchCallBack = function(response){
      console.log(response);
      $.each(response, function (index, result) {
        me.results.push({name: result.value, id: result.key});
      });
      me.searchLoading(false);
    }
  };
  return SearchEPModel;
});