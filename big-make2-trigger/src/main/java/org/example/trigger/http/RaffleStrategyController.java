package org.example.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.api.IRaffleService;
import org.example.api.dto.*;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.vo.RuleWeightVo;
import org.example.domain.strategy.service.armory.IStrategyArmory;
import org.example.domain.strategy.service.award.IRaffleAward;
import org.example.domain.activity.service.quota.IRaffleActivityAccountQuota;
import org.example.domain.strategy.service.rule.IRaffleRule;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/18 10:58
 */

@RestController
@CrossOrigin
@RequestMapping("/api/v1/raffle/strategy")
@Slf4j
public class RaffleStrategyController implements IRaffleService {

    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IRaffleAward raffleAward;
    @Resource
    private IRaffleRule raffleRule;
    @Resource
    private IRaffleActivityAccountQuota raffleActivityAccountQuota;

    /**
     * 策略装配接口
     * http://localhost:8091/api/v1/raffle/strategy_armory?strategyId=100001
     * @param strategyId
     * @return
     */
    @RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(Long strategyId) {
        try {
            //怎么清空redis中所有的健

            log.info("抽奖策略装配接口开始--strategyId:{}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);

            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("策略装配接口完成");
            return response;
        } catch (Exception e) {
            log.info("抽奖策略装配接口失败--strategyId:{}", strategyId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    /**
     * 请求路径 http://localhost:8091/api/v1/raffle/strategy/query_raffle_award_list
     * @param requestDTO 请求参数
     * @return
     */
    @Override
    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        log.info("奖品列表查询接口开始--activity_id:{}", requestDTO.getActivityId());
        //1 参数校验
        if(StringUtils.isBlank(requestDTO.getUserId())||requestDTO.getActivityId()==null){
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                    .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                    .build();
        }
        //2 查询奖品列表
        List<StrategyAwardEntity> entityList = raffleAward.queryRaffleStrategyAwardListByActivityId(requestDTO.getActivityId());
        // 2-1 获得奖品列表中所有的treeIds
        String[] treeIds = entityList.stream()
                .map(StrategyAwardEntity::getRuleModels)
                .filter(ruleModel -> ruleModel != null && !ruleModel.isEmpty())
                .toArray(String[]::new);
        //2-2 根据treeIds 取出对应的value
        Map<String, Integer> ruleLockCountMap = raffleRule.queryAwardRuleLockCount(treeIds);

        //2-3 查询抽奖次数
        Integer count = raffleActivityAccountQuota.queryRaffleActivityAccountDayPartakeCount(requestDTO.getUserId(), requestDTO.getActivityId());
        log.info("抽奖次数：{}",count);

        List<RaffleAwardListResponseDTO> result = new ArrayList<>();

        for (StrategyAwardEntity entity : entityList) {
            Integer lockCount = ruleLockCountMap.get(entity.getRuleModels());
            RaffleAwardListResponseDTO build = RaffleAwardListResponseDTO.builder()
                    .awardId(entity.getAwardId())
                    .awardTitle(entity.getAwardTitle())
                    .awardSubtitle(entity.getAwardSubtitle())
                    .sort(entity.getSort())
                    .awardRuleLockCount(lockCount)
                    .isAwardUnLock(lockCount==null||count>=lockCount)
                    .waitUnLockCount(lockCount==null||lockCount<count?0:lockCount-count)
                    .build();
            result.add(build);
        }
        log.info("奖品列表查询接口成功--activityId:{}", requestDTO.getActivityId());
        return Response.<List<RaffleAwardListResponseDTO>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(result)
                .build();

    }

    /**
     * 抽奖接口 http://localhost:8091/api/v1/raffle/random_raffle
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "random_raffle",method = RequestMethod.POST)
    @Override
    public Response<RaffleResponseDTO> RandomRaffle(@RequestBody RaffleRequestDTO requestDTO) {
        try {
            log.info("调用抽奖接口");

        }catch (Exception e){
            log.info("获得随机抽奖奖品接口失败--strategyId:{}", requestDTO.getStrategyId());
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
        return null;
    }

    /**
     * 权重规则范围展示接口 告诉用户总计抽奖多少次后，可以必中指定范围奖品
     * http://localhost:8091/api/v1/raffle/strategy/query_raffle_strategy_rule_weight
     * @param requestDTO
     * @return
     */
    @RequestMapping(value = "query_raffle_strategy_rule_weight",method = RequestMethod.POST)
    @Override
    public Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(@RequestBody RaffleStrategyRuleWeightRequestDTO requestDTO) {
        try {
            log.info("查询 策略权重规则配置开始");
            //1 校验参数
            if (StringUtils.isBlank(requestDTO.getUserId())||requestDTO.getActivityId()==null){
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }

            //2 查询用户抽奖总次数
            Integer totalCount=raffleActivityAccountQuota.queryUserDrawTotalCount(requestDTO.getUserId(),requestDTO.getActivityId());

            //3 查询规则
            List<RuleWeightVo> ruleWeightVos = raffleRule.queryAwardRuleWeightByActivityId(requestDTO.getActivityId());

            List<RaffleStrategyRuleWeightResponseDTO> resultList = new ArrayList<>();
            for (RuleWeightVo ruleWeightVo : ruleWeightVos) {
                List<RaffleStrategyRuleWeightResponseDTO.StrategyAward> strategyAwards=new ArrayList<>();
                List<RuleWeightVo.Award> awardList = ruleWeightVo.getAwardList();
                for (RuleWeightVo.Award award : awardList) {
                    RaffleStrategyRuleWeightResponseDTO.StrategyAward strategyAward = new RaffleStrategyRuleWeightResponseDTO.StrategyAward();
                    strategyAward.setAwardId(award.getAwardId());
                    strategyAward.setAwardTitle(award.getAwardTitle());
                    strategyAwards.add(strategyAward);
                }

                RaffleStrategyRuleWeightResponseDTO result = new RaffleStrategyRuleWeightResponseDTO();
                result.setStrategyAwards(strategyAwards);
                result.setRuleWeightCount(ruleWeightVo.getWeight());
                result.setUserActivityAccountTotalUserCount(totalCount);
                resultList.add(result);
            }

            return Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(resultList)
                    .build();

        }catch (AppException e){
            return Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .data(null)
                    .build();
        }catch (Exception e){
            return Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

}
