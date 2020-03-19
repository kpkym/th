package com.ou.th.mercari.util;

import cn.hutool.core.util.URLUtil;
import com.ou.th.mercari.model.MercarModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-19 22:25
 */
public class MercariUtil {
    public static String getPid(MercarModel mercarModel) {
        String url = mercarModel.getUrl();
        String normalize = URLUtil.normalize(url);

        String[] split = normalize.split("/+");
        List<String> strings = new ArrayList<>(Arrays.asList(split));

        return strings.get(strings.indexOf("items") + 1);
    }
}
