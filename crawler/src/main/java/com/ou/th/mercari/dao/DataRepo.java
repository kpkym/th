package com.ou.th.mercari.dao;

import com.ou.th.mercari.model.MercarModel;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
public interface DataRepo extends PagingAndSortingRepository<MercarModel, String> {
}



