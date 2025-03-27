package org.example.domain.activity.repository;


import org.example.domain.activity.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.award.model.aggregate.GiveOutPrizesAggregate;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 14:39
 */
public interface IAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    //查询奖品配置信息
    String queryAwardConfig(Integer awardId);

    void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate);

    //查询奖品的key
    String queryAwardKey(Integer awardId);
}
