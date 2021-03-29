package com.ou.th.crawler.dl;

import cn.hutool.core.util.URLUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-19 22:25
 */
public class DlUtil {
    public static String getIdFrom(String url) {
        String normalize = URLUtil.normalize(url);

        String[] split = normalize.split("/+");
        List<String> strings = new ArrayList<>(Arrays.asList(split));

        return strings.get(strings.indexOf("items") + 1);
    }
}
