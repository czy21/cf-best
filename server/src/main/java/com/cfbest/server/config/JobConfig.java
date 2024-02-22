package com.cfbest.server.config;

import com.cfbest.server.service.TelegramMessageService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class JobConfig {
    TelegramMessageService telegramMessageService;

    public JobConfig(TelegramMessageService telegramMessageService) {
        this.telegramMessageService = telegramMessageService;
    }

    @XxlJob("processTelegramMessage")
    public void processTelegramMessage() {
        telegramMessageService.process(LocalDate.now());
    }

    @XxlJob("cleanTelegramMessage")
    public void cleanTelegramMessage() {
        telegramMessageService.clean();
    }
}
