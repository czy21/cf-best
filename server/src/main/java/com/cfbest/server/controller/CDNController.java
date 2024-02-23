package com.cfbest.server.controller;

import com.cfbest.server.model.dto.CFCDNIPDTO;
import com.cfbest.server.model.query.CFCDNIPQuery;
import com.cfbest.server.service.CFCDNIPService;
import com.sunny.framework.core.model.CommonResult;
import com.sunny.framework.core.model.PagingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "cdn")
@RestController
public class CDNController {

    @Autowired
    CFCDNIPService cfcdnipService;

    @PostMapping(path = "page")
    public CommonResult<PagingResult<CFCDNIPDTO>> page(@RequestBody CFCDNIPQuery query) {
        return CommonResult.ok(cfcdnipService.page(query));
    }
}
