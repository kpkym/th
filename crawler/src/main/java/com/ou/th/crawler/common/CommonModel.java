package com.ou.th.crawler.common;

import com.ou.th.crawler.common.anatation.NeedOlder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-31 20:13
 */
@Data
public class CommonModel {
    // 感兴趣 看过之后会继续留在界面
    @NeedOlder
    protected boolean isLike = false;

    protected boolean isDel = false;

    protected boolean isChange = false;

    protected String picture;

    @NeedOlder
    protected List<PriceTime> priceTimes = new ArrayList<>();

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class PriceTime{
        private Long dateTime;
        private BigDecimal price;
    }
}
