import React, {Component} from 'react';
import PropTypes from 'prop-types';

import {Button, Descriptions, message, PageHeader, Radio, Statistic, Switch, Input} from 'antd';
import {crawlerMercari, crawlerSurugaya} from 'api/index'

class Header extends Component {
    static propTypes = {
        viewCount: PropTypes.number.isRequired,
        items: PropTypes.array.isRequired,
        changeIsLike: PropTypes.func.isRequired,
        triggerWebsite: PropTypes.func.isRequired,
        website: PropTypes.string,
        isLike: PropTypes.bool,
        searchFunc: PropTypes.func
    };

    crawler = (func) => func().then(() => {
        message.info('开始爬取');
    });

    render() {
        let {items, viewCount, changeIsLike, triggerWebsite, website, isLike, searchFunc} = this.props;
        let likedCount = items.reduce((a, b) => a + b.isLike, 0);
        return (
            <PageHeader title={<>
                "吃土中……"  
                {
                    <Input.Search style={{ width: 400 }} size="middle" onSearch={value => searchFunc(value)} enterButton />
                }
            </>}>
                <Descriptions size="small" column={7}>
                    <Descriptions.Item>
                        <Switch checked={isLike} onChange={changeIsLike}/>我感兴趣的<br/>
                    </Descriptions.Item>
                    <Descriptions.Item>
                        <Radio.Group value={website ? website : "mercari"} buttonStyle="solid"
                                     onChange={e => triggerWebsite(e.target.value)}>
                            <Radio.Button value="surugaya">Surugaya</Radio.Button>
                            <Radio.Button value="mercari">Mercari</Radio.Button>
                        </Radio.Group>
                    </Descriptions.Item>
                    <Descriptions.Item><Statistic title="显示数量" value={viewCount}/></Descriptions.Item>
                    <Descriptions.Item><Statistic title="感兴趣" value={likedCount}/></Descriptions.Item>
                    <Descriptions.Item><Statistic title="总数量" value={items.length}/></Descriptions.Item>
                    <Descriptions.Item><Button type="dashed"
                                               onClick={() => this.crawler(crawlerMercari)}>Mercari</Button></Descriptions.Item>
                    <Descriptions.Item><Button type="dashed"
                                               onClick={() => this.crawler(crawlerSurugaya)}>Surugaya</Button></Descriptions.Item>
                </Descriptions>
            </PageHeader>
        );
    }

}

export default Header;
