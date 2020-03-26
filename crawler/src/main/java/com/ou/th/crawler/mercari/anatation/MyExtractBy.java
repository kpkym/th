package com.ou.th.crawler.mercari.anatation;

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
    String value();

    enum Type {XPath, Regex, Css, JsonPath}

    Type type() default Type.XPath;

    boolean needAll() default false;
}
