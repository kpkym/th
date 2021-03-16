import React, {Component} from 'react';
import {VictoryChart, VictoryLine, VictoryScatter, VictoryZoomContainer} from 'victory';
import {outOfStockPrice} from 'util/utils'

function getPrice(item, arr) {
    // 不缺货
    if (item.price !== outOfStockPrice){
        return item.price;
    }

    // 缺货，选择前一个价格
    for (let i = arr.findIndex(e => e === item) - 1; i > -1; i--) {
        if (arr[i] !== outOfStockPrice) {
            return arr[i].price;
        }
    }
    // 去掉所有缺货标签后，选择第一个
    // 如果去掉所有之后为空，则返回0
    let outOfStockArr = arr.map(e => e.price).filter(e => e !== outOfStockPrice);
    return outOfStockArr.length > 0 ? outOfStockArr[0] : 0;
}

class PriceHistoryChart extends Component {
    static propTypes = {};

    render() {
        const {chartData} = this.props;
        return (
            <VictoryChart width={1000} height={150} domainPadding={{x: [30, 30], y:[5, 5]}} containerComponent={<VictoryZoomContainer/>}>
                <VictoryLine style={{data: {stroke: "#c43a31"}}}
                    labels={({datum}) => datum.price === outOfStockPrice ? "缺货" : datum.price}
                    data={chartData}
                    x={e => {
                        let date = new Date(e.dateTime);
                        return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
                    }}
                    y={e => getPrice(e, chartData)}
                />
                <VictoryScatter data={chartData}
                                size={5}
                                x={e => {
                                    let date = new Date(e.dateTime);
                                    return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
                                }}
                                y={e => getPrice(e, chartData)}
                                style={{ data: { fill: ({ datum }) => datum.promotion ? "lightgreen" : "#c43a31" }, }}
                />
            </VictoryChart>
        );
    }
}

export default PriceHistoryChart;
