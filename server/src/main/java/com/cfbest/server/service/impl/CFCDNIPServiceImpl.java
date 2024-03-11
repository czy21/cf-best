package com.cfbest.server.service.impl;

import com.cfbest.server.automap.CFCDNIPAutoMap;
import com.cfbest.server.feign.IpApiFeign;
import com.cfbest.server.feign.model.IpApiBatchResult;
import com.cfbest.server.mapper.CFCDNIPMapper;
import com.cfbest.server.model.dto.CFAggCountryDTO;
import com.cfbest.server.model.dto.CFCDNIPDTO;
import com.cfbest.server.model.dto.CFDayCountryDTO;
import com.cfbest.server.model.dto.excel.CFIPExportDTO;
import com.cfbest.server.model.po.CFCDNIPPO;
import com.cfbest.server.model.po.CFIPStat;
import com.cfbest.server.model.query.CFCDNIPQuery;
import com.cfbest.server.service.CFCDNIPService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunny.framework.core.model.PagingResult;
import com.sunny.framework.core.model.SimpleItemModel;
import com.sunny.framework.core.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CFCDNIPServiceImpl implements CFCDNIPService {


    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    IpApiFeign ipApiFeign;
    @Autowired
    CFCDNIPMapper cfcdnipMapper;
    @Autowired
    CFCDNIPAutoMap cfcdnipAutoMap;

    @Override
    public void populateRegion(Long telegramMessageId) {
        List<CFCDNIPPO> records = new ArrayList<>();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CFCDNIPMapper cfcdnipMapperInternal = sqlSession.getMapper(CFCDNIPMapper.class);
            Cursor<CFCDNIPPO> cursor = cfcdnipMapperInternal.selectCursorByTelegramMessageId(telegramMessageId);
            for (CFCDNIPPO t : cursor) {
                records.add(t);
                if (records.size() >= 50) {
                    updateRegion(records, cfcdnipMapperInternal);
                    sqlSession.commit();
                    records.clear();
                }
            }
            if (records.size() > 0) {
                updateRegion(records, cfcdnipMapperInternal);
                sqlSession.commit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyToView(List<LocalDateTime> timeInterval) {
        LocalDateTime latestViewTime = cfcdnipMapper.selectViewMaxTime();
        if (latestViewTime == null || timeInterval.get(0).toLocalDate().isAfter(latestViewTime.toLocalDate())) {
            cfcdnipMapper.truncateView();
            cfcdnipMapper.copyToView(timeInterval);
            cfcdnipMapper.aggByCountry(timeInterval.get(0).toLocalDate());
        }
    }

    @Override
    public PagingResult<CFCDNIPDTO> page(CFCDNIPQuery query) {
        try (Page<CFCDNIPDTO> page = PageHelper.startPage(query.getPage(), query.getPageSize())) {
            PageInfo<CFCDNIPDTO> pageInfo = page.doSelectPageInfo(() -> cfcdnipMapper.selectListBy(query));
            return PageUtil.convert(pageInfo);
        }
    }

    @Override
    public List<CFIPExportDTO> exportBy(CFCDNIPQuery query) {
        return cfcdnipAutoMap.mapToExportDTOs(cfcdnipMapper.selectListBy(query));
    }

    @Override
    public List<SimpleItemModel<String>> getCountryCityTree() {
        List<CFCDNIPPO> cfcdnippos = cfcdnipMapper.selectListGroupByCountryAndCity();
        List<SimpleItemModel<String>> countryTree = cfcdnippos.stream()
                .map(t -> SimpleItemModel.of(t.getCountry(), t.getCountry()))
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SimpleItemModel::getValue))), ArrayList::new));
        countryTree.forEach(t -> {
            List<SimpleItemModel<String>> children =
                    cfcdnippos.stream()
                            .filter(s -> s.getCountry().equals(t.getValue()))
                            .map(s -> SimpleItemModel.of(s.getCity(), s.getCity()))
                            .collect(Collectors.toList());
            t.setChildren(children);
        });
        return countryTree;
    }

    @Override
    public List<CFAggCountryDTO> getAggCountry() {
        return cfcdnipMapper.selectListForAggCountry();
    }

    @Override
    public CFDayCountryDTO getDayCountry() {
        CFDayCountryDTO dto = new CFDayCountryDTO();
        LocalDate endTime = cfcdnipMapper.selectViewMaxTime().toLocalDate();
        LocalDate startTime = endTime.plusDays(-6);
        List<LocalDate> dates = Stream.iterate(startTime, date -> date.plusDays(1)).limit(7).collect(Collectors.toList());
        dto.setDays(dates);
        Map<String, List<CFIPStat>> ipStatMap = cfcdnipMapper.selectListGroupByDateAndCountry(List.of(startTime, endTime)).stream().collect(Collectors.groupingBy(t -> t.getCountry()));
        List<CFDayCountryDTO.Series> series = new ArrayList<>();
        ipStatMap.forEach((k, v) -> {
            CFDayCountryDTO.Series series1 = new CFDayCountryDTO.Series();
            series1.setName(k);
            series1.setData(new ArrayList<>());
            for (LocalDate d : dates) {
                v.stream().filter(t -> t.getDate().equals(d)).findFirst()
                        .ifPresentOrElse(t -> series1.getData().add(t.getValue()), () -> series1.getData().add(0));
            }
            series.add(series1);
        });
        dto.setSeries(series);
        return dto;
    }

    private void updateRegion(List<CFCDNIPPO> records, CFCDNIPMapper cfcdnipMapperInternal) {
        List<CFCDNIPPO> updates = new ArrayList<>();
        HashSet<String> needQueryIps = new HashSet<>();
        for (CFCDNIPPO t : records) {
            CFCDNIPPO existValueStrAndRegion = cfcdnipMapperInternal.selectOneByValueStrAndHaveRegion(t.getValueStr());
            if (existValueStrAndRegion == null) {
                needQueryIps.add(t.getValueStr());
                continue;
            }
            CFCDNIPPO updateEntity = new CFCDNIPPO();
            updateEntity.setValueStr(t.getValueStr());
            updateEntity.setCountry(existValueStrAndRegion.getCountry());
            updateEntity.setCountryCode(existValueStrAndRegion.getCountryCode());
            updateEntity.setRegion(existValueStrAndRegion.getRegion());
            updateEntity.setRegionName(existValueStrAndRegion.getRegionName());
            updateEntity.setCity(existValueStrAndRegion.getCity());
            updateEntity.setIsp(existValueStrAndRegion.getIsp());
            updates.add(updateEntity);
        }
        if (CollectionUtils.isNotEmpty(needQueryIps)) {
            List<IpApiBatchResult> results = Optional.ofNullable(ipApiFeign.batch(needQueryIps)).orElse(new ArrayList<>());
            results.stream()
                    .filter(t -> StringUtils.isNotEmpty(t.getCountry()))
                    .forEach(t -> {
                        CFCDNIPPO updateEntity = new CFCDNIPPO();
                        updateEntity.setValueStr(t.getQuery());
                        updateEntity.setCountry(t.getCountry());
                        updateEntity.setCountryCode(t.getCountryCode());
                        updateEntity.setRegion(t.getRegion());
                        updateEntity.setRegionName(t.getRegionName());
                        updateEntity.setCity(t.getCity());
                        updateEntity.setIsp(t.getIsp());
                        updates.add(updateEntity);
                    });
        }
        updates.forEach(cfcdnipMapperInternal::updateByValueStrAndNoRegion);
    }

}
