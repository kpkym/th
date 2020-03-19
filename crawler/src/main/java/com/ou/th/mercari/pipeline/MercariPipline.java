package com.ou.th.mercari.pipeline;

import com.ou.th.mercari.anatation.NeedOlder;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-16 21:18
 */
@Component
public class MercariPipline implements Pipeline {
    @Autowired
    MercarModelService mercarModelService;

    @Override
    public synchronized void process(ResultItems resultItems, Task task) {
        MercarModel newer = (MercarModel) resultItems.getAll().values().toArray()[0];
        MercarModel older = mercarModelService.getByPid(newer.getPid());

        List<MercarModel.PriceTime> priceTimes = older.getPriceTimes();
        if (priceTimes.isEmpty()) {
            MercarModel.PriceTime t = new MercarModel.PriceTime();
            t.setDateTime(newer.getDateTime());
            t.setCurrentPrice(newer.getCurrentPrice());

            older.getPriceTimes().add(t);
        } else {
            BigDecimal oldCurrentPrice = priceTimes.remove(priceTimes.size() - 1).getCurrentPrice();
            if (!newer.getCurrentPrice().equals(oldCurrentPrice)) {
                MercarModel.PriceTime t = new MercarModel.PriceTime();
                t.setDateTime(newer.getDateTime());
                t.setCurrentPrice(newer.getCurrentPrice());

                older.getPriceTimes().add(t);
                needOlder(newer, older);
                newer.setChanged(true);
            } else {
                return;
            }
        }
        mercarModelService.save(newer);


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