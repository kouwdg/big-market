package org.example.api;


import org.example.api.dto.*;
import org.example.types.model.Response;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖服务的接口
 * @date 2024/12/15 21:10
 */
public interface IRaffleService {

    /**
     *策略装配接口
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 获取 奖品列表的接口
     * @param requestDTO 请求参数
     * @return 奖品的列表数据
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     * 抽奖接口
     * @param requestDTO
     * @return
     */
    Response<RaffleResponseDTO> RandomRaffle(RaffleRequestDTO requestDTO);

    Response<List<RaffleStrategyRuleWeightResponseDTO>>queryRaffleStrategyRuleWeight(RaffleStrategyRuleWeightRequestDTO requestDTO);
}
