package com.ou.th.crawler;

import cn.hutool.core.util.ArrayUtil;
import com.ou.th.config.KpkConfig;
import com.ou.th.crawler.dl.DlPageProcessor;
import com.ou.th.crawler.dl.DlPipline;
import com.ou.th.crawler.dl.MyDlHashSetDuplicateRemover;
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
public class DlCrawler {
    @Autowired
    DlPageProcessor pageProcessor;

    @Autowired
    DlPipline pipeline;

    @Autowired
    HttpClientDownloader httpClientDownloader;

    @Autowired
    KpkConfig kpkConfig;

    @Autowired
    CrawlerLogService logService;

    public void start() {
        Spider spider = Spider.create(pageProcessor);

        spider = spider.addUrl(ArrayUtil.toArray(kpkConfig.getDlUrls(), String.class));
        spider.setScheduler(new QueueScheduler()
                .setDuplicateRemover(new MyDlHashSetDuplicateRemover())
        );
        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(50);
        spider.start();

        logService.logStartd("dl");
    }
}
