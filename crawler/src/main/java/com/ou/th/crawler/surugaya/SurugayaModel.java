package com.ou.th.crawler.surugaya;

import com.ou.th.crawler.common.anatation.MyExtractBy;
import com.ou.th.crawler.common.anatation.NeedOlder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-31 17:28
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@RedisHash("surugayaModel")
public class SurugayaModel {
    @Id
    private String pid;

    // 感兴趣 看过之后会继续留在界面
    @NeedOlder
    private boolean isLike = false;

    // 不感兴趣 看过之后如果没有改变则继续不显示
    private boolean isDel = false;

    private boolean isChange = false;

    @MyExtractBy("//p[@class='title']/a/text()")
    private String title;

    @MyExtractBy("//div[@class='item']//img/@data-src")
    private String picture;

    private String pictureOriginal;

    @MyExtractBy("//p[@class='price']/text()|//p[@class='price_teika']//strong/text()")
    private BigDecimal price;

    @MyExtractBy("//a/@href")
    private String url;

    private Long dateTime;

    @NeedOlder
    private List<PriceTime> priceTimes = new ArrayList<>();

    @Data
    public static class PriceTime{
        private Long dateTime;
        private BigDecimal price;
    }
}
