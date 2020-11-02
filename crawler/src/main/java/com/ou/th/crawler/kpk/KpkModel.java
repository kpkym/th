package com.ou.th.crawler.kpk;

import com.ou.th.crawler.common.anatation.MyExtractBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author kpkym
 * Date: 2020-10-31 23:30
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KpkModel {
    @Id
    private String id;

    private String code;

    @MyExtractBy(pageList = "", itemDetail = "//h1[@id='work_name']/a/text()")
    private String title;

    @MyExtractBy(pageList = "", itemDetail = "", type = MyExtractBy.Type.Regex)
    private String xingshi;

    @MyExtractBy(pageList = "", itemDetail = "", type = MyExtractBy.Type.Regex)
    private String rongliang;
}
