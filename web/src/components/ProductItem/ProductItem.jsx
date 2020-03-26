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
        mercari.liked = !mercari.liked;
        this.props.update(mercari);
    };

    del = (mercari) => {
        mercari.disliked = true;
        this.props.update(mercari);
    };


    render() {
        let {item} = this.props;
        let src = baseImgUrl + "/" + item.pictures[0];
        src = src.includes("static.mercdn.net") ?  src.replace(baseImgUrl+"/", "") : src;
        return (
            <Card
                title={item.title}
                hoverable
                cover={<a href={item.url} style={{height: "100%", width: "100%", textAlign: "center"}}
                          target="_blank">
                    <img style={{height: "100px", objectFit: 'scale-down'}}
                         src={src}/></a>}
                actions={[
                    <Button type="link" onClick={() => this.triggerLiked(item)}>{item.liked ?
                        <HeartTwoTone twoToneColor="#eb2f96"/>
                        : <HeartTwoTone twoToneColor="#ccc"/>}</Button>,
                    <Button type="link" onClick={() => this.del(item)}><DeleteOutlined/></Button>,
                    <a href={"https://www.suruga-ya.jp/search?category=&search_word="+item.title} target="_blank">駿河屋</a>
                ]}
            >
                <Card.Meta title={item.title} description={<Statistic title="价格" value={item.currentPrice}/>}/>
            </Card>
        );
    }

}

export default ProductItem;
