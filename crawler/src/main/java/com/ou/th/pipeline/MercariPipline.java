package com.ou.th.pipeline;

import javafx.util.Pair;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author kpkym
 * Date: 2020-03-16 21:18
 */
public class MercariPipline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        // for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
        //     System.out.println(entry.getKey() + ":\t" + entry.getValue());
        // }
        List<String> titles = (List<String>) resultItems.getAll().get("titles");
        List<String> prices = (List<String>) resultItems.getAll().get("prices");

        List<Pair<String, String>> collects = IntStream.range(0, Math.min(titles.size(), prices.size()))
                .mapToObj(i -> new Pair<>(titles.get(i), prices.get(i)))
                .collect(Collectors.toList());

        for (Pair<String, String> pair : collects) {
            System.out.println(pair.getKey() + "====" + pair.getValue());
        }

    }
}
