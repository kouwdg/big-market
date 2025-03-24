package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.persistent.po.DailyBehaviorRebate;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/21 10:41
 */
@Mapper
public interface IDailyBehaviorRebateDao {

    //根据行为类型 返利 对应的 配置信息
    List<DailyBehaviorRebate> queryDailyBehaviorRebate(@Param("behaviorType") String behaviorType);
}
