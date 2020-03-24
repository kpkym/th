import React, {Component} from 'react';
import {Button, Card, Col, Row, Statistic} from 'antd';
import {DeleteOutlined, HeartTwoTone} from '@ant-design/icons';
import "./css/App.css"
import data from 'test/data.json';
import {baseImgUrl} from 'config/config'

class App extends Component {
    static propTypes = {};

    constructor(props) {
        super(props);
        let mercariMatrix = [], lineLen = 6;
        let t = [];
        for (let e of data.data) {
            if (t.length === lineLen) {
                mercariMatrix.push(t);
                t = [];
            } else {
                t.push(e);
            }
        }
        if (t.length !== 0) {
            mercariMatrix.push(t);
        }

        this.state = {mercariMatrix, liked: true};
    }

    componentDidMount() {
        // getAllMercari().then(e => this.setState({mercaris: e.data.data}));
    }

    triggerLiked = (e) => {
        this.setState(({mercariMatrix}) => {
            let clonedMercariMatrix = JSON.parse(JSON.stringify(mercariMatrix));
            loop: for (let i of clonedMercariMatrix) {
                for (let j of i) {
                    if (j.pid === e.pid) {
                        j.liked = !j.liked;
                        break loop;
                    }
                }
            }
            return {mercariMatrix: clonedMercariMatrix};
        });
    };


    render() {
        return (
            <>
                {this.state.mercariMatrix.map((line, index) => (
                    <Row gutter={[20, 20]} key={index}>
                        {line.map(e => (
                            <Col span={4} key={e.pid}>
                                <Card
                                    title={e.title}
                                    hoverable
                                    cover={<a href={e.url} style={{height: "100%", width: "100%", textAlign: "center"}}
                                              target="_blank">
                                        <img style={{height: "100px", objectFit: 'scale-down'}}
                                             src={baseImgUrl + "/" + e.pictures[0]}/></a>}
                                    actions={[
                                        <Button type="link" onClick={() => this.triggerLiked(e)}>{e.liked ?
                                            <HeartTwoTone twoToneColor="#eb2f96"/>
                                            : <HeartTwoTone twoToneColor="#ccc"/>}</Button>,
                                        <Button type="link"><DeleteOutlined/></Button>,
                                    ]}
                                >
                                    <Card.Meta title={e.title}
                                               description={<Statistic title="价格" value={e.currentPrice}/>}/>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                ))}
            </>);
    }
}

export default App;
