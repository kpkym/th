import React, {Component} from 'react';
import {VictoryChart, VictoryLine, VictoryScatter} from 'victory';
import {outOfStockPrice} from 'util/utils'

class PriceHistoryChart extends Component {
    static propTypes = {};

    render() {
        const {chartData} = this.props;
        let maxVal = Math.max(...chartData.map(e => e.price).filter(e => e !== outOfStockPrice));
        return (
            <VictoryChart width={800} height={150}>
                <VictoryLine style={{data: {stroke: "#c43a31"}}}
                    labels={({datum}) => datum.price === outOfStockPrice ? "缺货" : datum.price}
                    data={chartData}
                    x={e => {
                        let date = new Date(e.dateTime);
                        return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
                    }}
                    y={e => e.price === outOfStockPrice ? maxVal : e.price}
                />
                <VictoryScatter data={chartData}
                                size={5}
                                x={e => {
                                    let date = new Date(e.dateTime);
                                    return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
                                }}
                                y={e => e.price === outOfStockPrice ? maxVal : e.price}
                                style={{ data: { fill: "#c43a31" } }}
                />
            </VictoryChart>
        );
    }
}

export default PriceHistoryChart;
