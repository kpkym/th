package com.ou.th.crawler.surugaya;

import com.ou.th.crawler.common.CommonUtil;
import com.ou.th.crawler.surugaya.SurugayaNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-31 16:53
 */
@Slf4j
@Component
public class SurugayaPageProcessor implements PageProcessor {
    private String surugayaImgPrefix = "https://www.suruga-ya.jp/database/pics/game/";
    private String surugayaImgSuffix = ".jpg";
    private Site site = Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(100000);

    @Autowired
    SurugayaNotification surugayaNotification;

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        // 当前是搜索页
        if (url.contains("/search")) {
            log.info("当前URL为：" + url);

            if (surugayaNotification.neetDetail(url)) {
                page.addTargetRequests(page.getHtml().xpath("//div[@class='item']//p[@class='title']//a/@href").all());
                page.setSkip(true);
            } else {
                List<String> items = page.getHtml().xpath("//div[@class='item']").all();
                List<SurugayaModel> surugayaModels = new ArrayList<>();

                for (String item : items) {
                    SurugayaModel surugayaModel = CommonUtil.handleAnotation(item, new SurugayaModel(), true);
                    surugayaModel.setPicturesOriginal(
                            surugayaImgPrefix
                                    + SurugayaUtil.getIdFrom(surugayaModel.getUrl()).toLowerCase()
                                    + surugayaImgSuffix
                    );
                    surugayaModels.add(surugayaModel);
                }
                page.putField("arr", surugayaModels);
            }
            List<String> all = page.getHtml().xpath("//div[@id='pager']//a/@href").all();
            page.addTargetRequests(all);
        } else if (url.contains("/product/detail")) {
            SurugayaModel surugayaModel = CommonUtil.handleAnotation(page.getHtml().get(), new SurugayaModel(), false);
            surugayaModel.setPicturesOriginal(
                    surugayaImgPrefix
                            + SurugayaUtil.getIdFrom(surugayaModel.getUrl()).toLowerCase()
                            + surugayaImgSuffix
            );
            page.putField("obj", surugayaModel);
        } else {
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}