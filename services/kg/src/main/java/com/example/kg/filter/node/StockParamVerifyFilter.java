package com.example.kg.filter.node;

import cn.hutool.core.util.StrUtil;
import com.example.common.exception.CommonException;
import com.example.kg.dto.StockQueryDTO;
import com.example.kg.filter.StockVerifyFilter;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.kg.common.constant.Constant.*;


/**
 * 查询股票节点查询数据合法
 *
 */
@Component
public class StockParamVerifyFilter implements StockVerifyFilter<StockQueryDTO> {
    /**
     * 支持查询最小日期
     */
    volatile Date date = null;

    @Override
    public void handler(StockQueryDTO requestParam) {
        if (StrUtil.isBlank(requestParam.getTsCode())) {
            throw new CommonException("股票代码不能为空");
        }
        if (requestParam.getEndDate() <= VALID_DATE) {
            throw new CommonException("仅开放2021年后数据查询");
        }
        date = new Date();
        if (requestParam.getEndDate() > date.getYear() * YEAR + date.getMonth() * MONTH + date.getDay()){
            throw new CommonException("日期不合法");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
