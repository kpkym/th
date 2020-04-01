import React, {Component} from 'react';
import PropTypes from 'prop-types';

import {Checkbox, Descriptions, PageHeader, Statistic} from 'antd';


class Header extends Component {
    static propTypes = {
        viewCount: PropTypes.number.isRequired,
        items: PropTypes.array.isRequired,
        changeIsLiked: PropTypes.func.isRequired,
        triggerWebsite: PropTypes.func.isRequired,
    };

    render() {
        let {items, viewCount, changeIsLike, triggerWebsite} = this.props;
        let likedCount = items.reduce((a, b) => a + b.isLike, 0);
        return (
            <PageHeader title="吃土中……">
                <Descriptions size="small" column={6}>
                    <Descriptions.Item>
                        <div>
                            <Checkbox onChange={changeIsLike}>我感兴趣的</Checkbox><br/>
                            <Checkbox onChange={triggerWebsite}>骏河屋</Checkbox><br/>
                        </div>
                    </Descriptions.Item>
                    <Descriptions.Item><Statistic title="显示数量" value={viewCount}/></Descriptions.Item>
                    <Descriptions.Item><Statistic title="感兴趣" value={likedCount}/></Descriptions.Item>
                    <Descriptions.Item><Statistic title="总数量" value={items.length}/></Descriptions.Item>
                </Descriptions>
            </PageHeader>
        );
    }

}

export default Header;
