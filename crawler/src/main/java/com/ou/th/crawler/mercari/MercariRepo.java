package com.ou.th.crawler.mercari;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
public interface MercariRepo extends PagingAndSortingRepository<MercariModel, String> {
    List<MercariModel> findAllByIsDelFalseAndIsDontCrawlerFalse();
    List<MercariModel> findAllByIdOrTitleContainsIgnoreCase(String id, String keyword);
}



