package com.ou.th.crawler.mercari;

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
 * Date: 2020-03-16 21:18
 */
@Component
public class MercariPipline implements Pipeline {
    @Autowired
    MercariService mercariService;

    @Autowired
    FastdfsUtil fastdfsUtil;


    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("arr") != null) {
            List<MercariModel> mercariModels = resultItems.get("arr");
            for (MercariModel mercariModel : mercariModels) {
                save(mercariModel);
            }
        } else {
            save(resultItems.get("obj"));
        }
    }

    private void save(MercariModel newer) {
        String id = MercariUtil.getIdFrom(newer.getUrl());
        MercariModel older = mercariService.getById(id).orElse(new MercariModel());
        if (older.getId() == null) {
            initSave(newer, id);
        } else if (!older.getIsDontCrawler() && !older.getPrice().equals(newer.getPrice())) {
            CommonPipline.needUpdate(older, newer);
            older.setIsChange(true);
            older.setIsDel(false);
            older.getPriceTimes().add(
                    MercariModel.PriceTime.builder()
                            .dateTime(new Date().getTime())
                            .price(older.getPrice())
                            .build()
            );
            mercariService.save(older);
        }
    }

    private void initSave(MercariModel mercariModel, String id) {
        // 只上传第一张图片
        mercariModel.setId(id);
        mercariModel.setPicture(fastdfsUtil.uploadFromUrl(mercariModel.getPicturesOriginal()));
        mercariModel.getPriceTimes().add(
                MercariModel.PriceTime.builder()
                        .dateTime(new Date().getTime())
                        .price(mercariModel.getPrice())
                        .build()
        );
        mercariService.save(mercariModel);
    }
}
