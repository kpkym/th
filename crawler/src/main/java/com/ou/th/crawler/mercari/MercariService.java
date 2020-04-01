package com.ou.th.crawler.mercari;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
@Slf4j
@Service
public class MercariService {
    @Autowired
    MercariRepo mercariRepo;

    public Optional<MercariModel> getById(String id) {
        return mercariRepo.findById(id);
    }

    public List<MercariModel> list() {
        List<MercariModel> mercariModels = mercariRepo.findAllByIsDelFalse();
        log.info("获取列表数据总数：" + mercariModels.size());
        return mercariModels;
    }

    public MercariModel save(MercariModel mercariModel) {
        return mercariRepo.save(mercariModel);
    }
}
