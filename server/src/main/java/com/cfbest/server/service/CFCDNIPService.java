package com.cfbest.server.service;

import com.cfbest.server.model.dto.CFCDNIPDTO;
import com.cfbest.server.model.query.CFCDNIPQuery;
import com.sunny.framework.core.model.PagingResult;

import java.time.LocalDateTime;
import java.util.List;

public interface CFCDNIPService {

    void populateRegion(Long telegramMessageId);
    void copyToView(List<LocalDateTime> timeInterval);

    PagingResult<CFCDNIPDTO> page(CFCDNIPQuery query);
}