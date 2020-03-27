package com.ou.th.crawler.mercari.service.impl;

import com.ou.th.crawler.mercari.dao.DataRepo;
import com.ou.th.crawler.mercari.model.MercariModel;
import com.ou.th.crawler.mercari.service.MercariService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
@Slf4j
@Service
public class MercariServiceImpl implements MercariService {
    @Autowired
    DataRepo dataRepo;

    @Override
    public MercariModel getByPid(String pid) {
        return dataRepo.findById(pid).orElse(new MercariModel());
    }

    @Override
    public List<MercariModel> list() {
        List<MercariModel> mercariModels = StreamSupport
                .stream(dataRepo.findAll().spliterator(), false)
                .filter(e -> !e.isDisliked())
                .collect(Collectors.toList());
        log.info("获取列表数据总数：" + mercariModels.size());
        return mercariModels;
    }

    @Override
    public MercariModel save(MercariModel mercariModel) {
        return dataRepo.save(mercariModel);
    }
}
