package com.cfbest.server.mapper;

import com.cfbest.server.model.dto.CFAggCountryDTO;
import com.cfbest.server.model.po.CFCDNIPPO;
import com.cfbest.server.model.po.CFIPStat;
import com.cfbest.server.model.query.CFCDNIPQuery;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.cursor.Cursor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chenzhaoyu
 * @description 针对表【cf_cdn_ip(Cloudflare CDN IP表)】的数据库操作Mapper
 * @createDate 2024-01-29 14:02:12
 * @Entity generator.domain.CfCdnIp
 */
public interface CFCDNIPMapper {
    @Options(fetchSize = Integer.MIN_VALUE)
    Cursor<CFCDNIPPO> selectCursorByTelegramMessageId(@Param("telegramMessageId") Long telegramMessageId);

    @Select("select * from cf_cdn_ip where value_str = #{valueStr} and country is not null limit 1")
    CFCDNIPPO selectOneByValueStrAndHaveRegion(@Param("valueStr") String valueStr);

    void updateByValueStrAndNoRegion(CFCDNIPPO entity);

    void copyToView(@Param("timeInterval") List<LocalDateTime> timeInterval);

    void aggByCountry(@Param("date") LocalDate date);

    @Update("truncate table cf_cdn_ip_view")
    void truncateView();

    @Select("select max(create_time) from cf_cdn_ip_view")
    LocalDateTime selectViewMaxTime();

    List<CFCDNIPPO> selectListBy(CFCDNIPQuery query);

    List<CFCDNIPPO> selectListGroupByCountryAndCity();

    List<CFAggCountryDTO> selectListForAggCountry();

    List<CFIPStat> selectListGroupByDateAndCountry(@Param("dateInterval") List<LocalDate> dateInterval);

}




