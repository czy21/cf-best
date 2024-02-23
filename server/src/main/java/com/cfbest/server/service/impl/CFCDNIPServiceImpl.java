package com.cfbest.server.service.impl;

import com.cfbest.server.feign.IpApiFeign;
import com.cfbest.server.feign.model.IpApiBatchResult;
import com.cfbest.server.mapper.CFCDNIPMapper;
import com.cfbest.server.model.dto.CFCDNIPDTO;
import com.cfbest.server.model.po.CFCDNIPPO;
import com.cfbest.server.model.query.CFCDNIPQuery;
import com.cfbest.server.service.CFCDNIPService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunny.framework.core.model.PagingResult;
import com.sunny.framework.core.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class CFCDNIPServiceImpl implements CFCDNIPService {


    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    IpApiFeign ipApiFeign;
    @Autowired
    CFCDNIPMapper cfcdnipMapper;

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
        }
    }

    @Override
    public PagingResult<CFCDNIPDTO> page(CFCDNIPQuery query) {
        try (Page<CFCDNIPDTO> page = PageHelper.startPage(query.getPage(), query.getPageSize())) {
            PageInfo<CFCDNIPDTO> pageInfo = page.doSelectPageInfo(() -> cfcdnipMapper.selectListBy(query));
            return PageUtil.convert(pageInfo);
        }
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
