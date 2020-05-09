package com.ou.th.crawler.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HtmlUtil;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import us.codecraft.webmagic.selector.Html;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kpkym
 * Date: 2020-03-31 17:47
 */
public class CommonUtil {
    public static String getParamUniqueValue(String url, String param) {
        String normalize = URLUtil.normalize(url);
        normalize = url.substring(url.indexOf("?") + 1);
        String[] split = normalize.split("[&]");
        List<String> strings = new ArrayList<>(Arrays.asList(split));

        return strings.stream().filter(e -> e.startsWith(param))
                .sorted(String::compareTo)
                .map(value -> value.substring(param.length() + 1))
                .collect(Collectors.joining(","));
    }


    /**
     * 字符串转bigdecimal, 无效字符串返回 new BigDecimal(Integer.MAX_VALUE)
     * @param s
     * @return
     */
    public static BigDecimal StrToBigdecimal(String s) {
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(s.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            bigDecimal = new BigDecimal(Integer.MAX_VALUE);
        }

        return bigDecimal;
    }

    public static<T> T handleAnotation(String item, T t, boolean isList) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        String rule = "";
        Object value = "";
        for (Field declaredField : declaredFields) {
            MyExtractBy myExtractBy = declaredField.getAnnotation(MyExtractBy.class);
            if (myExtractBy == null) {
                continue;
            }
            rule = isList ? myExtractBy.list() : myExtractBy.detail();
            if (MyExtractBy.Type.XPath.equals(myExtractBy.type())) {
                value = new Html(item).xpath(rule).get();
            }else if (MyExtractBy.Type.Regex.equals(myExtractBy.type())) {
                if (!isList && StrUtil.isNotEmpty(myExtractBy.xpathDetail())) {
                    value = new Html(item).xpath(myExtractBy.xpathDetail()).get();
                }

                value = ((String)value).length() > 2 ? value : HtmlUtil.unescape(new Html(item).regex(rule).get())
                        .replaceAll("(?<=<)\\s+|\\s+(?=>)", "").trim();
            }
            if (declaredField.getType().isAssignableFrom(BigDecimal.class)) {
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
