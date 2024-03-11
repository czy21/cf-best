package com.cfbest.server.controller;

import com.cfbest.server.model.dto.CFAggCountryDTO;
import com.cfbest.server.model.dto.CFDayCountryDTO;
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
    public CommonResult<List<CFAggCountryDTO>> getAggCountry() {
        return CommonResult.ok(cfcdnipService.getAggCountry());
    }

    @GetMapping(path = "getDayCountry")
    public CommonResult<CFDayCountryDTO> getDayCountry() {
        return CommonResult.ok(cfcdnipService.getDayCountry());
    }
}
