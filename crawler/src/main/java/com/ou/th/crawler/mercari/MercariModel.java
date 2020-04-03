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

    @NeedUpdate
    @MyExtractBy(list = "//h3[contains(@class, 'items-box-name')]/text()", detail = "//h1[@class='item-name']/text()")
    private String title;

    private String picture;

    @MyExtractBy(list = "//img/@data-src", detail = "//div[@class='owl-carousel']//img/@data-src")
    private String picturesOriginal;

    @NeedUpdate
    @MyExtractBy(list = "//div[contains(@class, 'items-box-price')]/text()", detail = "//div[@class='item-price-box text-center']/span[@class='item-price bold']/text()")
    private BigDecimal price;

    @MyExtractBy(list = "//section[@class='items-box']/a/@href", detail = "//link[@rel='canonical']/@href")
    private String url;
}
