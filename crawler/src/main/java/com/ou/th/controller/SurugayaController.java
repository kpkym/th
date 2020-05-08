package com.ou.th.controller;

import cn.hutool.core.util.StrUtil;
import com.ou.th.crawler.SurugayaScrawler;
import com.ou.th.crawler.surugaya.SurugayaModel;
import com.ou.th.crawler.surugaya.SurugayaService;
import com.ou.th.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public Msg list(String keyword) {
        return Msg.success(StrUtil.isEmpty(keyword) ? surugayaService.list() : surugayaService.list(keyword));
    }

    @PutMapping("")
    public Msg update(@RequestBody List<SurugayaModel> surugayaModels) {
        surugayaService.save(surugayaModels);
        return Msg.success();
    }


    @PatchMapping("")
    public Msg del(@RequestBody List<String> ids) {
        surugayaService.del(ids);
        return Msg.success();
    }
}
