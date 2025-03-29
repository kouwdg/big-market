package org.example.infrastructure.repository;

import org.example.domain.credit.model.aggregate.TradeAggregate;
import org.example.domain.credit.model.entity.CreditAccountEntity;
import org.example.domain.credit.model.entity.CreditOrderEntity;
import org.example.domain.credit.repository.ICreditRepository;
import org.example.infrastructure.persistent.dao.IUserCreditAccountDao;
import org.example.infrastructure.persistent.dao.IUserCreditOrderDao;
import org.example.infrastructure.persistent.po.UserCreditAccount;
import org.example.infrastructure.persistent.po.UserCreditOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/27 17:11
 */
@Service
public class CreditRepository implements ICreditRepository {


    @Resource
    private IUserCreditOrderDao userCreditOrderDao;

    @Resource
    private IUserCreditAccountDao userCreditAccountDao;

    @Transactional
    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        // 1 保存积分订单表
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        UserCreditOrder userCreditOrder = UserCreditOrder.builder()
                .userId(creditOrderEntity.getUserId())
                .orderId(creditOrderEntity.getOrderId())
                .tradeAmount(creditOrderEntity.getTradeAmount())
                .tradeName(creditOrderEntity.getTradeName())
                .tradeType(creditOrderEntity.getTradeType())
                .outBusinessNo(creditOrderEntity.getOutBusinessNo())
                .build();
        userCreditOrderDao.insert(userCreditOrder);
        //2 修改用户积分数据
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        userCreditAccountDao.adjustAmount(creditAccountEntity);




    }
}
