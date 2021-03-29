package com.ou.th.crawler.dl;

import cn.hutool.core.util.StrUtil;
import com.ou.th.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-16 21:18
 */
@Component
public class DlPipline implements Pipeline {
    @Autowired
    DlService dlService;

    @Autowired
    FastdfsUtil fastdfsUtil;


    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("arr") != null) {
            List<DlModel> mercariModels = resultItems.get("arr");
            for (DlModel dlModel : mercariModels) {
                dlService.save(handle(dlModel));
            }
        } else {
            DlModel obj = resultItems.get("obj");
            if (obj != null)
                dlService.save(handle(obj));
        }
    }

    public DlModel handle(DlModel dlModel) {
        String lastDate = dlModel.getLastDate();
        if (StrUtil.isNotEmpty(lastDate)) {
            dlModel.setLastDate(StrUtil.subBefore(lastDate, "\n", false));
        }
        return dlModel;
    }
}
