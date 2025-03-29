package org.example.domain.RechargeDraw.Service.impl;

import org.example.domain.RechargeDraw.Model.entity.AddUserDrawEntity;
import org.example.domain.RechargeDraw.Model.vo.RechargeDrawVo;
import org.example.domain.RechargeDraw.Reposition.IRechargeDrawReposition;
import org.example.domain.RechargeDraw.Service.IRechargeDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/28 16:59
 */
public abstract class AbstractRechargeDrawService implements IRechargeDrawService {
    @Autowired
    protected IRechargeDrawReposition rechargeDrawReposition;

    //更新抽奖次数订单表 为用户添加抽奖次数
    @Transactional
    public Boolean addUserDrawTimes(AddUserDrawEntity entity){
            //1 更新抽奖次数订单表
            rechargeDrawReposition.updateRaffleActivityOrderToCompleted(entity.getOrderId());

            //2 为用户添加抽奖次数
            rechargeDrawReposition.addRaffleCount(entity.getUserId(),entity.getActivityId(),entity.getCount());
            return true;
    }


    //兑换抽奖次数
    public abstract void RechargeDraw(RechargeDrawVo drawVo);
}
