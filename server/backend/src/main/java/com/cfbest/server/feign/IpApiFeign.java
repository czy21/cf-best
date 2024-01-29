package com.cfbest.server.feign;

import com.cfbest.server.feign.model.IpApiBatchResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;

@FeignClient(value = "ipApi", url = "http://ip-api.com")
public interface IpApiFeign {

    // 最大上限100
    @PostMapping(path = "batch?lang=zh-CN")
    List<IpApiBatchResult> batch(@RequestBody HashSet<String> ips);
}
