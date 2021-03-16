package com.ou.th.crawler.common.anatation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kpkym
 * Date: 2020-03-19 00:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyExtractBy {
    /**
     * 页面元素可以直接获取的表达式
     */
    String pageList() default "";

    /**
     * 进入页面详情获取的表达式
     */
    String itemDetail() default "";

    /**
     * 当 {@link #type()}== {@link Type#Regex}时。对{@code HTML}字符串进行预处理
     */
    String regexPreHandleXpath() default "";

    /**
     * 对{@code HTML}字符串处理时表示式的类型
     */
    Type type() default Type.XPath;

    /**
     * 字段类型，一般是为List时，泛型强转
     */
    Class targetClazz() default String.class;

    // collection的分隔符
    String separatorRegex() default "";

    enum Type {XPath, Regex, Css, JsonPath}
}

