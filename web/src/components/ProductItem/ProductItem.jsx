import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {baseImgUrl} from "../../config/config";
import {Button, Card, Statistic} from "antd";
import {DeleteOutlined, HeartTwoTone} from "@ant-design/icons";

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
        let {item} = this.props;
        let src = baseImgUrl + item.picture;

        src = src.includes("static.mercdn.net") ?  item.picturesOriginal : src;
        let ptLength = item.priceTimes.length;
        let headStyle = {backgroundColor: item.isChange ? "lightgreen" : ""};
        let price = (
            <Statistic title="价格" value={item.priceTimes[ptLength - 1].price}
                       suffix={ptLength > 1 ?
                           <span style={{textDecorationLine: item.isChange ? "line-through" : ""}}>{item.priceTimes[ptLength - 2].price}</span>
                           : ""}
            />
        );
        return (
            <Card
                title={item.title}
                headStyle={headStyle}
                hoverable
                cover={<a href={item.url} style={{height: "100%", width: "100%", textAlign: "center"}}
                          target="_blank">
                    <img style={{height: "100px", objectFit: 'scale-down'}}
                         src={src}/></a>}
                actions={[
                    <Button type="link" onClick={() => this.triggerLiked(item)}>{item.isLike ?
                        <HeartTwoTone twoToneColor="#eb2f96"/>
                        : <HeartTwoTone twoToneColor="#ccc"/>}</Button>,
                    <Button type="link" onClick={() => this.del(item)}><DeleteOutlined/></Button>,
                    <a href={"https://www.suruga-ya.jp/search?category=&search_word="+item.title} target="_blank">駿河屋</a>
                ]}
            >
                <Card.Meta title={item.title} description={price}/>
            </Card>
        );
    }

}

export default ProductItem;
