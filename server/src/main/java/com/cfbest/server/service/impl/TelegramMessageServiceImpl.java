package com.cfbest.server.service.impl;

import com.cfbest.server.config.TelegramProperties;
import com.cfbest.server.kind.TelegramMessageStatusKind;
import com.cfbest.server.mapper.TelegramMessageMapper;
import com.cfbest.server.model.po.TelegramMessagePO;
import com.cfbest.server.service.CFCDNIPService;
import com.cfbest.server.service.TelegramMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TelegramMessageServiceImpl implements TelegramMessageService {

    private Logger logger = LoggerFactory.getLogger(TelegramMessageServiceImpl.class);

    @Autowired
    TelegramMessageMapper telegramMessageMapper;
    @Autowired
    CFCDNIPService cfcdnipService;
    @Autowired
    TelegramProperties telegramProperties;

    @Override
    public void process(LocalDate messageDate) {
        List<LocalDateTime> messageTimeInterval = List.of(LocalDateTime.of(messageDate, LocalTime.MIN), LocalDateTime.of(messageDate, LocalTime.MAX));
        List<TelegramMessagePO> messages = telegramMessageMapper.selectListByMessageTime(messageTimeInterval);
        for (TelegramMessagePO m : messages) {
            if (TelegramMessageStatusKind.SYNCED.getValue().equals(m.getStatus())) {
                try {
                    cfcdnipService.populateRegion(m.getId());
                } catch (Exception e) {
                    logger.error(MessageFormat.format("{0} process fail", m.getFileName()), e);
                    continue;
                }
                m.setStatus(TelegramMessageStatusKind.PROCESSED.getValue());
                telegramMessageMapper.updateStatus(m.getId(), m.getStatus());
            }
        }
        boolean allProcessed = messages.stream().allMatch(t -> TelegramMessageStatusKind.PROCESSED.getValue().equals(t.getStatus()));
        boolean isLatest = messages.stream().anyMatch(TelegramMessagePO::getIsLatest);
        if (allProcessed && isLatest) {
            cfcdnipService.copyToView(messageTimeInterval);
            cfcdnipService.pruneCache();
        }
    }

    @Override
    public void clean() {
        LocalDate latestDate = telegramMessageMapper.selectLatestDate();
        if (latestDate != null && telegramProperties.getRetainDays() >= 3) {
            LocalDateTime telegramMessageTimeClosed = LocalDateTime.of(latestDate.minusDays(telegramProperties.getRetainDays()), LocalTime.MAX);
            logger.info("telegram.retainDays: {}", telegramProperties.getRetainDays());
            logger.info("telegram delete message time closed {}", telegramMessageTimeClosed);
            telegramMessageMapper.deleteByTimeInterval(telegramMessageTimeClosed);
        }
    }
}
