package com.ou.th.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-16 20:51
 */
public class MercariPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        List<String> allTitles = page.getHtml().xpath("//section[@class='items-box']//div[@class='items-box-body']/h3/text()").all();
        page.putField("titles", allTitles);

        List<String> allPrices = page.getHtml().xpath("//div[contains(@class, 'items-box-content')]//div[contains(@class,'items-box-price')]/text()").all();
        page.putField("prices", allPrices);
        // if (page.getResultItems().get("name")==null){
        //     //skip this page
        //     page.setSkip(true);
        // }
        // page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-])").all());
    }

    @Override
    public Site getSite() {
        return site;
    }


}