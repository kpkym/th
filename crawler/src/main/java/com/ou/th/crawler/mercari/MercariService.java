package com.ou.th.crawler.mercari;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import com.ou.th.crawler.common.CommonUtil;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import com.ou.th.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
@Slf4j
@Service
public class MercariService {
    @Autowired
    MercariRepo mercariRepo;

    @Autowired
    HttpClientDownloader httpClientDownloader;

    @Autowired
    HttpUtil httpUtil;

    public Optional<MercariModel> getById(String id) {
        return mercariRepo.findById(id);
    }

    public List<MercariModel> list() {
        List<MercariModel> mercariModels = mercariRepo.findAllByIsDelFalseAndIsDontCrawlerFalse();
        log.info("获取列表数据总数：" + mercariModels.size());
        return mercariModels;
    }

    public void save(MercariModel mercariModel) {
        mercariRepo.save(mercariModel);
    }

    public void save(List<MercariModel> mercariModels) {
        mercariRepo.saveAll(mercariModels);
    }


    public void del(List<String> ids) {
        List<MercariModel> mercariModels = StreamSupport.stream(mercariRepo.findAllById(ids).spliterator(), false)
                .peek(e -> {
                    e.setIsDel(true);
                    e.setIsChange(false);
                    e.setIsLike(false);
                })
                .collect(Collectors.toList());
        save(mercariModels);
    }

    public List<MercariModel> list(String keyword) {
        if (keyword.startsWith("+")) {
            keyword = keyword.substring(1);
            Map<String, MercariModel> notSolds = mercariRepo.findAllByIdOrTitleContainsIgnoreCase(keyword, keyword).stream()
                    .filter(e -> !new Integer(1).equals(e.getIsSold()))
                    .collect(Collectors.toMap(MercariModel::getId, e -> e));

            List<MercariModel> delMercariModels = new ArrayList<>();
            List<MercariModel> updatedMercariModels = new ArrayList<>();

            Spider spider = Spider.create(new PageProcessor() {
                @Override
                public void process(Page page) {
                    MercariModel mercariModel = notSolds.get(MercariUtil.getIdFrom(page.getRequest().getUrl()));
                    if (page.getHtml().xpath("//h2[@class='deleted-item-name']/text()").get() != null) {
                        delMercariModels.add(mercariModel);
                    } else {
                        mercariModel.setIsSold(page.getHtml().xpath("//div[@class='item-photo']/div[@class='item-sold-out-badge']/div/text()").get() == null
                                ? 0 : 1);
                        if (mercariModel.getIsSold() == 1) {
                            mercariModel.setIsChange(true);
                            try {
                                BigDecimal price = CommonUtil.StrToBigdecimal(page.getHtml().xpath(mercariModel.getClass().getDeclaredField("price").getAnnotation(MyExtractBy.class).detail()).get());
                                if (!CollUtil.getLast(mercariModel.getPriceTimes()).getPrice().equals(price)) {
                                    mercariModel.setPrice(price);
                                    mercariModel.getPriceTimes().add(
                                            MercariModel.PriceTime.builder()
                                                    .dateTime(new Date().getTime())
                                                    .price(mercariModel.getPrice())
                                                    .build()
                                    );
                                }
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            }

                        }
                        updatedMercariModels.add(mercariModel);
                    }
                }
                @Override
                public Site getSite() {
                    return Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(100000);
                }
            });

            spider = spider.addUrl(notSolds.values().stream().map(e -> "https://www.mercari.com/" +e.getUrl()).toArray(String[]::new));
            spider.setScheduler(new QueueScheduler()
                    .setDuplicateRemover(new MyMercariHashSetDuplicateRemover())
            );
            spider.setDownloader(httpClientDownloader);
            spider.thread(20);
            spider.run();

            mercariRepo.deleteAll(delMercariModels);
            mercariRepo.saveAll(updatedMercariModels);
        }

        List<MercariModel> mercariModels = mercariRepo.findAllByIdOrTitleContainsIgnoreCase(keyword, keyword);
        log.info("获取列表数据总数(关键字[" + keyword + "])：" + mercariModels.size());
        return mercariModels;
    }


    // 删除无效数据，更新已经出售商品
    public void updateSold(List<MercariModel> mercariModels) {
        List<MercariModel> notSolds = mercariModels.stream()
                .filter(e -> e.getIsSold() == null || new Integer(0).equals(e.getIsSold()))
                .collect(Collectors.toList());

        List<MercariModel> delMercariModels = new ArrayList<>();
        List<MercariModel> updatedMercariModels = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (MercariModel mercariModel : notSolds) {
            executorService.submit(() -> {
                try (CloseableHttpClient client = httpUtil.getClient();
                     InputStream is = httpUtil.getInputStream("https://www.mercari.com/" + mercariModel.getUrl(), client)
                ) {
                    FastByteArrayOutputStream read = IoUtil.read(is);
                    if (new Html(read.toString()).xpath("//h2[@class='deleted-item-name']/text()").get() != null) {
                        delMercariModels.add(mercariModel);
                    } else {
                        mercariModel.setIsSold(new Html(read.toString()).xpath("//div[@class='item-photo']/div[@class='item-sold-out-badge']/div/text()").get() == null
                                ? 0 : 1);
                        updatedMercariModels.add(mercariModel);
                    }
                    Thread.sleep(777);
                } catch (Exception e) {
                    log.error("SSLHandshakeException 错误, URL=" + mercariModel.getUrl());
                    e.printStackTrace();
                }
            });

        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mercariRepo.deleteAll(delMercariModels);
        mercariRepo.saveAll(updatedMercariModels);
    }
}
