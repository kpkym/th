package com.ou.th.crawler.mercari;

import com.ou.th.crawler.common.anatation.NeedOlder;
import com.ou.th.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.lang.reflect.Field;
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
            // 只上传第一张图片
            newer.setId(id);
            newer.setPicture(fastdfsUtil.uploadFromUrl(newer.getPicturesOriginal()));
        } else if (!older.getPrice().equals(newer.getPrice())) {
            needOlder(newer, older);
            newer.setIsChange(true);
            newer.setIsDel(false);
        } else {
            // 如果不是第一插入或价格不变则不保存
            return;
        }
        // 一定要放在复制老数据后面
        newer.getPriceTimes().add(
                MercariModel.PriceTime.builder()
                        .dateTime(new Date().getTime())
                        .price(newer.getPrice())
                        .build()
        );
        mercariService.save(newer);
    }

    private void needOlder(MercariModel newer, MercariModel older) {
        for (Field newerField : newer.getClass().getDeclaredFields()) {
            NeedOlder[] annotationsByType = newerField.getAnnotationsByType(NeedOlder.class);
            if (annotationsByType.length < 1) {
                continue;
            }
            try {
                Field olderField = older.getClass().getDeclaredField(newerField.getName());
                olderField.setAccessible(true);
                newerField.setAccessible(true);

                newerField.set(newer, olderField.get(older));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

    }
}
