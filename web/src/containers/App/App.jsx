import React, {Component} from 'react';
import {Affix, Button, Col, Row} from 'antd';
import "./css/App.css"
import Header from "components/Header/Header"
import ProductItem from "components/ProductItem/ProductItem"
import {connect} from "react-redux";
import {initMercariAction, initSurugayaAction, updateMercariAction, updateSurugayaAction,delMercariAction,delSurugayaAction} from "redux/actions"
import {filterData,displaydData, chooseItem2UrlAndPic} from "util/utils";

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
        let {delAll, triggerWebsite} = this;
        let viewData = filterData(items, this.state.isLike);
        viewData.sort((a, b) => a.price - b.price);

        let data = displaydData(viewData);
        return (
            <>
                <Row><Col offset={20} span={3}>
                    <Affix offsetTop={10} style={{position: "absolute"}}>
                        <Button type="danger" size="large" block onClick={() => delAll(viewData)}>删除所有显示的数据
                        </Button>
                    </Affix>
                </Col></Row>
                <Header triggerWebsite={triggerWebsite} changeIsLike={() => this.setState({isLike: !this.state.isLike})}
                        items={items} viewCount={viewData.length}/>
                {data.map((line, index) => (
                    <Row gutter={[20, 20]} key={index}>
                        {line.map(e => (
                            <Col span={4} key={e.pid}>
                                    <ProductItem delFunc={delFunc} updateFunc={updateFunc} item={e} item2UrlAndPic={item2UrlAndPic}/>
                            </Col>
                        ))}
                    </Row>
                ))}
            </>);
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
