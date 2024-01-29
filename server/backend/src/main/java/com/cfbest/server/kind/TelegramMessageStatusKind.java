package com.cfbest.server.kind;

import lombok.Getter;

@Getter
public enum TelegramMessageStatusKind {
    SYNCING(0, "同步中"),
    SYNCED(1, "同步完成"),
    PROCESSED(2, "处理完成");

    final Integer value;
    final String label;

    TelegramMessageStatusKind(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

}
