package com.ou.th.crawler.common;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HtmlUtil;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import us.codecraft.webmagic.selector.Html;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    public static BigDecimal StrToBigdecimal(Object s) {
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(s.toString().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            bigDecimal = new BigDecimal(Integer.MAX_VALUE);
        }

        return bigDecimal;
    }

    public static<T> T handleAnotation(String htmlStr, T t, boolean isPageList) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        String rule = "";
        List<Object> values = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            MyExtractBy myExtractBy = declaredField.getAnnotation(MyExtractBy.class);
            if (myExtractBy == null) {
                continue;
            }
            rule = isPageList ? myExtractBy.pageList() : myExtractBy.itemDetail();

            // 为空时直接退出
            if (StrUtil.isEmpty(rule)) {
                return t;
            }

            if (MyExtractBy.Type.XPath.equals(myExtractBy.type())) {
                values.addAll(new Html(htmlStr).xpath(rule).all());
            }else if (MyExtractBy.Type.Regex.equals(myExtractBy.type())) {
                String v = "";
                if (!isPageList && StrUtil.isNotEmpty(myExtractBy.regexPreHandle())) {
                    v = new Html(htmlStr).xpath(myExtractBy.regexPreHandle()).get();
                }

                values.addAll(new Html(v).regex(rule).all().stream()
                        .map(e -> HtmlUtil.unescape(e).replaceAll("(?<=<)\\s+|\\s+(?=>)", "").trim())
                        .collect(Collectors.toList()));
            }

            if (BigDecimal.class.equals(myExtractBy.targetClazz())) {
                values = values.stream().map(CommonUtil::StrToBigdecimal).collect(Collectors.toList());
            }

            try {
                if (declaredField.getType().isAssignableFrom(Collection.class)) {
                    ReflectUtil.setFieldValue(t, declaredField, values);
                } else {
                    ReflectUtil.setFieldValue(t, declaredField, values.stream().findFirst().orElse(null));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }
}
