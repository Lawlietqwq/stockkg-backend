package filter;

import com.example.kg.common.enums.VerifyChainEnum;
import com.example.kg.dto.StockQueryDTO;
import com.example.patterns.chain.AbstractChainHandler;

public interface StockVerifyFilter<T extends StockQueryDTO> extends AbstractChainHandler<StockQueryDTO> {

    @Override
    default String mark() {
        return VerifyChainEnum.STOCK_VERIFY_CHAIN.name();
    }
}
