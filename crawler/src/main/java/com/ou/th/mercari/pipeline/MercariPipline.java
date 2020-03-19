package com.ou.th.mercari.pipeline;

import com.ou.th.mercari.anatation.NeedOlder;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.lang.reflect.Field;

/**
 * @author kpkym
 * Date: 2020-03-16 21:18
 */
@Component
public class MercariPipline implements Pipeline {
    @Autowired
    MercarService mercarService;

    @Override
    public synchronized void process(ResultItems resultItems, Task task) {
        MercarModel newer = (MercarModel) resultItems.getAll().values().toArray()[0];
        MercarModel older = mercarService.getByPid(newer.getPid());

        MercarModel.PriceTime t = new MercarModel.PriceTime();
        t.setDateTime(newer.getDateTime());
        t.setCurrentPrice(newer.getCurrentPrice());

        // 因为在列表页就判断了是不是需要保存，所以在这里不需要进行判断了
        needOlder(newer, older);
        newer.getPriceTimes().add(t);
        newer.setChanged(true);
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
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

    }
}
