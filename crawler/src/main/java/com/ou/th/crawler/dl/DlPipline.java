package com.ou.th.crawler.dl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.ou.th.util.FastdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Optional;

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
        Optional.<DlModel>ofNullable(resultItems.get("obj")).ifPresent(e -> ThreadUtil.execute(() -> dlService.save(handle(e))));
    }

    public DlModel handle(DlModel dlModel) {
        String lastDate = dlModel.getLastDate();
        if (StrUtil.isNotEmpty(lastDate)) {
            dlModel.setLastDate(StrUtil.subBefore(lastDate, "\n", false));
        }
        return dlModel;
    }
}
