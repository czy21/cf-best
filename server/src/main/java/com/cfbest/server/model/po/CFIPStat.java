package com.cfbest.server.model.po;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CFIPStat {
    private LocalDate date;
    private String country;
    private Integer value;
}
