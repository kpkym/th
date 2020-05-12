package com.ou.th.util;

import com.ou.th.config.KpkConfig;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kpkym
 * Date: 2020-05-12 12:29
 */
@Component
public class HttpUtil {
    @Autowired
    KpkConfig kpkConfig;

    public CloseableHttpClient getClient() {
        HttpHost proxy = new HttpHost(kpkConfig.getProxy().getHost(), kpkConfig.getProxy().getPort(), "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        return HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();
    }

    public InputStream getInputStream(String url, CloseableHttpClient client) throws IOException {
        HttpGet request = new HttpGet(url);
        return client.execute(request).getEntity().getContent();
    }
}
