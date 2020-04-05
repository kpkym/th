import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Button, Card, Drawer, Statistic} from "antd";
import {DeleteOutlined, HeartTwoTone, HistoryOutlined} from "@ant-design/icons";
import PriceHistoryChart from "components/PriceHistoryChart/PriceHistoryChart"
import {outOfStockPrice} from 'util/utils'

function outOfStockPrice2Text(price) {
    return price === outOfStockPrice ? "缺货" : price;
}

class ProductItem extends Component {
    static propTypes = {
        item: PropTypes.object.isRequired,
        updateFunc: PropTypes.func.isRequired,
        delFunc: PropTypes.func.isRequired,
        item2UrlAndPic: PropTypes.func.isRequired,
    };

    state = {
        visible: false
    };

    triggerLiked = (item) => {
        let {updateFunc} = this.props;
        updateFunc(item, e => {
            e.isLike = !e.isLike;
            return e});
    };

    del = (item) => {
        this.props.delFunc(item.id);
    };

    showDrawer = () => {
        this.setState({
            visible: true,
        });
    };

    render() {
        let {item, item2UrlAndPic} = this.props;
        let {triggerLiked, del, showDrawer} = this;
        let {priceTimes: chartData} = item;

        let ptLength = item.priceTimes.length;
        let headStyle = {backgroundColor: item.isChange ? "lightgreen" : ""};
        let price = (
            <Statistic title="价格" value={outOfStockPrice2Text(item.priceTimes[ptLength - 1].price)}
                       suffix={ptLength > 1 ?
                           <span
                               style={{textDecorationLine: item.isChange ? "line-through" : ""}}>{outOfStockPrice2Text(item.priceTimes[ptLength - 2].price)}</span>
                           : ""}
            />
        );
        return (
            <Card
                title={item.title}
                headStyle={headStyle}
                hoverable
                cover={<a href={item2UrlAndPic(item).url}
                          style={{height: "100%", width: "100%", textAlign: "center"}}
                          target="_blank">
                    <img style={{height: "100px", objectFit: 'scale-down'}}
                         src={item2UrlAndPic(item).picture}/></a>}
                actions={[
                    <Button type="link" onClick={() => triggerLiked(item)}>{item.isLike ?
                        <HeartTwoTone twoToneColor="#eb2f96"/>
                        : <HeartTwoTone twoToneColor="#ccc"/>}</Button>,
                    <Button type="link" onClick={() => del(item)}><DeleteOutlined/></Button>,
                    <Button type="link" onClick={showDrawer}>
                        <HistoryOutlined/>
                    </Button>,
                    <a href={"https://www.suruga-ya.jp/search?category=&search_word=" + item.title}
                       target="_blank">駿河屋</a>
                ]}
            >
                <Card.Meta title={item.title} description={price}/>

                <Drawer
                    placement="bottom"
                    closable={false}
                    onClose={() => this.setState({visible: false})}
                    visible={this.state.visible}
                >
                    <PriceHistoryChart chartData={chartData}/>
                </Drawer>
            </Card>
        );
    }

}

export default ProductItem;
