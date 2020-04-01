package com.ou.th.crawler.mercari;

import cn.hutool.core.collection.ConcurrentHashSet;
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
            String keyword = CommonUtil.getParamValue(url, "keyword");
            String page = CommonUtil.getParamValue(url, "page");
            page = page == null ? "1" : page;

            return keyword + ":" + page;
        } else if (url.contains("/jp/items")) {
            return MercariUtil.getId(url);
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
