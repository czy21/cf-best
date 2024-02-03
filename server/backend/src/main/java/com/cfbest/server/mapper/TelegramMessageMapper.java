package com.cfbest.server.mapper;

import com.cfbest.server.model.po.TelegramMessagePO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chenzhaoyu
 * @description 针对表【telegram_message(telegram消息表)】的数据库操作Mapper
 * @createDate 2024-01-29 14:02:12
 * @Entity generator.domain.TelegramMessage
 */
public interface TelegramMessageMapper {

    List<TelegramMessagePO> selectListByMessageTime(@Param("timeInterval") List<LocalDateTime> timeInterval);

    @Update("update telegram_message set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("select max(date(`message_time`)) from telegram_message")
    LocalDate selectLatestDate();

    void deleteByTimeInterval(LocalDateTime retainTime);
}




