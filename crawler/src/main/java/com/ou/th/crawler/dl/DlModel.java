package com.ou.th.crawler.dl;

import com.ou.th.crawler.common.anatation.MyExtractBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-18 23:30
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DlModel {
    // RJ编号
    @Id
    private String code;

    // 标题
    @MyExtractBy(itemDetail = "(?<=itemprop=\"url\">)[\\s\\S]*?(?=<\\/a>)", type = MyExtractBy.Type.Regex)
    private String title;

    // 封面
    @MyExtractBy(itemDetail = "(?<=<meta name=\"twitter:image:src\" content=\")[\\s\\S]*?(?=\">)", type = MyExtractBy.Type.Regex)
    private String img;


    // 社团名
    @MyExtractBy(itemDetail = "(?<=社团名</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex)
    private String circle;

    // 贩卖日
    @MyExtractBy(itemDetail = "(?<=贩卖日</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex)
    private String releaseDate;

    // 最终更新日
    @MyExtractBy(itemDetail = "(?<=最终更新日</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex)
    private String lastDate;

    // 系列名
    @MyExtractBy(itemDetail = "(?<=系列名</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex)
    private String series;

    // 声优
    @MyExtractBy(itemDetail = "(?<=声优</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex, separatorRegex="/")
    private List<String> voice;

    // 年龄指定
    @MyExtractBy(itemDetail = "(?<=年龄指定</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex)
    private String age;

    // 作品类型
    @MyExtractBy(itemDetail = "(?<=作品类型</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex)
    private String format;

    // 其他
    @MyExtractBy(itemDetail = "(?<=对应语言</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex, separatorRegex = "\\s+")
    private List<String> miscellaneous;

    // 分类
    @MyExtractBy(itemDetail = "(?<=分类</th>)[\\s\\S]*?(?=<\\/td>)", type = MyExtractBy.Type.Regex, separatorRegex = "\\s+")
    private List<String> genre;


    private Integer officialPrice;

    private Integer rank24h;

    private Integer rank7d;

    private Integer rank30d;

    // 贩卖数
    private Integer dlCount;

    // 收藏数
    private Integer wishlistCount;

    // 评分
    private BigDecimal score;

    // 评分数
    private Integer scoreCount;

    // 评价数
    private Integer reviewCount;
}
