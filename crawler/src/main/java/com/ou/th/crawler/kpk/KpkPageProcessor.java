package com.ou.th.crawler.kpk;

import com.ou.th.crawler.common.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kpkym
 * Date: 2020-10-31 20:51
 */
@Slf4j
@Component
public class KpkPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(531).setTimeOut(100000);

    @Autowired
    KpkService kpkService;

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        // 当前是搜索页
        if (url.contains("/Tag")) {
            log.info("当前URL为：" + url);
            List<String> allPage = page.getHtml().xpath("//ul[@class='pagination']/li/a/@href").all();
            List<String> alldata = page.getHtml().xpath("//p[@class='dlsiteCode']/text()").all();
            page.putField("arr", alldata.stream().map(e -> KpkModel.builder().code(e).build()).collect(Collectors.toList()));
            page.addTargetRequests(allPage);
        } else if (url.contains(".html")) {
            KpkModel field = CommonUtil.handleAnotation(page.getHtml().get(), new KpkModel(), false);
            String code = url.substring(url.lastIndexOf("/") + 1);
            code = code.substring(0, code.lastIndexOf("."));
            field.setCode(code);
            page.putField("obj", field);
        } else {
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}