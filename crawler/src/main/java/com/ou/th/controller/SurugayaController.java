package com.ou.th.controller;

import cn.hutool.core.util.StrUtil;
import com.ou.th.crawler.SurugayaScrawler;
import com.ou.th.crawler.surugaya.SurugayaModel;
import com.ou.th.crawler.surugaya.SurugayaService;
import com.ou.th.util.FastdfsUtil;
import com.ou.th.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    FastdfsUtil fastdfsUtil;

    @GetMapping(value = "start")
    public void surugaya() throws IOException {
        surugayaScrawler.start();
    }

    @GetMapping("list")
    public Msg list(String keyword) {
        List<SurugayaModel> surugayaModels = StrUtil.isEmpty(keyword) ? surugayaService.list() : surugayaService.list(keyword);
        // flushImg(surugayaModels);
        return Msg.success(surugayaModels);
    }

    @PutMapping("/flushImg/{id}")
    public Msg flushImg(@PathVariable String id) {
        SurugayaModel surugayaModel = surugayaService.getById(id).get();
        // surugayaModel.setPicture(fastdfsUtil.uploadFromUrl(surugayaModel.getPicturesOriginal()));

        // surugayaService.save(surugayaModel);

        return Msg.success(surugayaModel);
    }

    @Async
    public void flushImg(List<SurugayaModel> surugayaModels) {
        List<SurugayaModel> models = surugayaModels.stream().filter(e -> e.getPicture().contains("https://"))
                // .peek(e -> e.setPicture(fastdfsUtil.uploadFromUrl(e.getPicturesOriginal())))
                .collect(Collectors.toList());
        surugayaService.save(models);
    }

    @PutMapping
    public Msg update(@RequestBody List<SurugayaModel> surugayaModels) {
        surugayaService.save(surugayaModels);
        return Msg.success();
    }


    @PatchMapping
    public Msg del(@RequestBody List<String> ids) {
        surugayaService.del(ids);
        return Msg.success();
    }
}
