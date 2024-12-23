package com.cheng.domain.activity.service.rule.chain;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/23 14:25
 */
public abstract class AbstractActionChain implements IActionChain{
    private IActionChain next;

    public IActionChain next(){
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next=next;
        return next;
    }

}
