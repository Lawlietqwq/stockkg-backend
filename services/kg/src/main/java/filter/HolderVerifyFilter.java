package filter;

import com.example.kg.common.enums.VerifyChainEnum;
import com.example.kg.dto.HolderQueryDTO;
import com.example.patterns.chain.AbstractChainHandler;

public interface HolderVerifyFilter <T extends HolderQueryDTO> extends AbstractChainHandler<HolderQueryDTO> {
    @Override
    default String mark() {
        return VerifyChainEnum.HOLDER_VERIFY_CHAIN.name();
    }
}
