package com.ou.th.crawler.kpk;

import com.ou.th.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-10-31 21:18
 */
@Component
public class KpkPipline implements Pipeline {
    @Autowired
    KpkService kpkservice;

    @Autowired
    FastdfsUtil fastdfsUtil;


    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("arr") != null) {
            List<KpkModel> mercariModels = resultItems.get("arr");
            for (KpkModel mercariModel : mercariModels) {
                kpkservice.save(mercariModel);
            }
        } else {
            KpkModel obj = resultItems.get("obj");
            if (obj != null)
            kpkservice.save(obj);
        }
    }
}
