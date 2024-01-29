package com.cfbest.server.feign.model;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Data
public class IpApiBatchResult {

    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private Float lat;
    private Float lon;
    private String timezone;
    private String isp;
    private String org;
    private String query;
}
