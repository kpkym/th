package com.ou.th.crawler.surugaya;

import com.ou.th.crawler.common.CommonUtil;
import lombok.extern.slf4j.Slf4j;
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

    private Site site = Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(100000);

    @Override
    public void process(Page page) {
        // 下一页
        // String next = page.getHtml().xpath("//li[contains(@class, 'pager-next')]//li[1]/a/@href").get();
        // if (StrUtil.isNotEmpty(next)) {
        //     page.addTargetRequest(next);
        // }

        String url = page.getRequest().getUrl();

        // 当前是搜索页
        if (url.contains("/search")) {
            log.info("当前URL为：" + url);
            page.setSkip(true);
            // 找到所有的链接跟价格
            List<String> items = page.getHtml().xpath("//div[@class='item']").all();

            List<SurugayaModel> surugayaModels = new ArrayList<>();

            for (String item : items) {
                surugayaModels.add(CommonUtil.handleAnotation(item, new SurugayaModel(), true));
            }
            page.putField("data", surugayaModels);
        } else if (url.contains("/jp/items")) {
        } else {
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}