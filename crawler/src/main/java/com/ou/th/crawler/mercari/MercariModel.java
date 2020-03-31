package com.ou.th.crawler.mercari;

import com.ou.th.crawler.common.CommonModel;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
/**
 * @author kpkym
 * Date: 2020-03-18 23:30
 */
@Data
@RedisHash("mercariModel")
public class MercariModel extends CommonModel {
    @Id
    private String id;

    @MyExtractBy(list = "//h3[contains(@class, 'items-box-name')]/text()", detail = "//h1[@class='item-name']/text()")
    private String title;

    @MyExtractBy(list = "//img/@data-src", detail = "//div[@class='owl-carousel']//img/@data-src")
    private String picturesOriginal;

    @MyExtractBy(list = "//div[contains(@class, 'items-box-price')]/text()", detail = "//div[@class='item-price-box text-center']/span[@class='item-price bold']/text()")
    private BigDecimal price;

    @MyExtractBy(list = "//section[@class='items-box']/a/@href", detail = "//link[@rel='canonical']/@href")
    private String url;
}
