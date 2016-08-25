define(['jquery'], function ($) {
    /**
     * 财务图表Echarts帮助类
     */
    var FinancialChartUtil = {
        /**
         * 获得公共的图表配置对象
         * @param {string} title 图表标题
         */
        getOption: function(title){
            return {
                title : {
                    text: (title || '')
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: function (params) {
                        var tar = params[0];
                        return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
                    }
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                xAxis : [
                    {
                        type : 'category',
                        splitLine: {show:false},
                        data : []
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : []
            };
        }/** End getOption */

        /**
         * 获得x坐标数据
         */
        ,getXaxisData: function(jsonArray){
            var _arr = [];
            for (var item in jsonArray){
                _arr.push(jsonArray[item]);
            }
            return _arr;
        }/** End getXaxisData */

        /***
         * 获得图表数据
         * typeStr : 收入 支出
         */
        ,getSeriesData: function(json,xaxisArry,typeStr){
            if( xaxisArry ) {
                var returnArr = [];
                var assistantSeries = {
                    name: '辅助'+typeStr,
                    type: 'bar',
                    stack: typeStr,
                    itemStyle: {
                        normal: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        },
                        emphasis: {
                            barBorderColor: 'rgba(0,0,0,0)',
                            color: 'rgba(0,0,0,0)'
                        }
                    },
                    data: []
//                    data:[0, 1700, 1200, 300, 0]
                };
                var actualSeries = {
                    name: typeStr,
                    type: 'bar',
                    stack: typeStr,
                    itemStyle: { normal: {label: {show: true, position: 'inside'}}},
                    data: []
//                    data:[2900, 1200, 500, 900, 300]
                };

                var assistantSeriesData = [];
                var actualSeriesData = [];
                var alldata;
                for (var item in json) {
                    if (item == "alldata") {
                        alldata = json[item];
                        break;
                    }
                }
                for (var item in xaxisArry) {
                    if (json[item]) {
                        assistantSeriesData.push((parseFloat(alldata) - parseFloat(json[item])).toFixed(2));
                        actualSeriesData.push(parseFloat(json[item]).toFixed(2));
                        if (item != "alldata") {
                            alldata = (parseFloat(alldata) - parseFloat(json[item])).toFixed(2);
                        }
                    }
                }
                assistantSeries.data = assistantSeriesData;
                actualSeries.data = actualSeriesData;
                returnArr.push(assistantSeries);
                returnArr.push(actualSeries);
                return returnArr;
            }else{
                throw "类型不存在";
            }
        }/** End getSeriesData */

    };
    return FinancialChartUtil;
});