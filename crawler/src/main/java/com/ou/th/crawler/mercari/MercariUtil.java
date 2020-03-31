package com.ou.th.crawler.mercari;

import cn.hutool.core.util.URLUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-19 22:25
 */
public class MercariUtil {
    public static String getId(MercariModel mercariModel) {
        String url = mercariModel.getUrl();
        String normalize = URLUtil.normalize(url);

        String[] split = normalize.split("/+");
        List<String> strings = new ArrayList<>(Arrays.asList(split));

        return strings.get(strings.indexOf("items") + 1);
    }

    public static String getId(String s) {
        MercariModel mercariModel = new MercariModel();
        mercariModel.setUrl(s);
        return getId(mercariModel);
    }

}
