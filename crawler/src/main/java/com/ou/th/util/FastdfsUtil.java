package com.ou.th.util;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author kpkym
 * Date: 2020-03-22 02:51
 */
@Slf4j
@Component
public class FastdfsUtil {
    @Autowired
    FastFileStorageClient storageClient;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${proxy.host}")
    String proxyHost;

    @Value("${proxy.port}")
    Integer proxyPort;

    public String uploadFromUrl(String url) {
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        HttpGet request = new HttpGet(url);
        StorePath path = null;
        try (CloseableHttpClient client = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();
             InputStream is =  client.execute(request).getEntity().getContent()
        ){
            String fileExtName = FilenameUtils.getExtension(url);
            if (fileExtName.contains("?")) {
                fileExtName = fileExtName.substring(0, fileExtName.indexOf('?'));
            }

            byte[] bytes = IOUtils.toByteArray(is);
            FastImageFile fastImageFile = new FastImageFile.Builder()
                    .withFile(new ByteArrayInputStream(bytes), bytes.length, fileExtName)
                    .build();
            path = storageClient.uploadFile(fastImageFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下载图片失败，退出程序");
            System.exit(SpringApplication.exit(applicationContext, () -> 1));
        }
        return path == null ? null : path.getPath();
    }
}