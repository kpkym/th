package com.ou.th.crawler.surugaya;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-04-01 17:11
 */
public interface SurugayaRepo extends PagingAndSortingRepository<SurugayaModel, String> {
    List<SurugayaModel> findAllByIsDelFalseAndIsDontCrawlerFalse();
    List<SurugayaModel> findAllByIdOrTitleContainsIgnoreCase(String id, String keyword);
}
