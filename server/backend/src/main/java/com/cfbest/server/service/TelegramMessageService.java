package com.cfbest.server.service;

import java.time.LocalDate;

public interface TelegramMessageService {

    void process(LocalDate messageDate);

    void clean();
}
