package com.ou.th.crawler;

import com.ou.th.config.KpkConfig;
import com.ou.th.crawler.log.CrawlerLogService;
import com.ou.th.crawler.mercari.MercariPageProcessor;
import com.ou.th.crawler.mercari.MercariPipline;
import com.ou.th.crawler.mercari.MyMercariHashSetDuplicateRemover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @author kpkym
 * Date: 2020-03-19 08:08
 */
@Component
public class MercariCrawler {
    @Autowired
    MercariPageProcessor pageProcessor;

    @Autowired
    MercariPipline pipeline;

    @Autowired
    HttpClientDownloader httpClientDownloader;

    @Autowired
    KpkConfig kpkConfig;

    @Autowired
    CrawlerLogService logService;

    @Scheduled(cron = "2 23,59 0-1,8-23 * * *")
    public void start() {
        Spider spider = Spider.create(pageProcessor);
        spider = spider.addUrl(kpkConfig.getMercariUrls().toArray(new String[0]));
        spider.setScheduler(new QueueScheduler()
                .setDuplicateRemover(new MyMercariHashSetDuplicateRemover())
        );
        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(20);
        spider.start();

        logService.logStartd("煤炉");
    }
}
