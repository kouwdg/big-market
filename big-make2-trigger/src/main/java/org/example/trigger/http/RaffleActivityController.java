package org.example.trigger.http;


import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.api.IRaffleActivityService;
import org.example.api.dto.*;
import org.example.domain.RechargeDraw.Model.vo.RechargeDrawVo;
import org.example.domain.RechargeDraw.Service.IRechargeDrawService;
import org.example.domain.activity.model.entity.UserAwardRecordEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.activity.model.vo.UserAwardRecordStateVo;
import org.example.domain.activity.service.armory.IActivityArmory;
import org.example.domain.activity.service.awardOrder.IAwardService;
import org.example.domain.activity.service.order.IRaffleActivityPartakeService;
import org.example.domain.activity.service.quota.IRaffleActivityAccountQuota;

import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.vo.BehaviorTypeVo;
import org.example.domain.rebate.service.IBehaviorRebateService;
import org.example.domain.strategy.model.entity.ActivityAccountEntity;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.service.armory.IStrategyArmory;
import org.example.domain.strategy.service.raffle.IRaffleStrategy;
import org.example.domain.strategy.service.rule.IRaffleRule;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 抽奖活动服务
 * @date 2024/12/26 16:59
 */

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/raffle/activity")
public class RaffleActivityController implements IRaffleActivityService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IAwardService awardServices;
    @Resource
    private IActivityArmory activityArmory;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private IBehaviorRebateService behaviorRebateService;
    @Resource
    private IRaffleRule raffleRule;

    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Resource
    private IRaffleActivityAccountQuota raffleActivityAccountQuota;

    @Autowired
    @Qualifier("Recharge_draw_credit")
    private IRechargeDrawService rechargeDraw;


    /**
     * 活动装配

     * @param activityId
     * @return url:
     * http://localhost:8091/api/v1/raffle/activity/armory
     */
    @RequestMapping(value = "armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> armory(@RequestParam Long activityId) {

        try {
            activityArmory.redisFlush();

            log.info("活动装配,数据预热,开始 activityId:{}", activityId);
            //1 活动装配
            activityArmory.assembleActivitySkuByActivityId(activityId);
            //2 策略装配
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
            log.info("活动装配,数据预热,成功 activityId:{}", activityId);
            return response;
        } catch (Exception e) {
            log.error("活动装配,数据预热,失败 activityId:{}", activityId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(false)
                    .build();
        }
    }

    /**
     * 抽奖活动接口
     *
     * @param requestDTO
     * @return url :
     * http://localhost:8091/api/v1/raffle/activity/draw
     * /api/v1/raffle/activity/draw
     */
    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO requestDTO) {
        try {
            log.info("活动抽奖开始 userId{} activity:{}", requestDTO.getUserId(), requestDTO.getActivityId());

            //1 参数校验
            if (StringUtils.isBlank(requestDTO.getUserId()) || requestDTO.getActivityId() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            //2 参与活动 - 创建抽奖记录订单
            UserRaffleOrderEntity order = raffleActivityPartakeService.createOrder(requestDTO.getUserId(), requestDTO.getActivityId());
            //3 执行抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.preformRaffle(RaffleFactorEntity.builder()
                    .userId(order.getUserId())
                    .strategyId(order.getStrategyId())
                    .build());
            //4 存放结果 - 写入中奖记录
            UserAwardRecordEntity build = UserAwardRecordEntity.builder()
                    .userId(order.getUserId())
                    .activityId(order.getActivityId())
                    .strategyId(order.getStrategyId())
                    .orderId(order.getOrderId())
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardTime(new Date())
                    .awardState(UserAwardRecordStateVo.create)
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .build();
            awardServices.saveUserAwardRecord(build);
            //5 返回结果
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .build())
                    .build();

        } catch (AppException e) {
            log.error("抽奖活动失败 userId:{} activityId:{}", requestDTO.getUserId(), requestDTO.getActivityId());
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("抽奖活动失败 userId:{} activityId:{}", requestDTO.getUserId(), requestDTO.getActivityId());
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }


    /**
     * 每日签到返利接口
     *  http://localhost:8091/api/v1/raffle/activity/calendar_sign_rebate
     * @param requestDto 用户Id
     * @return 签到是否成功
     */
    @RequestMapping(value = "calendar_sign_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> calendarSignRebate(@RequestBody calendarSignRebateDTO requestDto) {
        try {
            log.info("日历签到返利开始: userId：{}", requestDto.getUserId());

            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(requestDto.getUserId());
            behaviorEntity.setBehaviorTypeVo(BehaviorTypeVo.SIGN);
            behaviorEntity.setOutBusinessNo(dateFormat.format(new Date()));

            List<String> orderList = behaviorRebateService.createOrder(behaviorEntity);
            log.info("日历签到返利完成: userId：{} orders:{}", requestDto.getUserId(), JSON.toJSONString(orderList));
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (AppException e) {
            log.error(e.getInfo());
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getMessage())
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
    /**
     * 判断日历签到接口是否已经使用
     * http://localhost:8091/api/v1/raffle/activity/is_calendar_sign_rebate
     */

    @RequestMapping(value = "is_calendar_sign_rebate",method = RequestMethod.POST)
    @Override
    public Response<Boolean> isCalendarSignRebate(@RequestBody IsCalendarSignRebateDTO requestDTO) {

        try {
            log.info("查询用户是否完成日历签到返利 开始: user:{}",requestDTO.getUserId());
            String outBusinessNo = dateFormat.format(new Date());
            List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = behaviorRebateService.queryOrderByBusinessNo(requestDTO.getUserId(), outBusinessNo);
            log.info("查询用户是否完成日历签到返利 完成: user:{}",requestDTO.getUserId());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(behaviorRebateOrderEntities!=null)
                    .build();

        }catch (Exception e){
            log.info("查询用户是否完成日历签到返利 失败: user:{}",requestDTO.getUserId());
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();

        }
    }

    /**
     * 查询用户额度
     * @param requestDTO
     *@return
     * http://localhost:8080/api/v1/raffle/activity/query_user_activity_account
     */
    @RequestMapping(value = "query_user_activity_account",method = RequestMethod.POST)
    @Override
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO requestDTO) {
        try {
            log.error("查询用户账户额度开始");
            raffleActivityAccountQuota.createUser(requestDTO.getUserId(),requestDTO.getActivityId());

            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuota.queryActivityAccountEntity(requestDTO.getActivityId(), requestDTO.getUserId());;
            UserActivityAccountResponseDTO result = UserActivityAccountResponseDTO.builder()
                    .totalCount(activityAccountEntity.getTotalCount())
                    .totalCountSurplus(activityAccountEntity.getTotalCountSurplus())
                    .dayCount(activityAccountEntity.getDayCount())
                    .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                    .monthCount(activityAccountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                    .build();
            log.error("查询用户账户额度成功");
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result)
                    .build();



        }catch (Exception e){
            log.error("查询用户账户额度失败");
            log.error(e.getMessage());
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();
        }


    }

    @Override
    public Response<Integer> UsedRaffleCount(String userId, Long activityId) {
        return null;
    }

    //使用积分兑换抽奖次数
    //http://localhost:8091/api/v1/raffle/activity/rechargeDrawCredit?userId=cheng&activityId=101&count=2
    @GetMapping("rechargeDrawCredit")
    @Override
    public Response<Boolean> RechargeDrawCredit(@RequestParam("userId") String userId,
                                                @RequestParam("activityId") Long activityId,
                                                @RequestParam("count") Integer count) {
        RechargeDrawVo build = RechargeDrawVo.builder()
                .sku(101L)
                .activity(activityId)
                .userId(userId)
                .count(count).build();
        rechargeDraw.RechargeDraw(build);
        return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(true)
                .build();
    }

    @RequestMapping(value = "used_Raffle_Count",method = RequestMethod.POST)
    public Response<Integer> UsedRaffleCount(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        Integer count = activityArmory.getUsedRaffleCount(requestDTO.getUserId(), requestDTO.getActivityId());
        return Response.<Integer>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(count)
                .build();
    }


}
