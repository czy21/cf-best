package com.cfbest.server.model.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CFIPExportDTO {
    @ExcelProperty("ip")
    private String ip;
    @ExcelProperty("国家")
    private String country;
    @ExcelProperty("国家代码")
    private String countryCode;
    @ExcelProperty("城市")
    private String city;
    @ExcelProperty("isp")
    private String isp;
}
