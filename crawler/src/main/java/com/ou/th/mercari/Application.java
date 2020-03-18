package com.ou.th.mercari;

import cn.hutool.core.util.StrUtil;
import com.ou.th.mercari.config.MyHttpClientDownloader;
import com.ou.th.mercari.crawler.MercariPageProcessor;
import com.ou.th.mercari.pipeline.MercariPipline;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author kpkym
 * Date: 2020-03-16 20:50
 */
public class Application {
    public static void main(String[] args) throws IOException {
        String url = null;
        FileReader reader = new FileReader(Thread.currentThread().getContextClassLoader().getResource("initURL.kpk").getFile());
        BufferedReader br = new BufferedReader(reader);
        url = br.readLine();

        Spider spider = Spider.create(new MercariPageProcessor());

        HttpClientDownloader httpClientDownloader = new MyHttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("127.0.0.1", 1087)));

        while (url != null) {
            if (StrUtil.isNotEmpty(url)) {
                spider = spider.addUrl(url);
            }
            url = br.readLine();
        }
        spider.setDownloader(httpClientDownloader).addPipeline(new MercariPipline()).thread(5).run();

    }
}
