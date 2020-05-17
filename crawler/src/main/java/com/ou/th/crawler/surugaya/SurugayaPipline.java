package com.ou.th.crawler.surugaya;

import cn.hutool.core.collection.CollUtil;
import com.ou.th.crawler.common.CommonPipline;
import com.ou.th.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author kpkym
 * Date: 2020-04-01 15:06
 */
@Component
public class SurugayaPipline implements Pipeline, Closeable {
    @Autowired
    SurugayaService surugayaService;

    @Autowired
    FastdfsUtil fastdfsUtil;

    @Autowired
    SurugayaRepo surugayaRepo;

    CopyOnWriteArrayList<SurugayaModel> asyncSurugayaArr = new CopyOnWriteArrayList<>();

    Lock lock = new ReentrantLock();


    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("arr") != null) {
            List<SurugayaModel> surugayaModels = resultItems.get("arr");
            for (SurugayaModel surugayaModel : surugayaModels) {
                save(surugayaModel, resultItems);
            }
        } else {
            save(resultItems.get("obj"), resultItems);
        }
    }

    private void save(SurugayaModel newer, ResultItems resultItems) {
        String id = SurugayaUtil.getIdFrom(newer.getUrl());
        SurugayaModel older = surugayaService.getById(id).orElse(new SurugayaModel());
        if (older.getId() == null) {
            initSave(newer, id);
        } else if (!CollUtil.getLast(older.getPriceTimes()).getPrice().equals(newer.getPrice())) {
            CommonPipline.needUpdate(older, newer);
            older.setIsChange(true);
            older.setIsDontCrawler(false);
            older.setIsDel(false);
            older.getPriceTimes().add(
                    SurugayaModel.PriceTime.builder()
                            .dateTime(new Date().getTime())
                            .promotion(older.getPromotion())
                            .price(older.getPrice())
                            .build()
            );
            // 如果是指定详情页面就删除队列中的数据，然后再添加
            if (resultItems.getRequest().getUrl().contains("product/detail")) {
                try {
                    lock.lock();
                    asyncSurugayaArr.remove(older);
                    asyncSurugayaArr.addIfAbsent(older);
                }finally {
                    lock.unlock();
                }
            } else {
                asyncSurugayaArr.addIfAbsent(older);
            }
        }
    }

    private void initSave(SurugayaModel mercariModel, String id) {
        // 只上传第一张图片
        mercariModel.setId(id);
        mercariModel.setPicture(fastdfsUtil.uploadFromUrl(mercariModel.getPicturesOriginal()));
        mercariModel.getPriceTimes().add(
                SurugayaModel.PriceTime.builder()
                        .dateTime(new Date().getTime())
                        .price(mercariModel.getPrice())
                        .build()
        );
        asyncSurugayaArr.addIfAbsent(mercariModel);
    }

    @Override
    public void close() throws IOException {
        surugayaService.save(asyncSurugayaArr);
        asyncSurugayaArr.clear();
    }
}
