package com.ou.th.mercari.crawler;

import cn.hutool.core.util.StrUtil;
import com.ou.th.mercari.anatation.MyExtractBy;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarService;
import com.ou.th.mercari.util.MercariUtil;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author kpkym
 * Date: 2020-03-16 20:51
 */
@Slf4j
@Component
public class MercariPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(10000);

    @Autowired
    MercarService mercarService;

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        log.info("当前URL为：" + url);
        // 当前是搜索页
        if (url.contains("/jp/search")) {
            page.setSkip(true);
            // page.addTargetRequests(page.getHtml().xpath("//section//section/a/@href").all());

            // 找到所有的链接跟价格
            List<String> allHref = page.getHtml().xpath("//div[contains(@class, 'items-box-content')]//a/@href").all();
            List<String> allprice = page.getHtml().xpath("//div[contains(@class, 'items-box-content')]//div[contains(@class, 'items-box-price')]/text()").all();

            List<Pair<String, String>> hrefprices = IntStream.range(0, Math.min(allHref.size(), allprice.size()))
                    .mapToObj(i -> new Pair<>(allHref.get(i), allprice.get(i)))
                    .collect(Collectors.toList());

            List<String> hrefs = hrefprices.stream().filter(e -> {
                String href = e.getKey();
                BigDecimal price = MercariUtil.StrToBigdecimal(e.getValue());
                MercarModel older = mercarService.getByPid(MercariUtil.getPid(href));

                // 如果在上一次加入了并且价格还没有改变就不进入这个页面了
                return !price.equals(older.getCurrentPrice());
            }).map(Pair::getKey).collect(Collectors.toList());

            // 最后筛选后的地址添加回调度器
            page.addTargetRequests(hrefs);
        } else if (url.contains("/jp/items")) {
            MercarModel mercarModel = extractDataAnotation(page);
            page.putField(mercarModel.getTitle(), mercarModel);
        } else {
            page.setSkip(true);
        }

        String next = page.getHtml().xpath("//li[contains(@class, 'pager-next')]//li[1]/a/@href").get();
        if (StrUtil.isNotEmpty(next)) {
            page.addTargetRequest(next);
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
            value = MercariUtil.StrToBigdecimal((String) value);
        }

        try {
            field.setAccessible(true);
            field.set(mercarModel, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}