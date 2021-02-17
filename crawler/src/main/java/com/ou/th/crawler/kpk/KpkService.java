package com.ou.th.crawler.kpk;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import com.ou.th.crawler.mercari.MyMercariHashSetDuplicateRemover;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author kpkym
 * Date: 2020-10-31 06:41
 */
@Slf4j
@Service
public class KpkService {
    @Autowired
    KpkRepo kpkRepo;

    public Optional<KpkModel> getById(String id) {
        return kpkRepo.findById(id);
    }

    public List<KpkModel> list() {
        // List<KpkModel> mercariModels = kpkRepo.findAllByIsDelFalseAndIsDontCrawlerFalse();
        // log.info("获取列表数据总数：" + mercariModels.size());
        return null;
    }

    public void save(KpkModel mercariModel) {
        kpkRepo.save(mercariModel);
    }

    public void save(List<KpkModel> mercariModels) {
        kpkRepo.saveAll(mercariModels);
    }
}
