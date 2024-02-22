package com.cfbest.server.model.po;

import com.sunny.framework.core.model.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * telegram消息表
 *
 * @TableName telegram_message
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TelegramMessagePO extends BasePO<Long, Long> {

    private Long messageId;

    private LocalDateTime messageTime;
    /**
     * 0:text,1:file
     */
    private Integer type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 0:同步中,1:同步完成
     */
    private Integer status;

    /**
     * 是否最新
     */
    private Boolean isLatest;
}