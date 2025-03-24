package org.example.domain.activity.service.order;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.RaffleActivityAccountDayEntity;
import org.example.domain.activity.model.entity.RaffleActivityAccountMonthEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.activity.model.vo.UserRaffleOrderStateVo;
import org.example.domain.strategy.model.entity.ActivityAccountEntity;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/25 19:12
 */
@Service
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake{
    private final SimpleDateFormat dateFormatMonth=new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {
        //1 查询账户的额度
        ActivityAccountEntity activityAccountEntity=activityRepository.queryActivityAccountByUserId(userId,activityId);

        //账户不存在
        if (activityAccountEntity==null){
            throw new AppException(ResponseCode.ACCOUNT_UN_EXIST.getCode(),ResponseCode.ACCOUNT_UN_EXIST.getInfo());
        }

        //额度判断 (已经存在账户，但是余额是0)
        if (activityAccountEntity!=null&&activityAccountEntity.getTotalCountSurplus()<=0){
            throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(),ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
        }

        String mouth=dateFormatMonth.format(currentDate);

        //查询月额度
        RaffleActivityAccountMonthEntity activityAccountMonthEntity=activityRepository.queryActivityAccountMouthByUserId(userId,activityId,mouth);

        //月额度不足
        if (activityAccountMonthEntity!=null && activityAccountMonthEntity.getMonthCountSurplus()<=0){
            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(),ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
        }
        boolean isExistMouth=activityAccountMonthEntity!=null;
        //月账户不存在
        if (activityAccountMonthEntity==null){
            //创建月账户
            activityAccountMonthEntity = new RaffleActivityAccountMonthEntity();
            activityAccountMonthEntity.setUserId(userId);
            activityAccountMonthEntity.setActivityId(activityId);
            activityAccountMonthEntity.setMonth(mouth);
            activityAccountMonthEntity.setMonthCount(activityAccountEntity.getMonthCount());
            activityAccountMonthEntity.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
            activityAccountMonthEntity.setCreateTime(new Date());
            activityAccountMonthEntity.setUpdateTime(new Date());
        }

        String day = dateFormatDay.format(currentDate);
        //查询日额度
        RaffleActivityAccountDayEntity activityAccountDay=activityRepository.queryActivityAccountDayByUserId(userId,activityId,day);

        //日额度不足
        if (activityAccountDay!=null && activityAccountDay.getDayCountSurplus()<=0){
            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(),ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
        }
        boolean isExistDay=activityAccountDay!=null;
        //日额度不存在
        if (activityAccountDay==null){
            //创建日账户
            activityAccountDay = new RaffleActivityAccountDayEntity();
            activityAccountDay.setUserId(userId);
            activityAccountDay.setActivityId(activityId);
            activityAccountDay.setDay(day);
            activityAccountDay.setDayCount(activityAccountEntity.getDayCount());
            activityAccountDay.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
            activityAccountDay.setCreateTime(new Date());
            activityAccountDay.setUpdateTime(new Date());
        }

        //返回聚合
        CreatePartakeOrderAggregate orderAggregate = new CreatePartakeOrderAggregate();
        orderAggregate.setUserId(userId);
        orderAggregate.setActivityId(activityId);
        orderAggregate.setActivityAccountEntity(activityAccountEntity);
        orderAggregate.setActivityAccountMonthEntity(activityAccountMonthEntity);
        orderAggregate.setActivityAccountDayEntity(activityAccountDay);
        orderAggregate.setExistAccountDay(isExistDay);
        orderAggregate.setExistAccountMouth(isExistMouth);

        return orderAggregate;
    }


    //构建订单
    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate) {
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

        UserRaffleOrderEntity orderEntity = new UserRaffleOrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setOrderState(UserRaffleOrderStateVo.create.getCode());
        orderEntity.setOrderTime(new Date());
        orderEntity.setOrderId(RandomStringUtils.randomNumeric(11));
        orderEntity.setActivityId(activityId);
        orderEntity.setActivityName(activityEntity.getActivityName());
        orderEntity.setStrategyId(activityEntity.getStrategyId());
        return orderEntity;
    }
}
