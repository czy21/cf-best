package com.cfbest.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CFCDNIPService {

    void populateRegion(Long telegramMessageId);
    void copyToView(List<LocalDateTime> timeInterval);
}
