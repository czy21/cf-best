package com.cfbest.server.model.dto;

import com.sunny.framework.core.model.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Cloudflare CDN IP表
 *
 * @TableName cf_cdn_ip
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CFCDNIPDTO extends BasePO<Long, Long> {

    /**
     * ip字符串
     */
    private String valueStr;

    /**
     * 0:v4,1:v6
     */
    private Integer type;

    /**
     * 国家
     */
    private String country;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 地区
     */
    private String region;

    /**
     * 地区名称
     */
    private String regionName;

    /**
     * 城市
     */
    private String city;

    /**
     * 提供商
     */
    private String isp;
}