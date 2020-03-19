package com.ou.th.controller;

import com.ou.th.crawler.MercariCrawler;
import com.ou.th.mercari.service.MercarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-20 03:21
 */
@RestController
@RequestMapping("mercari")
public class MercariController {
    @Autowired
    MercariCrawler mercariCrawler;

    @Autowired
    MercarService mercarService;

    @GetMapping(value = "start")
    public void mercari() throws IOException {
        mercariCrawler.start();
    }

    @GetMapping("list")
    public List list() {
        return mercarService.list();
    }
}
