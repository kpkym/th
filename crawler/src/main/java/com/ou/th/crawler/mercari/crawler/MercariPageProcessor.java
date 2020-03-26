package com.ou.th.crawler.mercari.crawler;

import cn.hutool.core.util.StrUtil;
import com.ou.th.crawler.mercari.anatation.MyExtractBy;
import com.ou.th.crawler.mercari.model.MercariModel;
import com.ou.th.crawler.mercari.service.MercariService;
import com.ou.th.crawler.mercari.util.MercariUtil;
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

    private Site site = Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(100000);

    @Autowired
    MercariService mercariService;

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        // 当前是搜索页
        if (url.contains("/jp/search")) {
            log.info("当前URL为：" + url);
            page.setSkip(true);
            // 找到所有的链接跟价格
            List<String> allHref = page.getHtml().xpath("//div[contains(@class, 'items-box-content')]//a/@href").all();
            List<String> allprice = page.getHtml().xpath("//div[contains(@class, 'items-box-content')]//div[contains(@class, 'items-box-price')]/text()").all();

            List<Pair<String, String>> hrefprices = IntStream.range(0, Math.min(allHref.size(), allprice.size()))
                    .mapToObj(i -> new Pair<>(allHref.get(i), allprice.get(i)))
                    .collect(Collectors.toList());

            List<String> hrefs = hrefprices.stream().filter(e -> {
                String href = e.getKey();
                BigDecimal price = MercariUtil.StrToBigdecimal(e.getValue());
                MercariModel older = mercariService.getByPid(MercariUtil.getPid(href));

                // 如果在上一次加入了并且价格还没有改变就不进入这个页面了
                return !price.equals(older.getCurrentPrice());
            }).map(Pair::getKey).collect(Collectors.toList());

            // 最后筛选后的地址添加回调度器
            page.addTargetRequests(hrefs);
        } else if (url.contains("/jp/items")) {
            MercariModel mercariModel = extractDataAnotation(page);
            page.putField(mercariModel.getTitle(), mercariModel);
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


    private MercariModel extractDataAnotation(Page page) {
        MercariModel mercariModel = new MercariModel();
        for (Field declaredField : mercariModel.getClass().getDeclaredFields()) {
            handleAnotation(page, mercariModel, declaredField);
        }
        handleNonAnotation(mercariModel, page);

        return mercariModel;
    }

    private void handleNonAnotation(MercariModel mercariModel, Page page) {
        mercariModel.setUrl(page.getRequest().getUrl());
        mercariModel.setDateTime(System.currentTimeMillis());
        mercariModel.setPid(MercariUtil.getPid(mercariModel));
    }


    private void handleAnotation(Page page, MercariModel mercariModel, Field field) {
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
            field.set(mercariModel, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}