package org.example.domain.strategy.service.rule.chain;

import org.example.domain.strategy.model.entity.RaffleAwardEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/15 12:27
 */
public abstract class AbstractLogicChain implements ILogicChain{


    private ILogicChain next;

    @Override
    public ILogicChain addNext(ILogicChain next) {
        this.next=next;
        return next;
    }
    @Override
    public ILogicChain Next() {
       return this.next;
    }

    protected abstract String ruleModel();
}
