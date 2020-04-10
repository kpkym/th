import React, {Component} from 'react';
import {Affix, Button, Col, message, Row} from 'antd';
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
import {chooseItem2UrlAndPic, displaydData, getViewData} from "util/utils";

class App extends Component {
    static propTypes = {};

    state = {
        isLike: false,
        website: "",
        initFunc: null,
        updateFunc: null,
        delFunc: null,
        item2UrlAndPic: null,
    };

    changeIsLike = () => this.setState({isLike: !this.state.isLike});

    delAll = () => {
        let viewData = getViewData(this.props.items, this.state.isLike);
        this.state.delFunc(viewData.map(e => e.id)).then(() => this.state.initFunc());
    };

    readAll = () => {
        let viewData = getViewData(this.props.items, this.state.isLike);
        this.state.updateFunc(viewData, e => {
            e.isChange = false;
            return e;
        }).then(() => this.state.initFunc());
    };

    switch2Mercari = () => {
        this.setState({
            website: "mercari",
            initFunc: this.props.initMercariAction,
            updateFunc: this.props.updateMercariAction,
            delFunc: this.props.delMercariAction,
            item2UrlAndPic: chooseItem2UrlAndPic('mercari')
        });
    };

    switch2Surugaya = () => {
        this.setState({
            website: "surugaya",
            initFunc: this.props.initSurugayaAction,
            updateFunc: this.props.updateSurugayaAction,
            delFunc: this.props.delSurugayaAction,
            item2UrlAndPic: chooseItem2UrlAndPic('surugaya')
        });
    };

    triggerWebsite = (website = "mercari") => {
        if (website === "mercari") {
            this.props.initMercariAction().then(() => this.switch2Mercari());
        } else if (website === "surugaya") {
            this.props.initSurugayaAction().then(() => this.switch2Surugaya());
        }
    };


    componentDidMount() {
        this.switch2Mercari();
        this.props.initMercariAction().then(() => {
            window.addEventListener("keyup", e => {
                switch (e.key) {
                    case 'q':
                        this.triggerWebsite("mercari");
                        break;
                    case 'e':
                        this.triggerWebsite("surugaya");
                        break;
                    case 'w':
                        this.changeIsLike();
                        break;
                    case 'd':
                        let read2DelAll = window.localStorage.getItem("read2DelAll");
                        if (read2DelAll) {
                            this.delAll();
                        } else {
                            window.localStorage.setItem("read2DelAll", "true");
                            message.warning("是否确认删除所有？", 0.3);
                            window.setTimeout(() => window.localStorage.removeItem("read2DelAll"), 300);
                        }
                        break;
                    case 'a':
                        if (this.state.isLike) {
                            this.readAll();
                        }
                        break;
                    default:
                        console.log(e)
                }
            });
        });
    }

    render() {
        let {items} = this.props;
        let {website, isLike, updateFunc, delFunc, item2UrlAndPic} = this.state;
        let {delAll, readAll, triggerWebsite, changeIsLike} = this;

        let viewData = getViewData(items, isLike);
        let data = displaydData(viewData);
        return (
            <>
                {
                    this.state.isLike ? (
                        <Affix offsetTop={10} style={{position: "absolute", left: "10vw"}}>
                            <Button type="primary" size="large" onClick={readAll}>已读所有显示的数据</Button>
                        </Affix>
                    ) : null
                }
                <Affix offsetTop={10} style={{position: "absolute", right: "2vw"}}>
                    <Button type="danger" size="large" onClick={delAll}>删除所有显示的数据</Button>
                </Affix>
                <Header isLike={isLike} website={website} triggerWebsite={triggerWebsite} changeIsLike={changeIsLike}
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
