package filter.node;

import cn.hutool.core.util.StrUtil;
import com.example.common.exception.CommonException;
import com.example.kg.dto.HolderQueryDTO;
import filter.HolderVerifyFilter;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.kg.common.constant.Constant.*;

/**
 * 查询股东节点查询数据合法
 *
 */
@Component
public class HolderParamVerifyFilter implements HolderVerifyFilter<HolderQueryDTO> {

    /**
     * 支持查询最小日期
     */
    volatile Date date = null;

    @Override
    public void handler(HolderQueryDTO requestParam) {
        if (StrUtil.isBlank(requestParam.getName())) {
            throw new CommonException("股东名称不能为空");
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
