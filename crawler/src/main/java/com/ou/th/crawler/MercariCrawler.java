package com.ou.th.crawler;

import com.ou.th.config.KpkConfig;
import com.ou.th.crawler.common.config.MyHttpClientDownloader;
import com.ou.th.crawler.mercari.MercariPageProcessor;
import com.ou.th.crawler.mercari.MercariPipline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.IOException;

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
    KpkConfig kpkConfig;

    public void start() throws IOException {

        Spider spider = Spider.create(pageProcessor);


        HttpClientDownloader httpClientDownloader = new MyHttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy(kpkConfig.getProxy().getHost(), kpkConfig.getProxy().getPort())));

        spider = spider.addUrl(kpkConfig.getMercariUrls().toArray(new String[0]));
        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(20);
        spider.start();
    }
}
