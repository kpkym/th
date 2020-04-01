package com.ou.th.crawler.mercari;

import com.ou.th.crawler.common.CommonUtil;
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
 * Date: 2020-03-16 20:51
 */
@Slf4j
@Component
public class MercariPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(100000);

    @Autowired
    MercariService mercariService;

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        // 当前是搜索页
        if (url.contains("/jp/search")) {
            log.info("当前URL为：" + url);
            List<String> items = page.getHtml().xpath("//section[@class='items-box']").all();
            List<MercariModel> mercariModels = new ArrayList<>();

            for (String item : items) {
                mercariModels.add(CommonUtil.handleAnotation(item, new MercariModel(), true));
            }
            page.putField("arr", mercariModels);
            List<String> all = page.getHtml().xpath("//li[@class='pager-cell']/a/@href").all();
            page.addTargetRequests(all);
        } else if (url.contains("/jp/items")) {
            page.putField("obj", CommonUtil.handleAnotation(page.getHtml().get(), new MercariModel(), false));
        } else {
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}