package com.ou.th.controller;

import com.ou.th.Msg;
import com.ou.th.crawler.MercariCrawler;
import com.ou.th.crawler.mercari.dao.DataRepo;
import com.ou.th.crawler.mercari.model.MercariModel;
import com.ou.th.crawler.mercari.service.MercariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    MercariService mercariService;

    @Autowired
    DataRepo da;

    @GetMapping(value = "start")
    public void mercari() throws IOException {
        mercariCrawler.start();
    }

    @GetMapping("list")
    public Msg list() {
        return Msg.success(mercariService.list());
    }

    @PostMapping("update")
    public Msg update(@RequestBody MercariModel mercariModel) {
        return Msg.success(mercariService.save(mercariModel));
    }
}
