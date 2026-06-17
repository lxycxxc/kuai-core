//创建折线图

function showZxt(id,xAxisKey,list){

    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById(id));

    var legend = [];
    var xAxis= [];
    var datas = {};
    var series = [];
    if(list!=null&&list.length>0){
        var dd = list[0];
        for(var k in dd){
            legend.push(k);
        }

        for(var i in list){
            var dd = list[i];
            for(var i in legend){
                var k = legend[i];
                var ll = datas[k];
                if(ll == null){ll = [];datas[k]=ll;}
                ll.push(dd[k]);
            }
        }
    }

    if(xAxisKey==null){//第一个字段
        xAxisKey = legend[0];
    }

    var index = legend.indexOf(xAxisKey);
    if(index>=0){
        xAxis = datas[xAxisKey];
        delete datas[xAxisKey];
        legend.splice(index,1)
    }

    for(var k in datas){
        series.push({
            name: k,
            type: 'line',
            data: datas[k]
        });
    }


    console.log("legend:"+JSON.stringify(legend))
    console.log("datas:"+JSON.stringify(datas))



    // 指定图表的配置项和数据
    option = {
        title: {
            text: '标题'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: legend
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxis
        },
        yAxis: {
            type: 'value'
        },
        series: series
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

