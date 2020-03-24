import React, {Component} from 'react';
import PropTypes from 'prop-types';

import {Checkbox, Descriptions, PageHeader, Statistic} from 'antd';


class Header extends Component {
    static propTypes = {
        isSelling: PropTypes.bool.isRequired,
        triggerIsSelling: PropTypes.func.isRequired,
        viewCount: PropTypes.number.isRequired,
        mercaris: PropTypes.array.isRequired
    };

    render() {
        let {isSelling, triggerIsSelling, mercaris, viewCount} = this.props;
        return (
                    <PageHeader
                        // title="Mercari"
                    >
                        <Descriptions size="small" column={6}>
                            <Descriptions.Item>
                                <div>
                                    <Checkbox checked={isSelling} onChange={triggerIsSelling}>贩卖中</Checkbox><br/>
                                    <Checkbox>我感兴趣的</Checkbox><br/>
                                </div>
                            </Descriptions.Item>
                            <Descriptions.Item><Statistic title="显示数量" value={viewCount} /></Descriptions.Item>
                            <Descriptions.Item><Statistic title="感兴趣" value={112893} /></Descriptions.Item>
                            <Descriptions.Item><Statistic title="标记删除" value={112893} /></Descriptions.Item>
                            <Descriptions.Item><Statistic title="总数量" value={mercaris.length} /></Descriptions.Item>
                        </Descriptions>
                    </PageHeader>
        );
    }

}

export default Header;
