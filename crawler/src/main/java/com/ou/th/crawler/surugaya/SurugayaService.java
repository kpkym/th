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
        List<SurugayaModel> surugayaModels = surugayaRepo.findAllByIsDelFalseAndIsDontCrawlerFalse();
        log.info("获取列表数据总数：" + surugayaModels.size());
        return surugayaModels;
    }

    public List<SurugayaModel> list(String keyword) {
        List<SurugayaModel> surugayaModels = surugayaRepo.findAllByIdOrTitleContainsIgnoreCase(keyword, keyword);
        log.info("获取列表数据总数(关键字[" + keyword + "])：" + surugayaModels.size());
        return surugayaModels;
    }

    public void save(SurugayaModel surugayaModel) {
        surugayaRepo.save(surugayaModel);
    }

    public void save(List<SurugayaModel> surugayaModels) {
        surugayaRepo.saveAll(surugayaModels);
    }

    public void del(List<String> ids) {
        List<SurugayaModel> surugayaModels = StreamSupport.stream(surugayaRepo.findAllById(ids).spliterator(), false)
                .peek(e -> {
                    e.setIsDel(true);
                    e.setIsChange(false);
                    e.setIsLike(false);
                })
                .collect(Collectors.toList());
        save(surugayaModels);
    }


}
