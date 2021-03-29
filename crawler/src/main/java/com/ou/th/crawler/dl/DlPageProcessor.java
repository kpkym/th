package com.ou.th.crawler.dl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
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
 * Date: 2020-03-16 20:51
 */
@Slf4j
@Component
public class DlPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(5331).setTimeOut(100000);
    @Autowired
    DlService dlService;

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();

        try {
            int i = NumberUtil.parseInt(StrUtil.subAfter(url, "page/", true));
            if (i > 20) {
                page.setSkip(true);
                return;
            }
        } catch (Exception e) {
            page.setSkip(true);
            return;
        }

        // 当前是搜索页
        if (url.contains("/maniax/fsr")) {
            log.info("当前URL为：" + url);
            List<String> alldata = page.getHtml().xpath("//dd[@class='work_name']//a/@href").all()
                    .stream().map(e -> e + "/?locale=zh_CN").collect(Collectors.toList());
            List<String> allPage = page.getHtml().xpath("//td[@class='page_no']//a/@href").all();
            page.addTargetRequests(allPage);
            page.addTargetRequests(alldata);
        } else if (url.contains("product_id")) {
            DlModel field = CommonUtil.handleAnotation(page.getHtml().get(), new DlModel(), false);

            String code = ReUtil.findAllGroup0("(?<=product_id/)[\\s\\S]*?(?=.html)", url).stream().findFirst().orElse("");

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