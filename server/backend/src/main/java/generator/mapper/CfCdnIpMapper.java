package generator.mapper;

import generator.domain.CfCdnIp;

/**
* @author chenzhaoyu
* @description 针对表【cf_cdn_ip(Cloudflare CDN IP表)】的数据库操作Mapper
* @createDate 2024-01-29 15:47:31
* @Entity generator.domain.CfCdnIp
*/
public interface CfCdnIpMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CfCdnIp record);

    int insertSelective(CfCdnIp record);

    CfCdnIp selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CfCdnIp record);

    int updateByPrimaryKey(CfCdnIp record);

}
