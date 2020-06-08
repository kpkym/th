package com.ou.th.crawler.surugaya;

import cn.hutool.core.util.URLUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-04-01 15:20
 */
public class SurugayaUtil {
    public static String getIdFrom(String url) {
        String normalize = URLUtil.normalize(url);

        String[] split = normalize.split("[/?]+");
        List<String> strings = new ArrayList<>(Arrays.asList(split));

        return strings.get(strings.indexOf("detail") + 1);
    }
}
