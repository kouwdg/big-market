package com.cheng.domain.award.repository;

import com.cheng.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 14:39
 */
public interface IAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
}
