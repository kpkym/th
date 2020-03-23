import React, {Component} from 'react';

import './css/App.css';
import ProductItem from 'components/ProductItem/ProductItem';
import {getAllMercari} from 'api/index'

class App extends Component {
    static propTypes = {};

    state = {
        mercaris: []

    };

    componentDidMount() {
        getAllMercari().then(e => this.setState({mercaris: e.data.data}));
    }

    render() {
        return (
            <table>
                <tbody>
                    {this.state.mercaris.map(e => <ProductItem key={e.url} mercari={e}/>)}
                </tbody>
            </table>
        );
    }

}

export default App;
