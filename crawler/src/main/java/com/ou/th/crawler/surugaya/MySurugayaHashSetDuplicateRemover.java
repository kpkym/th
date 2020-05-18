package com.ou.th.crawler.surugaya;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.StrUtil;
import com.ou.th.crawler.common.CommonUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import java.util.Set;

/**
 * @author kpkym
 * Date: 2020-04-01 15:14
 */
public class MySurugayaHashSetDuplicateRemover implements DuplicateRemover {
    private Set<String> urls = new ConcurrentHashSet<>();

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(getKey(request));
    }

    protected String getKey(Request request) {
        String url = request.getUrl();
        if (url.contains("/search")) {
            String keyword = CommonUtil.getParamUniqueValue(url, "restrict[]")
                    + CommonUtil.getParamUniqueValue(url, "search_word")
                    + CommonUtil.getParamUniqueValue(url, "category");

            String page = StrUtil.emptyToDefault(CommonUtil.getParamUniqueValue(url, "page"), "1");

            return keyword + ":" + page;
        } else if (url.contains("/product/detail")) {
            return SurugayaUtil.getIdFrom(url);
        }
        return "";
    }
    @Override
    public void resetDuplicateCheck(Task task) {
        urls.clear();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }
}
