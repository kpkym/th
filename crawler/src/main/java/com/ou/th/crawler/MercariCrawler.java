package com.ou.th.crawler;

import com.ou.th.crawler.log.CrawlerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * @author kpkym
 * Date: 2020-03-19 08:08
 */
@Component
public class MercariCrawler {
    @Autowired
    @Qualifier("mercariSpider")
    Spider mercariSpider;

    @Autowired
    CrawlerLogService logService;

    @Scheduled(cron = "2 23,59 * * * *")
    public void start() {
        mercariSpider.start();

        logService.logStartd("煤炉");
    }
}
