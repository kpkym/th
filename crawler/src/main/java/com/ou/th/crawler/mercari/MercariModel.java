package com.ou.th.crawler.mercari;

import com.ou.th.crawler.common.CommonModel;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import com.ou.th.crawler.common.anatation.NeedUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
/**
 * @author kpkym
 * Date: 2020-03-18 23:30
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MercariModel extends CommonModel {
    @Id
    private String id;

    private Integer isSold;

    @NeedUpdate
    @MyExtractBy(pageList = "//h3[contains(@class, 'items-box-name')]/text()", itemDetail = "//h1[@class='item-name']/text()")
    private String title;

    private String picture;

    @MyExtractBy(pageList = "//img/@data-src", itemDetail = "//div[@class='owl-carousel']//img/@data-src")
    private String picturesOriginal;

    @NeedUpdate
    @MyExtractBy(pageList = "//div[contains(@class, 'items-box-price')]/text()", itemDetail = "//div[@class='item-price-box text-center']/span[@class='item-price bold']/text()", targetClazz = BigDecimal.class)
    private BigDecimal price;

    @MyExtractBy(pageList = "//section[@class='items-box']/a/@href", itemDetail = "//link[@rel='canonical']/@href")
    private String url;
}
