package com.cheng.domain.activity.service.partake;

import com.cheng.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.cheng.domain.activity.model.entity.ActivityEntity;
import com.cheng.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.cheng.domain.activity.model.entity.UserRaffleOrderEntity;
import com.cheng.domain.activity.model.vo.ActivityStateVO;
import com.cheng.domain.activity.repository.IActivityRepository;
import com.cheng.domain.activity.service.IRaffleActivityPartakeService;
import com.cheng.types.enums.ResponseCode;
import com.cheng.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/25 18:35
 */
@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {
    //仓储接口
    protected final IActivityRepository activityRepository;
    public AbstractRaffleActivityPartake(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {

        String userId= partakeRaffleActivityEntity.getUserId();
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        Date currentDate = new Date();

        //1 活动查询
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

            //1-1 校验活动状态
        if(!ActivityStateVO.open.getCode().equals(activityEntity.getState().getCode())){
            throw new AppException(ResponseCode.ACTIVITY_STATUS_OPEN.getCode(),ResponseCode.ACTIVITY_STATUS_OPEN.getInfo());
        }
            //1-2 校验活动时间
        Date date = new Date();
        if(activityEntity.getBeginDateTime().after(date)||activityEntity.getEndDateTime().before(date)){
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(),ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }


        //2 查询是否有未使用的订单
        UserRaffleOrderEntity userRaffleOrder=activityRepository.queryNoUseRaffleOrder(partakeRaffleActivityEntity);
        if (userRaffleOrder!=null){
            log.info("创建抽奖活动订单[已存在未消费的订单]");
            return userRaffleOrder;
        }

        //3 账户额度过滤
        CreatePartakeOrderAggregate orderAggregate=this.doFilterAccount(userId,activityId,currentDate);

        //4 构建订单
        userRaffleOrder=this.buildUserRaffleOrder(userId,activityId,currentDate);

        //5 为聚合对象设置 抽奖单
        orderAggregate.setUserRaffleOrder(userRaffleOrder);

        //6 保存聚合对象
         activityRepository.saveCreatePartakeOrderAggregate(orderAggregate);

        return userRaffleOrder;
    }


    protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate);

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);
}
