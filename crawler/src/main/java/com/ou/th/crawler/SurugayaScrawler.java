package com.ou.th.crawler;

import com.ou.th.config.KpkConfig;
import com.ou.th.crawler.surugaya.MySurugayaHashSetDuplicateRemover;
import com.ou.th.crawler.surugaya.SurugayaPageProcessor;
import com.ou.th.crawler.surugaya.SurugayaPipline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @author kpkym
 * Date: 2020-03-31 16:52
 */
@Component
public class SurugayaScrawler {
    @Autowired
    SurugayaPageProcessor pageProcessor;

    @Autowired
    SurugayaPipline pipeline;

    @Autowired
    HttpClientDownloader httpClientDownloader;

    @Autowired
    KpkConfig kpkConfig;

    @Scheduled(cron = "2 23,59 * * * *")
    public void start() {
        Spider spider = Spider.create(pageProcessor);
        spider = spider.addUrl(kpkConfig.getSurugayaUrls().toArray(new String[0]));

        spider.setScheduler(new QueueScheduler()
                .setDuplicateRemover(new MySurugayaHashSetDuplicateRemover())
        );

        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(20);
        spider.start();
    }
}
