package org.example.domain.credit.service;

import org.example.domain.credit.model.aggregate.TradeAggregate;
import org.example.domain.credit.model.entity.CreditAccountEntity;
import org.example.domain.credit.model.entity.CreditOrderEntity;
import org.example.domain.credit.model.entity.TradeEntity;
import org.example.domain.credit.repository.ICreditRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/27 17:11
 */
@Service
public class CreditAdjustService implements ICreditAdjustService {

    @Resource
    private ICreditRepository creditRepository;
    @Override
    public String creteOrder(TradeEntity entity) {

        //1 创建账户积分实体对象
        CreditAccountEntity creditAccount = TradeAggregate.CreateCreditAccountEntity
                (entity.getUserId(), entity.getAdjustAmount());

        //2 创建账户实体订单
        CreditOrderEntity creditOrderEntity = TradeAggregate.CreateCreditOrderEntity(
                entity.getUserId(),
                entity.getTradeName(),
                entity.getTradeType(),
                entity.getAdjustAmount(),
                entity.getOutBusinessNo());

        //构建聚合对象
        TradeAggregate tradeAggregate = TradeAggregate.builder()
                .userId(entity.getUserId())
                .creditAccountEntity(creditAccount)
                .creditOrderEntity(creditOrderEntity)
                .build();
        creditRepository.saveUserCreditTradeOrder(tradeAggregate);

        //返回订单号
        return creditOrderEntity.getOrderId();
    }
}
