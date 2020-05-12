package com.ou.th.util;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLHandshakeException;
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

    @Autowired
    HttpUtil httpUtil;

    public String uploadFromUrl(String url) {
        StorePath path = null;
        try (CloseableHttpClient client = httpUtil.getClient();
             InputStream is = httpUtil.getInputStream(url, client)
        ) {
            String fileExtName = FilenameUtils.getExtension(url);
            if (fileExtName.contains("?")) {
                fileExtName = fileExtName.substring(0, fileExtName.indexOf('?'));
            }

            byte[] bytes = IOUtils.toByteArray(is);
            FastImageFile fastImageFile = new FastImageFile.Builder()
                    .withFile(new ByteArrayInputStream(bytes), bytes.length, fileExtName)
                    .build();
            path = storageClient.uploadFile(fastImageFile);
        } catch (SSLHandshakeException e) {
            log.error("SSLHandshakeException 错误, URL=" + url);
            e.printStackTrace();
            path = new StorePath("", url);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下载图片失败，退出程序");
            path = new StorePath("", url);
            // System.exit(SpringApplication.exit(applicationContext, () -> 1));
        }
        return path == null ? null : path.getPath();
    }


}
