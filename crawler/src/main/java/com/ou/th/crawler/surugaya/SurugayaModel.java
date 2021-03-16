package com.ou.th.crawler.surugaya;

import com.ou.th.crawler.common.CommonModel;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import com.ou.th.crawler.common.anatation.NeedUpdate;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

/**
 * @author kpkym
 * Date: 2020-03-31 17:28
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SurugayaModel extends CommonModel {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @MyExtractBy(pageList = "(?<=<p.{0,1000}class=.title.>.{0,1000}<a.{0,1000}>)[\\s\\S]*?(?=<\\/a)",
            itemDetail = "(?<=id=\"item_title\">).+?(?=</h1>)",
            // regexPreHandleXpath = "//img[@id='imagecaption']/@title",
            type= MyExtractBy.Type.Regex)
    @NeedUpdate
    private String title;


    @MyExtractBy(pageList = "//span[@class='sale_time']/text()", itemDetail = "//p[@class='mgnB5']/strong/text()")
    @NeedUpdate
    private String promotion;

    private String picture;

    @MyExtractBy(pageList = "//span[@class='sale_time']/text()", itemDetail = "//img[@class='img-fluid' and @alt]/@src")
    private String picturesOriginal;

    @NeedUpdate
    @MyExtractBy(pageList = "//span[@class='text-red']/strong/text()", itemDetail = "//label[@class='mgnB0']/span[@class='text-price-detail price-buy']/text()", targetClazz = BigDecimal.class)
    private BigDecimal price;

    // @MyExtractBy(pageList = "//p[@class='title']/a/@href", itemDetail = "//link[@rel='canonical']/@href")
    private String url;
}
