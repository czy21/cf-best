package com.cfbest.server.controller;

import com.cfbest.server.model.dto.CFAggCountryDTO;
import com.cfbest.server.model.dto.CFDayCountryDTO;
import com.cfbest.server.model.dto.CFCDNIPDTO;
import com.cfbest.server.model.dto.excel.CFIPExportDTO;
import com.cfbest.server.model.query.CFCDNIPQuery;
import com.cfbest.server.service.CFCDNIPService;
import com.sunny.framework.core.model.CommonResult;
import com.sunny.framework.core.model.PagingResult;
import com.sunny.framework.core.model.SimpleItemModel;
import com.sunny.framework.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequestMapping(path = "cdn")
@RestController
public class CDNController extends BaseController {

    @Autowired
    CFCDNIPService cfcdnipService;

    @PostMapping(path = "page")
    public CommonResult<PagingResult<CFCDNIPDTO>> page(@RequestBody CFCDNIPQuery query) {
        return CommonResult.ok(cfcdnipService.page(query));
    }

    @PostMapping(path = "export")
    public CompletableFuture<ResponseEntity<byte[]>> export(@RequestBody CFCDNIPQuery query) throws UnsupportedEncodingException {
        String fileName = URLEncoder.encode(MessageFormat.format("cf-cdn-ip-{0}.xlsx", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), StandardCharsets.UTF_8.name());
        return CompletableFuture.supplyAsync(() -> downloadExcel(cfcdnipService.exportBy(query), CFIPExportDTO.class, fileName));
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
