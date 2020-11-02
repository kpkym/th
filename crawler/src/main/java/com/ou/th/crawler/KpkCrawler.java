package com.ou.th.crawler;

import com.ou.th.config.KpkConfig;
import com.ou.th.crawler.kpk.KpkPageProcessor;
import com.ou.th.crawler.kpk.KpkPipline;
import com.ou.th.crawler.kpk.MyKpkHashSetDuplicateRemover;
import com.ou.th.crawler.log.CrawlerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @author kpkym
 * Date: 2020-03-19 08:08
 */
@Component
public class KpkCrawler {
    @Autowired
    KpkPageProcessor pageProcessor;

    @Autowired
    KpkPipline pipeline;

    @Autowired
    HttpClientDownloader httpClientDownloader;

    @Autowired
    KpkConfig kpkConfig;

    @Autowired
    CrawlerLogService logService;

    public void start() {
        Spider spider = Spider.create(pageProcessor);
        spider = spider.addUrl(kpkConfig.getKpkUrls().toArray(new String[0]));
        spider.setScheduler(new QueueScheduler()
                .setDuplicateRemover(new MyKpkHashSetDuplicateRemover())
        );
        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(20);
        spider.start();

        logService.logStartd("kpk");
    }
}
