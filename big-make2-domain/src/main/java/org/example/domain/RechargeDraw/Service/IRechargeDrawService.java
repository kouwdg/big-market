package org.example.domain.RechargeDraw.Service;

import org.example.domain.RechargeDraw.Model.entity.AddUserDrawEntity;
import org.example.domain.RechargeDraw.Model.vo.RechargeDrawVo;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 充值抽奖次数功能
 * @date 2025/3/28 11:30
 */
public interface IRechargeDrawService {

    //1 兑换抽奖次数
    void RechargeDraw(RechargeDrawVo drawVo);

    //2 更新抽奖次数订单表 为用户添加抽奖次数
    Boolean addUserDrawTimes(AddUserDrawEntity entity);
}
