package com.ou.th.crawler.common;

import com.ou.th.crawler.common.anatation.MyExtractBy;
import us.codecraft.webmagic.selector.Html;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author kpkym
 * Date: 2020-03-31 17:47
 */
public class CommonUtil {
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
