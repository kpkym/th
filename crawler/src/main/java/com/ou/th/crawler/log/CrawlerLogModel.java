package com.ou.th.crawler.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author kpkym
 * Date: 2020-04-16 19:47
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CrawlerLogModel {
    @Id
    private String id;
    private String site;
    private Long time;
}
