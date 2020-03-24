import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {baseImgUrl} from "../../config/config";
import {Button, Card, Statistic} from "antd";
import {DeleteOutlined, HeartTwoTone} from "@ant-design/icons";

class ProductItem extends Component {
    static propTypes = {
        item: PropTypes.object.isRequired
    };

    triggerLiked = (e) => {
        this.setState(state => {
            let mercaris = JSON.parse(JSON.stringify(state.mercaris));
            for (let i of mercaris) {
                if (i.pid === e.pid) {
                    i.liked = !i.liked;
                    break;
                }
            }
            return {mercaris};
        });
    };

    render() {
        let {item} = this.props;
        return (
            <Card
                title={item.title}
                hoverable
                cover={<a href={item.url} style={{height: "100%", width: "100%", textAlign: "center"}}
                          target="_blank">
                    <img style={{height: "100px", objectFit: 'scale-down'}}
                         src={baseImgUrl + "/" + item.pictures[0]}/></a>}
                actions={[
                    <Button type="link" onClick={() => this.triggerLiked(item)}>{item.liked ?
                        <HeartTwoTone twoToneColor="#eb2f96"/>
                        : <HeartTwoTone twoToneColor="#ccc"/>}</Button>,
                    <Button type="link"><DeleteOutlined/></Button>,
                ]}
            >
                <Card.Meta title={item.title}
                           description={<Statistic title="价格" value={item.currentPrice}/>}/>
            </Card>
        );
    }

}

export default ProductItem;
