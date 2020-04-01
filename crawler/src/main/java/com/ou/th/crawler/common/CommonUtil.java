package com.ou.th.crawler.common;

import cn.hutool.core.util.URLUtil;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import us.codecraft.webmagic.selector.Html;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-31 17:47
 */
public class CommonUtil {
    public static String getParamValue(String url, String param) {
        String normalize = URLUtil.normalize(url);
        normalize = url.substring(url.indexOf("?") + 1);
        String[] split = normalize.split("[&=]");
        List<String> strings = new ArrayList<>(Arrays.asList(split));

        int p = strings.lastIndexOf(param);
        return p >= 0 ? strings.get(p + 1) : null;
    }


    public static BigDecimal StrToBigdecimal(String s) {
        return new BigDecimal(s.replaceAll("[^0-9]", ""));
    }

    public static<T> T handleAnotation(String item, T t, boolean isList) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            MyExtractBy myExtractBy = declaredField.getAnnotation(MyExtractBy.class);
            if (myExtractBy == null) {
                continue;
            }
            String xpath = isList ? myExtractBy.list() : myExtractBy.detail();
            Object value = new Html(item).xpath(xpath).get();
            if (declaredField.getType().isAssignableFrom(BigDecimal.class)) {
                value = "品切れ".equals(value) ? Integer.MAX_VALUE + "" : value;
                value = CommonUtil.StrToBigdecimal((String) value);
            }
            declaredField.setAccessible(true);
            try {
                declaredField.set(t, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }
}
