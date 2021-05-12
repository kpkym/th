package com.ou.th.crawler.dl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ou.th.config.KpkConfig;
import com.ou.th.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
@Slf4j
@Service
public class DlService {
    @Autowired
    DlRepo dlRepo;

    @Autowired
    HttpClientDownloader httpClientDownloader;

    @Autowired
    HttpUtil httpUtil;

    @Autowired
    KpkConfig kpkConfig;

    @Autowired
    MongoTemplate mongoTemplate;


    @Async
    public void save(DlModel dlModel) {
        if (StrUtil.isEmpty(dlModel.getTitle())
        ) {
            return;
        }

        String body = HttpRequest.get(StrUtil.format(kpkConfig.getDlDetail(), dlModel.getCode()))
                .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(kpkConfig.getProxy().getHost(), kpkConfig.getProxy().getPort())))
                .execute()
                .body();
        JSONObject jsonObject = JSONUtil.parseObj(body).getJSONObject(dlModel.getCode());

        Map<String, Integer> ranks = jsonObject.getJSONArray("rank").stream().filter(e -> "voice".equals(JSONUtil.parseObj(e).getStr("category")))
                .collect(Collectors.toMap(e -> JSONUtil.parseObj(e).getStr("term"), e -> JSONUtil.parseObj(e).getInt("rank")));


        dlModel.setOfficialPrice(jsonObject.getInt("official_price"));
        dlModel.setRank24h(ranks.get("day"));
        dlModel.setRank7d(ranks.get("week"));
        dlModel.setRank30d(ranks.get("month"));
        dlModel.setDlCount(jsonObject.getInt("dl_count"));
        dlModel.setWishlistCount(jsonObject.getInt("wishlist_count"));
        dlModel.setScore(jsonObject.getBigDecimal("rate_average_2dp"));
        dlModel.setScoreCount(jsonObject.getInt("rate_count"));
        dlModel.setReviewCount(jsonObject.getInt("review_count"));
        dlRepo.save(dlModel);
    }

    public void save(List<DlModel> kpkModels) {
        dlRepo.saveAll(kpkModels);
    }

    public List<DlModel> list() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "dlCount"));

        List<DlModel> dlModels = mongoTemplate.find(query, DlModel.class);

        return dlModels;
    }
}
