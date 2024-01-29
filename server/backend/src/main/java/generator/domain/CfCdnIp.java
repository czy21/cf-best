package generator.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Cloudflare CDN IP表
 * @TableName cf_cdn_ip
 */
@Data
public class CfCdnIp implements Serializable {
    /**
     * 主键自增
     */
    private Long id;

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

    /**
     * 
     */
    private Long telegramMessageId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 是否删除
     */
    private Boolean deleted;

    private static final long serialVersionUID = 1L;
}