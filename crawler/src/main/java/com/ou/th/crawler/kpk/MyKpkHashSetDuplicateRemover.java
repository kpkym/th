package com.ou.th.crawler.kpk;

import cn.hutool.core.collection.ConcurrentHashSet;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.util.Set;

/**
 * @author kpkym
 * Date: 2020-10-31 08:06
 */
public class MyKpkHashSetDuplicateRemover extends HashSetDuplicateRemover {
    private Set<String> urls = new ConcurrentHashSet<>();

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(getKey(request));
    }

    protected String getKey(Request request) {
        String url = request.getUrl();
        int i = url.lastIndexOf("/");
        return url.substring(i);
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
