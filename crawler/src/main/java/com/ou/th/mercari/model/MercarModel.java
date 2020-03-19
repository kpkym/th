package com.ou.th.mercari.model;

import com.ou.th.mercari.anatation.MyExtractBy;
import com.ou.th.mercari.anatation.NeedOlder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-18 23:30
 */
@Data
@RedisHash("mercarModel")
public class MercarModel {
    @Id
    private String pid;

    @NeedOlder
    private boolean liked = false;

    private boolean changed = false;

    @MyExtractBy("//h1[@class='item-name']/text()")
    private String title;

    @MyExtractBy("//p[@class='item-wording']/text()")
    private String description;

    @MyExtractBy("//tr[1]/td/a/text()")
    private String seller;

    @MyExtractBy(value = "//td/a/div/text()", needAll = true)
    private List<String> type;

    @MyExtractBy(value = "//tr[4]/td/text()")
    private String status;

    @MyExtractBy(value = "//tr[5]/td/text()")
    private String whoseFreight;

    @MyExtractBy(value = "//tr[6]/td/text()")
    private String deliveryMethod;

    @MyExtractBy(value = "//tr[7]/td/text()")
    private String deliveryArea;

    @MyExtractBy(value = "//tr[8]/td/text()")
    private String deliveryTime;

    @MyExtractBy(value = "//div[@class='owl-carousel']//img/@data-src", needAll = true)
    private List<String> pictures;

    @MyExtractBy(value = "//div[@class='item-price-box text-center']/span[@class='item-price bold']/text()")
    private BigDecimal currentPrice;

    private String url;

    private Long dateTime;

    @NeedOlder
    private List<PriceTime> priceTimes = new ArrayList<>();

    @Data
    public static class PriceTime{
        private Long dateTime;
        private BigDecimal currentPrice;
    }
}
