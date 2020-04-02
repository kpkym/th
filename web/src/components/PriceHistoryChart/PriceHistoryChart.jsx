import React, {Component} from 'react';
import {VictoryChart, VictoryLine, VictoryScatter} from 'victory';

class PriceHistoryChart extends Component {
    static propTypes = {};

    render() {
        const {chartData} = this.props;

        return (
            <VictoryChart width={800} height={150}>
                <VictoryLine style={{data: {stroke: "#c43a31"}}}
                    labels={({datum}) => datum.price}
                    data={chartData}
                    x={e => {
                        let date = new Date(e.dateTime);
                        return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
                    }}
                    y="price"
                />
                <VictoryScatter data={chartData}
                                size={5}
                                x={e => {
                                    let date = new Date(e.dateTime);
                                    return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
                                }}
                                y="price"
                                style={{ data: { fill: "#c43a31" } }}
                />
            </VictoryChart>
        );
    }
}

export default PriceHistoryChart;
