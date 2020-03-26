package com.ou.th.crawler.mercari.dao;

import com.ou.th.crawler.mercari.model.MercariModel;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
public interface DataRepo extends PagingAndSortingRepository<MercariModel, String> {
}



