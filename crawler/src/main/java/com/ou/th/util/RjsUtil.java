package com.ou.th.util;

import cn.hutool.core.collection.ConcurrentHashSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RjsUtil {
    private final static Set<String> rjs = new ConcurrentHashSet<>();

    public static void addAll(Collection<String> data) {
        RjsUtil.rjs.addAll(data);
    }

    public static Set<String> getAll() {
        return RjsUtil.rjs;
    }
}
