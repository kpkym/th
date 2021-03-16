import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Button, Card, Drawer, Statistic, Tag} from "antd";
import {CloseOutlined, DeleteOutlined, HeartTwoTone, HistoryOutlined, SyncOutlined} from "@ant-design/icons";
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
        website: PropTypes.string,
    };

    state = {
        visible: false
    };

    triggerLiked = () => {
        let {updateFunc, item} = this.props;
        updateFunc(item, e => {
            e.isLike = !e.isLike;
            e.isDel = false;
            e.isDontCrawler = false;
            return e
        });
    };

    del = () => {
        this.props.delFunc(this.props.item.id);
    };

    dontCrawler = () => {
        let {updateFunc, item} = this.props;
        updateFunc(item, e => {
            e.isDontCrawler = true;
            return e
        }).then(() => this.del());
    };

    flushImg = () => {
        let {item, flushImgSurugayaAction} = this.props;
        flushImgSurugayaAction(item.id);
    };
    
    showDrawer = () => {
        this.setState({
            visible: true,
        });
    };

    render() {
        let {item, item2UrlAndPic, website} = this.props;
        let {triggerLiked, del, showDrawer, dontCrawler, flushImg} = this;
        let {priceTimes: chartData} = item;

        let ptLength = item.priceTimes.length;
        let headStyle = {backgroundColor: item.isChange && ptLength > 1 ? (item.priceTimes[ptLength - 1].price < item.priceTimes[ptLength - 2].price ? "lightgreen" : "lightpink") : ""};
        let price = (
            <Statistic title="价格" value={outOfStockPrice2Text(item.priceTimes[ptLength - 1].price)}
                       suffix={ptLength > 1 ?
                           <span
                               style={{textDecorationLine: "line-through"}}>{outOfStockPrice2Text(item.priceTimes[ptLength - 2].price)}</span>
                           : ""}
            />
        );
        return (
            <Card
                title={item.title}
                headStyle={headStyle}
                hoverable
                extra={item.isSold === 1 ? <Tag color="orange">S</Tag> : null}
                cover={<a href={item2UrlAndPic(item).url}
                          style={{height: "100%", width: "100%", textAlign: "center"}}
                          target="_blank">
                    <img style={{height: "100px", objectFit: 'scale-down'}}
                         src={item.picturesOriginal}/></a>}
                actions={[
                    <Button type="link" onClick={triggerLiked}>{item.isLike ?
                        <HeartTwoTone twoToneColor="#eb2f96"/>
                        : <HeartTwoTone twoToneColor="#ccc"/>}</Button>,
                    <Button type="link" onClick={del}><DeleteOutlined/></Button>,
                    <Button type="link" onClick={dontCrawler}><CloseOutlined /></Button>,
                    <Button type="link" onClick={showDrawer}>
                        <HistoryOutlined/>
                    </Button>,
                    website === 'mercari' ? 
                    <a href={"https://www.suruga-ya.jp/search?category=&search_word=" + item.title}
                       target="_blank">駿河屋</a>
                       : <Button type="link" onClick={flushImg}><SyncOutlined /></Button>
                ]}
            >
                <Card.Meta description={price}/>
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
