package com.ou.th.crawler.mercari;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
@Slf4j
@Service
public class MercariService {
    @Autowired
    DataRepo dataRepo;

    public Optional<MercariModel> getById(String id) {
        return dataRepo.findById(id);
    }

    public List<MercariModel> list() {
        List<MercariModel> mercariModels = StreamSupport
                .stream(dataRepo.findAll().spliterator(), false)
                .filter(e -> !e.getIsDel())
                .collect(Collectors.toList());
        log.info("获取列表数据总数：" + mercariModels.size());
        return mercariModels;
    }

    public MercariModel save(MercariModel mercariModel) {
        return dataRepo.save(mercariModel);
    }
}
