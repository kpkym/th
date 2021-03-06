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
    updateSurugayaAction,
    searchSurugayaAction,
    searchMercariAction,
    flushImgSurugayaAction
} from "redux/actions"
import {chooseItem2UrlAndPic, displaydData, getViewData} from "util/utils";

class App extends Component {
    static propTypes = {};

    constructor(props){
        super(props);
        this.state = {
            isLike: false,
            website: "",
            initFunc: null,
            updateFunc: null,
            delFunc: null,
            item2UrlAndPic: null,
            searchFunc: null,
        };

    }

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
        });
    };

    switch2Mercari = () => {
        this.setState({
            website: "mercari",
            initFunc: this.props.initMercariAction,
            updateFunc: this.props.updateMercariAction,
            delFunc: this.props.delMercariAction,
            item2UrlAndPic: chooseItem2UrlAndPic('mercari'),
            searchFunc: this.props.searchMercariAction,
        });
    };

    switch2Surugaya = () => {
        this.setState({
            website: "surugaya",
            initFunc: this.props.initSurugayaAction,
            updateFunc: this.props.updateSurugayaAction,
            delFunc: this.props.delSurugayaAction,
            item2UrlAndPic: chooseItem2UrlAndPic('surugaya'),
            searchFunc: this.props.searchSurugayaAction,
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
        this.switch2Surugaya();
        this.props.initSurugayaAction().then(() => {
            window.addEventListener("keyup", e => {
                if (e.target.localName === "input"){
                    return;
                }
                switch (e.key) {
                    case 'a':
                    case 'ArrowLeft':
                        this.triggerWebsite("surugaya");
                        break;
                    case 'd':
                    case 'ArrowRight':
                        this.triggerWebsite("mercari");
                        break;
                    case 'w':
                    case 'ArrowUp':
                        this.changeIsLike();
                        break;
                    case 's':
                    case 'ArrowDown':
                        this.readAll();
                        break;
                    case 'z':
                        if (window.confirm("是否确认不再抓取所有？")) {
                            this.state.updateFunc(getViewData(this.props.items, this.state.isLike), e => {
                                e.isDontCrawler = true;
                                return e;
                            }).then(() => this.delAll());
                            window.localStorage.removeItem("xx");
                        }
                        break;
                    case 'x':
                        if (window.localStorage.getItem("read2DelAll")) {
                            this.delAll();
                            window.localStorage.removeItem("read2DelAll");
                        } else {
                            window.localStorage.setItem("read2DelAll", "true");
                            message.warning("是否确认删除所有？", 1);
                            window.setTimeout(() => window.localStorage.removeItem("read2DelAll"), 1000);
                        }
                        break;
                    default:
                        console.log(e)
                }
            });
        });
    }

    render() {
        let {items, flushImgSurugayaAction} = this.props;
        let {website, isLike, updateFunc, delFunc, item2UrlAndPic, searchFunc} = this.state;
        let {delAll, readAll, triggerWebsite, changeIsLike} = this;

        let viewData = getViewData(items, isLike);
        let data = displaydData(viewData);
        return (
            <>
                <Affix offsetTop={10} style={{position: "absolute", right: "2vw"}}>
                    <Button type="danger" size="large" onClick={delAll}>删除所有显示的数据</Button>
                </Affix>
                <Header searchFunc={searchFunc} isLike={isLike} website={website} triggerWebsite={triggerWebsite} changeIsLike={changeIsLike}
                        items={items} viewCount={viewData.length}/>
                {data.map((line, index) => (
                    <Row gutter={[20, 20]} key={index}>
                        {line.map(e => (
                            <Col span={4} key={e.pid}>
                                <ProductItem delFunc={delFunc} updateFunc={updateFunc} item={e}
                                             website={website}
                                             item2UrlAndPic={item2UrlAndPic}
                                             flushImgSurugayaAction={flushImgSurugayaAction}/>
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
    delSurugayaAction,
    searchSurugayaAction,
    searchMercariAction,
    flushImgSurugayaAction
})(App);
