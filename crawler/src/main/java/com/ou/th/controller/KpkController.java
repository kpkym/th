package com.ou.th.controller;

import com.ou.th.crawler.KpkCrawler;
import com.ou.th.crawler.kpk.KpkModel;
import com.ou.th.crawler.kpk.KpkRepo;
import com.ou.th.crawler.kpk.KpkService;
import com.ou.th.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-20 03:21
 */
@RestController
@RequestMapping("kpk")
public class KpkController {
    @Autowired
    KpkCrawler kpkCrawler;

    @Autowired
    KpkService kpkService;

    @Autowired
    KpkRepo repo;

    @GetMapping(value = "start")
    public void start() throws IOException {
        kpkCrawler.start();
    }

    @GetMapping("file")
    public Msg list(String keyword) {
        return null;
    }

    @PutMapping
    public Msg update(@RequestBody List<KpkModel> kpkModels) {
        return Msg.success();
    }

    @PatchMapping
    public Msg del(@RequestBody List<String> ids) {
        return Msg.success();
    }
}
