package com.cfbest.server.model.query;

import com.sunny.framework.core.model.PagingParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CFCDNIPQuery extends PagingParam {
    private List<List<String>> locations;
}
