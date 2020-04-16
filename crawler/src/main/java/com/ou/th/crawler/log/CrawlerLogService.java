package com.ou.th.crawler.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author kpkym
 * Date: 2020-04-16 19:48
 */
@Slf4j
@Service
public class CrawlerLogService {
    @Autowired
    CrawlerLogRepo logRepo;

    public void logStartd(String site) {
        Date date = new Date();
        log.info("开始爬取：" + site + "。时间：" + date.toLocaleString());
        logRepo.save(CrawlerLogModel.builder()
                .site(site)
                .time(date.getTime()).build()
        );
    }

}
