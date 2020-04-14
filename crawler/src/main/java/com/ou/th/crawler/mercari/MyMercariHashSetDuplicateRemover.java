package com.ou.th.crawler.mercari;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.StrUtil;
import com.ou.th.crawler.common.CommonUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.util.Set;

/**
 * @author kpkym
 * Date: 2020-04-01 08:06
 */
public class MyMercariHashSetDuplicateRemover extends HashSetDuplicateRemover {
    private Set<String> urls = new ConcurrentHashSet<>();

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(getKey(request));
    }

    protected String getKey(Request request) {
        String url = request.getUrl();
        if (url.contains("/jp/search")) {
            String keyword = CommonUtil.getParamUniqueValue(url, "keyword");
            String page = StrUtil.emptyToDefault(CommonUtil.getParamUniqueValue(url, "page"), "1");

            return keyword + ":" + page;
        } else if (url.contains("/jp/items")) {
            return MercariUtil.getIdFrom(url);
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
