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
    MercariRepo mercariRepo;

    public Optional<MercariModel> getById(String id) {
        return mercariRepo.findById(id);
    }

    public List<MercariModel> list() {
        List<MercariModel> mercariModels = mercariRepo.findAllByIsDelFalse();
        log.info("获取列表数据总数：" + mercariModels.size());
        return mercariModels;
    }

    public void save(MercariModel mercariModel) {
        mercariRepo.save(mercariModel);
    }

    public void save(List<MercariModel> mercariModels) {
        mercariRepo.saveAll(mercariModels);
    }


    public void del(List<String> ids) {
        List<MercariModel> mercariModels = StreamSupport.stream(mercariRepo.findAllById(ids).spliterator(), false)
                .peek(e -> e.setIsDel(true))
                .collect(Collectors.toList());
        save(mercariModels);
    }
}
