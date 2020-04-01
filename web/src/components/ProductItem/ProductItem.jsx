import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Button, Card, Statistic} from "antd";
import {DeleteOutlined, HeartTwoTone} from "@ant-design/icons";
import {baseImgUrl} from "config/config";

function itemUrlAndPic(item, website = "mercari") {
    let url, picture;
    if (website === "mercari") {
        url = item.url.includes("mercari.com") ? item.url : "https://www.mercari.com" + item.url;
        picture = item.picture.includes("static.mercdn.net") ? item.picture : (baseImgUrl + item.picture);
    } else if (website === "surugaya") {
        url = item.url;
        picture = item.picture.includes("/database/pics") ? item.picture : baseImgUrl + item.picture;
    }
    return ({url, picture});
}

class ProductItem extends Component {
    static propTypes = {
        item: PropTypes.object.isRequired,
        update: PropTypes.func.isRequired,
    };

    triggerLiked = (mercari) => {
        mercari.isLike = !mercari.isLike;
        this.props.update(mercari);
    };

    del = (mercari) => {
        mercari.isDel = true;
        this.props.update(mercari);
    };


    render() {
        let {item, website} = this.props;

        let ptLength = item.priceTimes.length;
        let headStyle = {backgroundColor: item.isChange ? "lightgreen" : ""};
        let price = (
            <Statistic title="价格" value={item.priceTimes[ptLength - 1].price}
                       suffix={ptLength > 1 ?
                           <span
                               style={{textDecorationLine: item.isChange ? "line-through" : ""}}>{item.priceTimes[ptLength - 2].price}</span>
                           : ""}
            />
        );
        return (
            <Card
                title={item.title}
                headStyle={headStyle}
                hoverable
                cover={<a href={itemUrlAndPic(item, website).url}
                          style={{height: "100%", width: "100%", textAlign: "center"}}
                          target="_blank">
                    <img style={{height: "100px", objectFit: 'scale-down'}}
                         src={itemUrlAndPic(item, website).picture}/></a>}
                actions={[
                    <Button type="link" onClick={() => this.triggerLiked(item)}>{item.isLike ?
                        <HeartTwoTone twoToneColor="#eb2f96"/>
                        : <HeartTwoTone twoToneColor="#ccc"/>}</Button>,
                    <Button type="link" onClick={() => this.del(item)}><DeleteOutlined/></Button>,
                    <a href={"https://www.suruga-ya.jp/search?category=&search_word=" + item.title}
                       target="_blank">駿河屋</a>
                ]}
            >
                <Card.Meta title={item.title} description={price}/>
            </Card>
        );
    }

}

export default ProductItem;
