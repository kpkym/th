package com.ou.th.mercari.pipeline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * @author kpkym
 * Date: 2020-03-16 21:18
 */
public class MercariPipline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> stringObjectEntry : resultItems.getAll().entrySet()) {
            System.out.println(stringObjectEntry);
        }
    }
}
