package com.cfbest.server.mapper;

import com.cfbest.server.model.po.CFCDNIPPO;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

/**
 * @author chenzhaoyu
 * @description 针对表【cf_cdn_ip(Cloudflare CDN IP表)】的数据库操作Mapper
 * @createDate 2024-01-29 14:02:12
 * @Entity generator.domain.CfCdnIp
 */
public interface CFCDNIPMapper {
    @Options(fetchSize = Integer.MIN_VALUE)
    Cursor<CFCDNIPPO> selectCursorByTelegramMessageId(@Param("telegramMessageId") Long telegramMessageId);

    @Select("select * from cf_cdn_ip where value_str = #{valueStr} and region is not null")
    CFCDNIPPO selectOneByValueStrAndHaveRegion(@Param("valueStr") String valueStr);

    void updateByValueStrAndNoRegion(CFCDNIPPO entity);
}




