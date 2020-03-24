import React, {Component} from 'react';
import {Col, Row} from 'antd';
import "./css/App.css"
import Header from "components/Header/Header"
import ProductItem from "components/ProductItem/ProductItem"
import {connect} from "react-redux";
import {triggerIsSelling} from "redux/actions"

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
    return mercaris.filter(e => !e.sold || !isSelling);
};

let displaydData = (mercaris) => {
    return array2Matrix(mercaris);
};

class App extends Component {
    static propTypes = {};

    render() {
        let {mercaris, isSelling, triggerIsSelling} = this.props;
        let viewData = filterdData(mercaris, isSelling);
        let data = displaydData(viewData);
        return (
            <>
                <Header isSelling={isSelling} triggerIsSelling={triggerIsSelling} mercaris={mercaris} viewCount={viewData.length}/>
                {data.map((line, index) => (
                    <Row gutter={[20, 20]} key={index}>
                        {line.map(e => (
                            <Col span={4} key={e.pid}>
                                <ProductItem item={e}/>
                            </Col>
                        ))}
                    </Row>
                ))}
            </>);
    }
}

export default connect(state => (
    {...state.mercaris, mercaris: state.mercaris.items}
), {triggerIsSelling})(App);
