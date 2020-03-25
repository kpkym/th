import React, {Component} from 'react';
import {Col, Row} from 'antd';
import "./css/App.css"
import Header from "components/Header/Header"
import ProductItem from "components/ProductItem/ProductItem"
import {connect} from "react-redux";
import {initMercariAction, triggerIsSellingAction, updateMerciAction} from "redux/actions"

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

let filterdData = (mercaris, isSelling = true) => {
    return mercaris.filter(e => !e.sold || !isSelling).filter(e => !e.disliked);
};

let displaydData = (mercaris) => {
    return array2Matrix(mercaris);
};

class App extends Component {
    static propTypes = {};


    componentDidMount() {
        this.props.initMercariAction();
    }


    render() {
        let {mercaris, isSelling, triggerIsSellingAction} = this.props;
        let viewData = filterdData(mercaris, isSelling);
        viewData.sort((a, b) => a.currentPrice - b.currentPrice);
        let data = displaydData(viewData);
        return (
            <>
                <Row><Col><button onClick={() => viewData.forEach(e => {
                    e.disliked = true;
                    this.props.updateMerciAction(e);
                })}>del all</button></Col></Row>

                <Header isSelling={isSelling} triggerIsSellingAction={triggerIsSellingAction} mercaris={mercaris} viewCount={viewData.length}/>
                {data.map((line, index) => (
                    <Row gutter={[20, 20]} key={index}>
                        {line.map(e => (
                            <Col span={4} key={e.pid}>
                                <ProductItem update={this.props.updateMerciAction} item={e}/>
                            </Col>
                        ))}
                    </Row>
                ))}
            </>);
    }
}

export default connect(state => (
    {...state.mercaris, mercaris: state.mercaris.items}
), {triggerIsSellingAction, initMercariAction, updateMerciAction})(App);
