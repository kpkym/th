package com.ou.th.crawler.surugaya;

import com.ou.th.crawler.common.CommonPipline;
import com.ou.th.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-04-01 15:06
 */
@Component
public class SurugayaPipline implements Pipeline {
    @Autowired
    SurugayaService surugayaService;

    @Autowired
    FastdfsUtil fastdfsUtil;


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

    private boolean hasPriceTag(ResultItems resultItems) {
        return resultItems.getRequest().getUrl().contains("price");
    }

    private void save(SurugayaModel newer, ResultItems resultItems) {
        String id = SurugayaUtil.getIdFrom(newer.getUrl());
        SurugayaModel older = surugayaService.getById(id).orElse(new SurugayaModel());
        if (older.getId() == null) {
            initSave(newer, id);
        } else if (!older.getPrice().equals(newer.getPrice())) {
            // 有价格标签跟标记不抓取则不抓取(防止点错)
            if (hasPriceTag(resultItems) && older.getIsDontCrawler()) {
                return;
            }
            CommonPipline.needUpdate(older, newer);
            older.setIsChange(true);
            older.setIsDontCrawler(false);
            older.setIsDel(false);
            older.getPriceTimes().add(
                    SurugayaModel.PriceTime.builder()
                            .dateTime(new Date().getTime())
                            .price(older.getPrice())
                            .build()
            );
            surugayaService.save(older);
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
        surugayaService.save(mercariModel);
    }
}
