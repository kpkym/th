package com.ou.th.crawler.log;

import com.ou.th.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kpkym
 * Date: 2020-04-17 19:01
 */
@RestController
public class CrawlerLogController {
    @Autowired
    CrawlerLogService crawlerLogService;

    @GetMapping("log")
    public Msg list() {
        return Msg.success(crawlerLogService.list());
    }
}
