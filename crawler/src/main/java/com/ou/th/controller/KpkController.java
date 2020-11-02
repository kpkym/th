package com.ou.th.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.ou.th.crawler.KpkCrawler;
import com.ou.th.crawler.MercariCrawler;
import com.ou.th.crawler.kpk.KpkModel;
import com.ou.th.crawler.kpk.KpkRepo;
import com.ou.th.crawler.kpk.KpkService;
import com.ou.th.crawler.mercari.MercariModel;
import com.ou.th.crawler.mercari.MercariService;
import com.ou.th.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public void mercari() throws IOException {
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
