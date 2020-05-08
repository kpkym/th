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

    @MyExtractBy(list = "//p[@class='title']/a/text()", detail = "//h2[@id='item_title']/text()")
    private String title;

    private String picture;

    private String picturesOriginal;

    @NeedUpdate
    @MyExtractBy(list = "//p[@class='price']/text()|//span[@class='text-red']/strong/text()", detail = "//table[@class='table_grade_list']//span/text()")
    private BigDecimal price;

    @MyExtractBy(list = "//p[@class='title']/a/@href", detail = "//input[@type='hidden' and @name='url']/@value")
    private String url;


}
