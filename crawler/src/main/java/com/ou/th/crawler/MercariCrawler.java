package com.ou.th.crawler;

import cn.hutool.core.util.StrUtil;
import com.ou.th.crawler.mercari.config.MyHttpClientDownloader;
import com.ou.th.crawler.mercari.crawler.MercariPageProcessor;
import com.ou.th.crawler.mercari.pipeline.MercariPipline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.BufferedReader;
import java.io.FileReader;
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

    @Value("${configFile}")
    String configFile;

    @Value("${proxy.host}")
    String proxyHost;

    @Value("${proxy.port}")
    Integer proxyPort;

    public void start() throws IOException {
        String url = null;
        FileReader reader = new FileReader(configFile);
        BufferedReader br = new BufferedReader(reader);
        url = br.readLine();

        Spider spider = Spider.create(pageProcessor);


        HttpClientDownloader httpClientDownloader = new MyHttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy(proxyHost, proxyPort)));

        while (url != null) {
            if (StrUtil.isNotEmpty(url)) {
                spider = spider.addUrl(url);
            }
            url = br.readLine();
        }
        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(5);
        spider.start();
    }
}
