package com.ou.th.crawler.mercari.service;


import com.ou.th.crawler.mercari.model.MercariModel;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-19 06:40
 */
public interface MercariService {
    MercariModel save(MercariModel mercariModel);

    MercariModel getByPid(String pid);

    List<MercariModel> list();
}
