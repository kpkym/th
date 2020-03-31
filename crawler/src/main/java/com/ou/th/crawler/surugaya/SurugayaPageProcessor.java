package com.ou.th.crawler.surugaya;

import com.ou.th.crawler.common.CommonUtil;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-31 16:53
 */
@Slf4j
@Component
public class SurugayaPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(2300).setTimeOut(100000);

    @Override
    public void process(Page page) {
        // 下一页
        // String next = page.getHtml().xpath("//li[contains(@class, 'pager-next')]//li[1]/a/@href").get();
        // if (StrUtil.isNotEmpty(next)) {
        //     page.addTargetRequest(next);
        // }

        String url = page.getRequest().getUrl();

        // 当前是搜索页
        if (url.contains("/search")) {
            log.info("当前URL为：" + url);
            page.setSkip(true);
            // 找到所有的链接跟价格
            List<String> items = page.getHtml().xpath("//div[@class='item']").all();

            List<SurugayaModel> surugayaModels = new ArrayList<>();

            for (String item : items) {
                SurugayaModel e = this.handleAnotation(item);
                e.setDateTime(new Date().getTime());
                surugayaModels.add(e);
            }
            page.putField("data", surugayaModels);
            // 最后筛选后的地址添加回调度器
            // page.addTargetRequests(hrefs);
        }
        // else if (url.contains("/jp/items")) {
        // } else {
        //     page.setSkip(true);
        // }
    }

    @Override
    public Site getSite() {
        return site;
    }

    private SurugayaModel handleAnotation(String item) {
        SurugayaModel surugayaModel = new SurugayaModel();
        Field[] declaredFields = SurugayaModel.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            MyExtractBy myExtractBy = declaredField.getAnnotation(MyExtractBy.class);
            if (myExtractBy == null) {
                continue;
            }
            String xpath = myExtractBy.value();
            Object value = new Html(item).xpath(xpath).get();
            if (declaredField.getType().isAssignableFrom(BigDecimal.class)) {
                value = "品切れ".equals(value) ? Integer.MAX_VALUE + "" : value;
                value = CommonUtil.StrToBigdecimal((String) value);
            }
            declaredField.setAccessible(true);
            try {
                declaredField.set(surugayaModel, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return surugayaModel;
    }
}