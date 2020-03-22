package com.ou.th.mercari.pipeline;

import com.ou.th.mercari.anatation.NeedOlder;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * @author kpkym
 * Date: 2020-03-16 21:18
 */
@Component
public class MercariPipline implements Pipeline {
    @Autowired
    MercarService mercarService;

    @Value("${proxy.host}")
    String proxyHost;

    @Value("${proxy.port}")
    Integer proxyPort;

    @Override
    public void process(ResultItems resultItems, Task task) {
        MercarModel newer = (MercarModel) resultItems.getAll().values().toArray()[0];
        MercarModel older = mercarService.getByPid(newer.getPid());

        // 因为在列表页就判断了是不是需要保存，所以在这里不需要进行判断了
        MercarModel.PriceTime t = new MercarModel.PriceTime();
        t.setDateTime(newer.getDateTime());
        t.setCurrentPrice(newer.getCurrentPrice());

        // 第一次添加
        if (older.getPid() == null) {
            newer.getPictures().replaceAll(this::saveImg);
        } else {
            needOlder(newer, older);
        }
        newer.getPriceTimes().add(t);
        newer.setChanged(true);
        mercarService.save(newer);
    }

    private void needOlder(MercarModel newer, MercarModel older) {
        for (Field newerField : newer.getClass().getDeclaredFields()) {
            NeedOlder[] annotationsByType = newerField.getAnnotationsByType(NeedOlder.class);
            if (annotationsByType.length < 1) {
                continue;
            }
            try {
                Field olderField = older.getClass().getDeclaredField(newerField.getName());
                olderField.setAccessible(true);
                newerField.setAccessible(true);

                newerField.set(newer, olderField.get(older));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

    }

    // TODO url 替换 fastdfs地址
    public String saveImg(String url) {
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient client = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();

        HttpGet request = new HttpGet(url);

        HttpResponse response = null;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();

            InputStream is = null;
            is = entity.getContent();

            String filePath = "1.png";
            FileOutputStream fos = new FileOutputStream(new File(filePath));

            int inByte;
            while ((inByte = is.read()) != -1) {
                fos.write(inByte);
            }
            is.close();
            fos.close();
        client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
