package com.ou.th.crawler.mercari.pipeline;

import com.ou.th.crawler.mercari.anatation.NeedOlder;
import com.ou.th.crawler.mercari.model.MercariModel;
import com.ou.th.crawler.mercari.service.MercariService;
import com.ou.th.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.lang.reflect.Field;
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
        MercariModel newer = (MercariModel) resultItems.getAll().values().toArray()[0];
        MercariModel older = mercariService.getByPid(newer.getPid());

        // 因为在列表页就判断了是不是需要保存，所以在这里不需要进行判断了
        MercariModel.PriceTime t = new MercariModel.PriceTime();
        t.setDateTime(newer.getDateTime());
        t.setCurrentPrice(newer.getCurrentPrice());
        newer.getPriceTimes().add(t);

        // 第一次添加
        if (older.getPid() == null) {
            // 只上传第一张图片
            List<String> pictures = newer.getPictures();
            pictures.set(0, fastdfsUtil.uploadFromUrl(pictures.get(0)));
        }else {
            needOlder(newer, older);
            newer.setChanged(true);
            newer.setDisliked(false);
        }
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