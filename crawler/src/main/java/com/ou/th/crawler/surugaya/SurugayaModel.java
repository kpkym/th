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
@Data()
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SurugayaModel extends CommonModel {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @MyExtractBy(pageList = "(?<=<p.{0,1000}class=.title.>.{0,1000}<a.{0,1000}>)[\\s\\S]*?(?=<\\/a)",
            itemDetail = "(?<=<script type=\"application\\/ld\\+json\">[\\s\\S]{0,500}\"name\".{0,20}?\").+?(?=\",)",
            regexPreHandle = "//img[@id='imagecaption']/@title",
            type= MyExtractBy.Type.Regex)
    @NeedUpdate
    private String title;


    @MyExtractBy(pageList = "//span[@class='sale_time']/text()", itemDetail = "//p[@class='mgnB5']/strong/text()")
    @NeedUpdate
    private String promotion;

    private String picture;

    private String picturesOriginal;

    @NeedUpdate
    @MyExtractBy(pageList = "//p[@class='price']/text()|//span[@class='text-red']/strong/text()", itemDetail = "//table[@class='table_grade_list']//span[contains(@class, 'text-red')]/text()", targetClazz = BigDecimal.class)
    private BigDecimal price;

    // @MyExtractBy(pageList = "//p[@class='title']/a/@href", itemDetail = "//input[@type='hidden' and @name='url']/@value")
    private String url;
}
