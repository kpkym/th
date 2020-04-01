package com.ou.th.crawler.common;

import com.ou.th.crawler.common.anatation.NeedUpdate;

import java.lang.reflect.Field;

/**
 * @author kpkym
 * Date: 2020-04-01 17:01
 */

public class CommonPipline {
    public static  <T> void needUpdate(T older, T newer) {
        for (Field olderField : older.getClass().getDeclaredFields()) {
            NeedUpdate[] annotationsByType = olderField.getAnnotationsByType(NeedUpdate.class);
            if (annotationsByType.length < 1) {
                continue;
            }
            try {
                Field newerField = newer.getClass().getDeclaredField(olderField.getName());
                olderField.setAccessible(true);
                newerField.setAccessible(true);

                olderField.set(older, newerField.get(newer));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

    }
}
