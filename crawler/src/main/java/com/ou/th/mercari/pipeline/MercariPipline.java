package com.ou.th.mercari.pipeline;

import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

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
        MercarModel mercarModel = (MercarModel) resultItems.getAll().values().toArray()[0];

        mercarModelService.save(mercarModel);
    }
}
