package org.example.domain.strategy.service.stock;

import org.example.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/17 21:38
 */
@Service
public class RaffleStock implements IRaffleStock{
    @Resource
    private IStrategyRepository strategyRepository;
    @Override
    public StrategyAwardStockKeyVo takeQueryValue() throws InterruptedException {
        return strategyRepository.takeQueryValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategy, Integer award) {
        strategyRepository.updateStrategyAwardStock(strategy,award);
    }
}
