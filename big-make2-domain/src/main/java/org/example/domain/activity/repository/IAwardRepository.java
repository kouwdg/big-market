package org.example.domain.activity.repository;


import org.example.domain.activity.model.aggregate.UserAwardRecordAggregate;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/26 14:39
 */
public interface IAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
}
