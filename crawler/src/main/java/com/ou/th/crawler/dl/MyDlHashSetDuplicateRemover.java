package com.ou.th.crawler.dl;

import cn.hutool.core.collection.ConcurrentHashSet;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.util.Set;

/**
 * @author kpkym
 * Date: 2020-04-01 08:06
 */
public class MyDlHashSetDuplicateRemover extends HashSetDuplicateRemover {
    private Set<String> urls = new ConcurrentHashSet<>();

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(getKey(request));
    }

    protected String getKey(Request request) {
        return request.getUrl();
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
