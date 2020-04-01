package com.ou.th.crawler.surugaya;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author kpkym
 * Date: 2020-04-01 15:23
 */
@Slf4j
@Service
public class SurugayaService {
    @Autowired
    SurugayaRepo surugayaRepo;

    public Optional<SurugayaModel> getById(String id) {
        return surugayaRepo.findById(id);
    }

    public List<SurugayaModel> list() {
        List<SurugayaModel> surugayaModels = StreamSupport
                .stream(surugayaRepo.findAll().spliterator(), false)
                .filter(e -> !e.getIsDel())
                .collect(Collectors.toList());
        log.info("获取列表数据总数：" + surugayaModels.size());
        return surugayaModels;
    }

    public SurugayaModel save(SurugayaModel surugayaModel) {
        return surugayaRepo.save(surugayaModel);
    }
}
