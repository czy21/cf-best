package com.cfbest.server.config;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.cfbest.server.feign.IpApiFeign;
import com.cfbest.server.service.TelegramMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Configuration
public class JobConfig {
    TelegramMessageService telegramMessageService;

    public JobConfig(TelegramMessageService telegramMessageService) {
        this.telegramMessageService = telegramMessageService;
    }

    @Autowired
    IpApiFeign ipApiFeign;

    @XxlJob("processTGForCF")
    public void processTelegramMessageForCF() {
        telegramMessageService.process(LocalDate.now());
    }

    @XxlJob("cleanTelegramMessage")
    public void cleanTelegramMessage() {
        telegramMessageService.clean();
    }
}
