package com.ou.th.controller;

import cn.hutool.core.util.StrUtil;
import com.ou.th.crawler.MercariCrawler;
import com.ou.th.crawler.mercari.MercariModel;
import com.ou.th.crawler.mercari.MercariService;
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
@RequestMapping("mercari")
public class MercariController {
    @Autowired
    MercariCrawler mercariCrawler;

    @Autowired
    MercariService mercariService;

    @GetMapping(value = "start")
    public void mercari() throws IOException {
        mercariCrawler.start();
    }

    @GetMapping("list")
    public Msg list(String keyword) {
        return Msg.success(StrUtil.isEmpty(keyword) ? mercariService.list() : mercariService.list(keyword));
    }

    @PutMapping("")
    public Msg update(@RequestBody List<MercariModel> mercariModels) {
        mercariService.save(mercariModels);
        return Msg.success();
    }


    @PatchMapping("")
    public Msg del(@RequestBody List<String> ids) {
        mercariService.del(ids);
        return Msg.success();
    }
}
