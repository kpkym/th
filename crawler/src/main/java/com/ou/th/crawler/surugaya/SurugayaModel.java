package com.ou.th.crawler.surugaya;

import com.ou.th.crawler.common.CommonModel;
import com.ou.th.crawler.common.anatation.MyExtractBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author kpkym
 * Date: 2020-03-31 17:28
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@RedisHash("surugayaModel")
public class SurugayaModel extends CommonModel {
    @Id
    private String id;

    @MyExtractBy(list = "//p[@class='title']/a/text()", detail = "//p[@class='title']/a/text()")
    private String title;

    @MyExtractBy(list = "//div[@class='item']//img/@data-src", detail = "//div[@class='item']//img/@data-src")
    private String pictureOriginal;

    @MyExtractBy(list = "//a/@href", detail = "")
    private String url;
}
