package com.ou.th.crawler;

import com.ou.th.crawler.log.CrawlerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * @author kpkym
 * Date: 2020-03-31 16:52
 */
@Component
public class SurugayaScrawler {
    @Autowired
    @Qualifier("surugayaSpider")
    Spider surugayaSpider;

    @Autowired
    CrawlerLogService logService;

    @Scheduled(cron = "7 6,39 * * * *")
    public void start() {
        surugayaSpider.start();

        logService.logStartd("骏河屋");
    }
}
