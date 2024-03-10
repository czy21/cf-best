package com.cfbest.server.controller;

import com.cfbest.server.model.dto.CFBestAggCountryDTO;
import com.cfbest.server.model.dto.CFCDNIPDTO;
import com.cfbest.server.model.query.CFCDNIPQuery;
import com.cfbest.server.service.CFCDNIPService;
import com.sunny.framework.core.model.CommonResult;
import com.sunny.framework.core.model.PagingResult;
import com.sunny.framework.core.model.SimpleItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "cdn")
@RestController
public class CDNController {

    @Autowired
    CFCDNIPService cfcdnipService;

    @PostMapping(path = "page")
    public CommonResult<PagingResult<CFCDNIPDTO>> page(@RequestBody CFCDNIPQuery query) {
        return CommonResult.ok(cfcdnipService.page(query));
    }

    @GetMapping(path = "countryTree")
    public CommonResult<List<SimpleItemModel<String>>> countryTree() {
        return CommonResult.ok(cfcdnipService.getCountryCityTree());
    }

    @GetMapping(path = "getAggCountry")
    public CommonResult<List<CFBestAggCountryDTO>> getAggCountry() {
        return CommonResult.ok(cfcdnipService.getAggCountry());
    }
}
