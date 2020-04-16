package com.ou.th.crawler.log;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author kpkym
 * Date: 2020-04-16 19:49
 */
public interface CrawlerLogRepo extends PagingAndSortingRepository<CrawlerLogModel, String> {
}
