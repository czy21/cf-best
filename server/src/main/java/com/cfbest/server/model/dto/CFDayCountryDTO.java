package com.cfbest.server.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CFDayCountryDTO {
    private List<LocalDate> days;
    private List<Series> series;

    @Data
    public static class Series {
        private String name;
        private List<Integer> data;
    }
}
