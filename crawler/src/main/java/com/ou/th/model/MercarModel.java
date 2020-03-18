package com.ou.th.model;

import com.ou.th.anatation.MyExtractBy;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-18 23:30
 */
@Data
public class MercarModel {
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

    @MyExtractBy(value = "//section//div/div[3]/div/div/img/@src", needAll = true)
    private List<String> picture;

    @MyExtractBy("//div[@class='item-price-box text-center']/span[@class='item-price bold']/text()")
    private BigDecimal price;

    @MyExtractBy(value = "", skip = true)
    private String url;
}