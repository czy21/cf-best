package com.cfbest.server.service;

import com.cfbest.server.model.dto.CFAggCountryDTO;
import com.cfbest.server.model.dto.CFDayCountryDTO;
import com.cfbest.server.model.dto.CFCDNIPDTO;
import com.cfbest.server.model.dto.excel.CFIPExportDTO;
import com.cfbest.server.model.query.CFCDNIPQuery;
import com.sunny.framework.core.model.PagingResult;
import com.sunny.framework.core.model.SimpleItemModel;

import java.time.LocalDateTime;
import java.util.List;

public interface CFCDNIPService {

    void populateRegion(Long telegramMessageId);
    void copyToView(List<LocalDateTime> timeInterval);
    PagingResult<CFCDNIPDTO> page(CFCDNIPQuery query);
    List<CFIPExportDTO> exportBy(CFCDNIPQuery query);
    List<SimpleItemModel<String>> getCountryCityTree();
    List<CFAggCountryDTO> getAggCountry();
    CFDayCountryDTO getDayCountry();
}
