package com.cfbest.server.automap;

import com.cfbest.server.model.dto.excel.CFIPExportDTO;
import com.cfbest.server.model.po.CFCDNIPPO;
import com.sunny.framework.core.automap.CentralConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(config = CentralConfig.class)
public interface CFCDNIPAutoMap {

    @Mappings({
            @Mapping(source = "valueStr", target = "ip"),
    })
    CFIPExportDTO mapToExportDTO(CFCDNIPPO dto);

    List<CFIPExportDTO> mapToExportDTOs(List<CFCDNIPPO> list);

}
