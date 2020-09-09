package com.ou.th.controller;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import com.ou.th.util.Msg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CommonController {
    @Value("${server.port}")
    Integer serverPort;

    Integer imgPort = 57596;

    @GetMapping("ip")
    public Msg getIp() {
        String s = NetUtil.localIpv4s().stream()
                .filter(e -> !e.startsWith("127"))
                .findFirst()
                .map(URLUtil::normalize)
                .orElse("http://127.0.0.1");

        Map<String, String> map = Map.of(
                "serverUrl", s + ":" + serverPort,
                "imgUrl", s + ":" + imgPort
        );
        return Msg.success(map);
    }
}
