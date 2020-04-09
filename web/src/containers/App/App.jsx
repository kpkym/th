import React, {Component} from 'react';
import {Affix, Button, Col, Row} from 'antd';
import "./css/App.css"
import Header from "components/Header/Header"
import ProductItem from "components/ProductItem/ProductItem"
import {connect} from "react-redux";
import {
    delMercariAction,
    delSurugayaAction,
    initMercariAction,
    initSurugayaAction,
    updateMercariAction,
    updateSurugayaAction
} from "redux/actions"
import {chooseItem2UrlAndPic, displaydData, filterData} from "util/utils";

class App extends Component {
    static propTypes = {};

    state = {
        isLike: false,
        initFunc: null,
        updateFunc: null,
        delFunc: null,
        item2UrlAndPic: null
    };

    delAll = (viewData) => {
        this.state.delFunc(viewData.map(e => e.id)).then(() => this.state.initFunc());
    };

    readAll = (viewData) => {
        this.state.updateFunc(viewData, e => {
            e.isChange = false;
            return e;
        }).then(() => this.state.initFunc());
    };

    switch2Surugaya = () => {
        this.setState({
            initFunc: this.props.initSurugayaAction,
            updateFunc: this.props.updateSurugayaAction,
            delFunc: this.props.delSurugayaAction,
            item2UrlAndPic: chooseItem2UrlAndPic('surugaya')
        });
    };

    switch2Mercari = () => {
        this.setState({
            initFunc: this.props.initMercariAction,
            updateFunc: this.props.updateMercariAction,
            delFunc: this.props.delMercariAction,
            item2UrlAndPic: chooseItem2UrlAndPic('mercari')
        });
    };

    triggerWebsite = (website) => {
        if (website === "mercari") {
            this.props.initMercariAction().then(() => this.switch2Mercari());
        } else if (website === "surugaya") {
            this.props.initSurugayaAction().then(() => this.switch2Surugaya());
        }
    };

    componentDidMount() {
        this.props.initMercariAction();
        this.switch2Mercari();
    }

    render() {
        let {items} = this.props;
        let {updateFunc, delFunc, item2UrlAndPic} = this.state;
        let {delAll, readAll, triggerWebsite} = this;
        let viewData = filterData(items, this.state.isLike);
        viewData.sort((a, b) => a.price - b.price);

        let data = displaydData(viewData);
        return (
            <>
                {
                    this.state.isLike ? (
                        <Affix offsetTop={10} style={{position: "absolute", left: "10vw"}}>
                            <Button type="primary" size="large" onClick={() => readAll(viewData)}>已读所有显示的数据</Button>
                        </Affix>
                    ) : null
                }
                <Affix offsetTop={10} style={{position: "absolute", right: "2vw"}}>
                    <Button type="danger" size="large" onClick={() => delAll(viewData)}>删除所有显示的数据</Button>
                </Affix>
                <Header triggerWebsite={triggerWebsite} changeIsLike={() => this.setState({isLike: !this.state.isLike})}
                        items={items} viewCount={viewData.length}/>
                {data.map((line, index) => (
                    <Row gutter={[20, 20]} key={index}>
                        {line.map(e => (
                            <Col span={4} key={e.pid}>
                                <ProductItem delFunc={delFunc} updateFunc={updateFunc} item={e}
                                             item2UrlAndPic={item2UrlAndPic}/>
                            </Col>
                        ))}
                    </Row>
                ))}
            </>
        );
    }
}

export default connect(state => (
    {...state.th}
), {
    initMercariAction,
    updateMercariAction,
    initSurugayaAction,
    updateSurugayaAction,
    delMercariAction,
    delSurugayaAction
})(App);
