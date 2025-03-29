package org.example.domain.credit.repository;

import org.example.domain.credit.model.aggregate.TradeAggregate;

/**
 * @author 程宇乐
 * @version 1.0
 * @description:
 * @date 2025/3/27 17:09
 */
public interface ICreditRepository {
    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);
}
