package com.ou.th.mercari.crawler;

import com.ou.th.mercari.anatation.MyExtractBy;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.util.MercariUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author kpkym
 * Date: 2020-03-16 20:51
 */
@Slf4j
@Component
public class MercariPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(10000);

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        // 当前是搜索页
        if (url.contains("/jp/search")) {
            page.setSkip(true);
            page.addTargetRequests(page.getHtml().xpath("//section//section/a/@href").all());
        } else if (url.contains("/jp/items")) {
            MercarModel mercarModel = extractDataAnotation(page);
            page.putField(mercarModel.getTitle(), mercarModel);
        } else {
            page.setSkip(true);
        }


    }

    @Override
    public Site getSite() {
        return site;
    }


    private MercarModel extractDataAnotation(Page page) {
        MercarModel mercarModel = new MercarModel();
        for (Field declaredField : mercarModel.getClass().getDeclaredFields()) {
            handleAnotation(page, mercarModel, declaredField);
        }
        handleNonAnotation(mercarModel, page);

        return mercarModel;
    }

    private void handleNonAnotation(MercarModel mercarModel, Page page) {
        mercarModel.setUrl(page.getRequest().getUrl());
        mercarModel.setDateTime(System.currentTimeMillis());
        mercarModel.setPid(MercariUtil.getPid(mercarModel));
    }


    private void handleAnotation(Page page, MercarModel mercarModel, Field field) {
        MyExtractBy[] annotationsByType = field.getAnnotationsByType(MyExtractBy.class);
        if (annotationsByType.length < 1) {
            return;
        }
        MyExtractBy myExtractBy = annotationsByType[0];
        String xpath = myExtractBy.value();

        Object value = null;
        if (myExtractBy.needAll()) {
            value = page.getHtml().xpath(xpath).all();
        } else {
            value = page.getHtml().xpath(xpath).get();
        }
        if (field.getName().contains("rice")) {
            value = new BigDecimal(((String) value).replaceAll("[^0-9]", ""));
        }

        try {
            field.setAccessible(true);
            field.set(mercarModel, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}