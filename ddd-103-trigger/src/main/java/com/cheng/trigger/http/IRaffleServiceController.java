package com.cheng.trigger.http;

import com.cheng.api.IRaffleService;
import com.cheng.api.dto.RaffleAwardListRequestDTO;
import com.cheng.api.dto.RaffleAwardListResponseDTO;
import com.cheng.api.dto.RaffleRequestDTO;
import com.cheng.api.dto.RaffleResponseDTO;
import com.cheng.domain.strategy.model.entity.RaffleAwardEntity;
import com.cheng.domain.strategy.model.entity.RaffleFactorEntity;
import com.cheng.domain.strategy.model.entity.StrategyAwardEntity;
import com.cheng.domain.strategy.service.IRaffleAward;
import com.cheng.domain.strategy.service.IRaffleStrategy;
import com.cheng.domain.strategy.service.armory.IStrategyArmory;
import com.cheng.types.enums.ResponseCode;
import com.cheng.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖服务
 * @date 2024/12/15 21:25
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/raffle/")
public class IRaffleServiceController implements IRaffleService {
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IRaffleAward raffleAward;
    @Resource
    private IRaffleStrategy raffleStrategy;


    /**
     * 策略装配接口 实现
     *
     * @param strategyId 策略ID
     * @return
     */
    @RequestMapping(value = "strategy_armory",method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(Long strategyId) {
        try {
            log.info("抽奖策略装配接口开始--strategyId:{}",strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);

            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("策略装配接口完成");
            return response;
        }catch (Exception e){
            log.info("抽奖策略装配接口失败--strategyId:{}",strategyId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    /**
     * 奖品列表查询接口 实现
     * @param requestDTO 请求参数
     * @return
     */
    @RequestMapping(value = "query_raffle_award_list",method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        try {
            log.info("奖品列表查询接口开始--strategyId:{}",requestDTO.getStrategyId());
            List<StrategyAwardEntity> entityList = raffleAward.queryRaffleStrategyAwardList(requestDTO.getStrategyId());
            List<RaffleAwardListResponseDTO> result=new ArrayList();
            for (StrategyAwardEntity entity : entityList) {
                RaffleAwardListResponseDTO build = RaffleAwardListResponseDTO.builder()
                        .awardId(entity.getAwardId())
                        .awardTitle(entity.getAwardTitle())
                        .awardSubtitle(entity.getAwardSubtitle())
                        .sort(entity.getSort())
                        .build();
                result.add(build);
            }
            log.info("奖品列表查询接口成功--strategyId:{}",requestDTO.getStrategyId());
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result)
                    .build();

        }catch (Exception e){
            log.info("获得抽奖列表接口失败--strategyId:{}",requestDTO.getStrategyId());
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();

        }
    }


    @RequestMapping(value = "random_raffle",method = RequestMethod.POST)
    @Override
    public Response<RaffleResponseDTO> RandomRaffle(@RequestBody RaffleRequestDTO requestDTO) {
        try {
            log.info("调用抽奖接口");
            //调用抽奖接口
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.preformRaffle(RaffleFactorEntity.builder()
                    .userId("system")
                    .strategyId(requestDTO.getStrategyId())
                    .build());
            log.info("调用抽奖成功");

            //封装返回结果
            Response<RaffleResponseDTO> result = Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("获得随机抽奖奖品接口完成--awardId:{} awardIndex:{}",raffleAwardEntity.getAwardId(),raffleAwardEntity.getSort());
            return result;

        }catch (Exception e){
            log.info("获得随机抽奖奖品接口失败--strategyId:{}",requestDTO.getStrategyId());
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
