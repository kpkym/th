package com.ou.th.mercari.pipeline;

import com.ou.th.mercari.anatation.NeedOlder;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarService;
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
    MercarService mercarService;

    @Autowired
    FastdfsUtil fastdfsUtil;


    @Override
    public void process(ResultItems resultItems, Task task) {
        MercarModel newer = (MercarModel) resultItems.getAll().values().toArray()[0];
        MercarModel older = mercarService.getByPid(newer.getPid());

        // 因为在列表页就判断了是不是需要保存，所以在这里不需要进行判断了
        MercarModel.PriceTime t = new MercarModel.PriceTime();
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
        mercarService.save(newer);
    }

    private void needOlder(MercarModel newer, MercarModel older) {
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
