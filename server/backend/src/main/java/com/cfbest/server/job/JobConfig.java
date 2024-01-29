package com.cfbest.server.job;

import com.cfbest.server.feign.IpApiFeign;
import com.cfbest.server.feign.model.IpApiBatchResult;
import com.cfbest.server.service.TelegramMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class JobConfig {
    TelegramMessageService telegramMessageService;

    public JobConfig(TelegramMessageService telegramMessageService) {
        this.telegramMessageService = telegramMessageService;
    }

    @Autowired
    IpApiFeign ipApiFeign;

    @Scheduled(cron = "0/5 * * * * ?")
    public void processTelegramMessageForCF() {
        telegramMessageService.process(LocalDate.now());
    }
}
