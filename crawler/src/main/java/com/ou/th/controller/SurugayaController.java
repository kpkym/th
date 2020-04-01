package com.ou.th.controller;

import com.ou.th.crawler.SurugayaScrawler;
import com.ou.th.crawler.surugaya.SurugayaModel;
import com.ou.th.crawler.surugaya.SurugayaService;
import com.ou.th.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author kpkym
 * Date: 2020-04-01 17:48
 */
@RestController
@RequestMapping("surugaya")
public class SurugayaController {
    @Autowired
    SurugayaScrawler surugayaScrawler;

    @Autowired
    SurugayaService surugayaService;

    @GetMapping(value = "start")
    public void surugaya() throws IOException {
        surugayaScrawler.start();
    }

    @GetMapping("list")
    public Msg list() {
        return Msg.success(surugayaService.list());
    }

    @PostMapping("update")
    public Msg update(@RequestBody SurugayaModel surugayaModel) {
        return Msg.success(surugayaService.save(surugayaModel));
    }
}
