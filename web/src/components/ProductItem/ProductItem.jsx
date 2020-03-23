import React, {Component} from 'react';
import PropTypes from 'prop-types';

class ProductItem extends Component {
    static propTypes = {
        mercari: PropTypes.object.isRequired
    };

    state = {};

    render() {
        let {url, title, currentPrice, pictures} = this.props.mercari;
        return (
            <tr style={{border:1}}>
                <td><a href={url}>{title}</a></td>
                <td>{currentPrice}</td>
                {pictures.map(e => (<td key={e}><img style={{maxHeight: '100px'}} src={"http://localhost:57596/"+e}/></td>))}
            </tr>
        );
    }

}

export default ProductItem;
