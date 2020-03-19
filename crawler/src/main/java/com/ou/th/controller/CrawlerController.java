package com.ou.th.controller;

import com.ou.th.crawler.MercariCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author kpkym
 * Date: 2020-03-20 00:06
 */
@RestController
public class CrawlerController {
    @Autowired
    MercariCrawler mercariCrawler;

    @GetMapping(value = "/")
    public void mercari() throws IOException {
        mercariCrawler.start();
    }
}
