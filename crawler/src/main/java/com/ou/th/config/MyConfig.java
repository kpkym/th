package com.ou.th.config;

import com.ou.th.crawler.common.config.MyHttpClientDownloader;
import com.ou.th.crawler.mercari.MercariPageProcessor;
import com.ou.th.crawler.mercari.MercariPipline;
import com.ou.th.crawler.mercari.MyMercariHashSetDuplicateRemover;
import com.ou.th.crawler.surugaya.MySurugayaHashSetDuplicateRemover;
import com.ou.th.crawler.surugaya.SurugayaModel;
import com.ou.th.crawler.surugaya.SurugayaPageProcessor;
import com.ou.th.crawler.surugaya.SurugayaPipline;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author kpkym
 * Date: 2020-03-23 11:12
 */
@Configuration
public class MyConfig {
    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin("*");
        //是否发送Cookie信息
        config.setAllowCredentials(true);
        //放行哪些原始域(请求方式)
        config.addAllowedMethod("*");
        //放行哪些原始域(头部信息)
        config.addAllowedHeader("*");
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        // config.addExposedHeader("*");

        //2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }

    @Bean
    public HttpClientDownloader httpClientDownloader(KpkConfig kpkConfig) {
        HttpClientDownloader httpClientDownloader = new MyHttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy(kpkConfig.getProxy().getHost(), kpkConfig.getProxy().getPort())));

        return httpClientDownloader;
    }

    @Bean
    public Spider mercariSpider(MercariPageProcessor pageProcessor,
                              MercariPipline pipeline,
                              HttpClientDownloader httpClientDownloader,
                              KpkConfig kpkConfig) {
        Spider spider = Spider.create(pageProcessor);
        spider = spider.addUrl(kpkConfig.getMercariUrls().toArray(new String[0]));
        spider.setScheduler(new QueueScheduler()
                .setDuplicateRemover(new MyMercariHashSetDuplicateRemover())
        );

        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(20);
        return spider;
    }

    @Bean
    public Spider surugayaSpider(SurugayaPageProcessor pageProcessor,
                                 SurugayaPipline pipeline,
                                 HttpClientDownloader httpClientDownloader,
                                 KpkConfig kpkConfig) {
        Spider spider = Spider.create(pageProcessor);
        spider = spider.addUrl(kpkConfig.getSurugayaUrls().toArray(new String[0]));
        spider.setScheduler(new QueueScheduler()
                .setDuplicateRemover(new MySurugayaHashSetDuplicateRemover())
        );

        spider.setDownloader(httpClientDownloader);
        spider.addPipeline(pipeline);
        spider.thread(20);
        return spider;
    }

    @Bean
    public CopyOnWriteArrayList<SurugayaModel> asyncSurugayaArr() {
        return new CopyOnWriteArrayList<>();
    }
}
