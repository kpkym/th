package com.ou.th.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import com.ou.th.crawler.DlCrawler;
import com.ou.th.crawler.dl.DlModel;
import com.ou.th.crawler.dl.DlRepo;
import com.ou.th.crawler.dl.DlService;
import com.ou.th.util.RjsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author dlym
 * Date: 2020-03-20 03:21
 */
@RestController
@RequestMapping("dl")
public class DlController {
    @Autowired
    DlCrawler dlCrawler;

    @Autowired
    DlService dlService;

    @Autowired
    DlRepo repo;

    @GetMapping(value = "start")
    public void start() throws IOException {
         dlCrawler.start();
    }

    @GetMapping(value = "printAllRjs")
    public void printAllRjs() throws IOException {
        Set<String> all = RjsUtil.getAll();
        FileUtil.writeString(all.toString(), "~/Desktop/rjs.txt", CharsetUtil.UTF_8);
    }

    @GetMapping(value = "abc")
    public void abc() throws IOException {

    }

    @GetMapping(value = "list")
    public List<DlModel> list() throws IOException {
        List<DlModel> list = dlService.list();

        return list;
    }
}
