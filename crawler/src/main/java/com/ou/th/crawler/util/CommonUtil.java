package com.ou.th.crawler.util;

import java.math.BigDecimal;

/**
 * @author kpkym
 * Date: 2020-03-31 17:47
 */
public class CommonUtil {
    public static BigDecimal StrToBigdecimal(String s) {
        return new BigDecimal(s.replaceAll("[^0-9]", ""));
    }
}
