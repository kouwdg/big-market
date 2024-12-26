package com.cheng.domain.award.service;

import com.cheng.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 保存奖品接口
 * @date 2024/12/26 10:58
 */
public interface IAwardService {


    /**
     * 保存中奖记录
     */
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);
}
