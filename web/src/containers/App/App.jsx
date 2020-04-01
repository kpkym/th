import React, {Component} from 'react';
import {Affix, Button, Col, Row} from 'antd';
import "./css/App.css"
import Header from "components/Header/Header"
import ProductItem from "components/ProductItem/ProductItem"
import {connect} from "react-redux";
import {initMercariAction, initSurugayaAction, updateMercariAction, updateSurugayaAction} from "redux/actions"

function array2Matrix(arr, lineLen = 6) {
    let matrix = [];
    let t = [];
    for (let e of arr) {
        if (t.length === lineLen) {
            matrix.push(t);
            t = [];
        } else {
            t.push(e);
        }
    }
    if (t.length !== 0) {
        matrix.push(t);
    }
    return matrix;
}

let filterdData = (items, isLike = false) => {
    return items.filter(e => e.isDel === false && e.isLike === isLike);
};

let displaydData = (items) => {
    return array2Matrix(items);
};

class App extends Component {
    static propTypes = {};

    state = {
        isLike: false,
        website: ""
    };

    componentDidMount() {
        // this.props.initMercariAction().then(()=> this.setState({"website": "mercari"}));
        this.props.initSurugayaAction().then(() => this.setState({"website": "surugaya"}));
    }


    render() {
        let {items} = this.props;
        let viewData = filterdData(items, this.state.isLike);
        viewData.sort((a, b) => a.price - b.price);

        let data = displaydData(viewData);
        return (
            <>
                <Row><Col offset={20} span={3}>
                    <Affix offsetTop={10} style={{position: "absolute"}}>
                        <Button type="danger" size="large" block onClick={() => {
                            viewData.forEach(e => {
                                e.isDel = true;
                                this.props.updateMercariAction(e);
                            });
                            this.props.initMercariAction();
                        }}>删除所有显示的数据
                        </Button>
                    </Affix>
                </Col></Row>
                <Header changeIsLike={() => this.setState({isLike: !this.state.isLike})}
                        items={items} viewCount={viewData.length}/>
                {data.map((line, index) => (
                    <Row gutter={[20, 20]} key={index}>
                        {line.map(e => (
                            <Col span={4} key={e.pid}>
                                <ProductItem update={this.props.updateMercariAction} item={e}/>
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
    updateSurugayaAction
})(App);
