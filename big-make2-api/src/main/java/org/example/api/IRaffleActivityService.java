package org.example.api;


import org.example.api.dto.*;
import org.example.types.model.Response;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖活动服务
 * @date 2024/12/26 16:55
 */
public interface IRaffleActivityService {



    /**
     * 抽奖活动装配
     * @param activityId
     * @return
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @return
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO requestDTO);

    /**
     * 每日签到返利接口
     * @param userId 用户Id
     * @return 签到是否成功
     */
    Response<Boolean> calendarSignRebate(calendarSignRebateDTO userId);

    /**
     * 判断日历签到接口是否已经使用
     */
    Response<Boolean> isCalendarSignRebate(IsCalendarSignRebateDTO request);


    /**
     *
     *查询账户额度
     * @return
     */
    Response<UserActivityAccountResponseDTO>queryUserActivityAccount(UserActivityAccountRequestDTO requestDTO);

    Response<Integer> UsedRaffleCount(String userId,Long activityId);



}
