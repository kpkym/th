package com.ou.th.util;

import java.io.File;

/**
 * @author kpkym
 * Date: 2020-03-20 03:00
 */
public class ConfigUtil {
    public static File getConfigFile() {
        return new File(Thread.currentThread().getContextClassLoader().getResource("initURL.kpk").getFile());
    }
}
