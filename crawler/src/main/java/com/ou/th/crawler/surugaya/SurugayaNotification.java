package com.ou.th.crawler.surugaya;

import cn.hutool.core.util.URLUtil;
import com.ou.th.config.KpkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SurugayaNotification {
    @Autowired
    KpkConfig kpkConfig;

    public boolean neetDetail(String url) {
        String decode = URLUtil.decode(url);

        return kpkConfig.getSurugayaKeyword().stream().anyMatch(decode::contains);
    }

    public void surugayaInterceptor(List<SurugayaModel> notDetails) {

    }
}
