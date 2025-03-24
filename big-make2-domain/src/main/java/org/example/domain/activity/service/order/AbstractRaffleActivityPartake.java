package org.example.domain.activity.service.order;


import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.activity.model.vo.ActivityStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/25 18:35
 */
@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {

    @Resource
    protected IActivityRepository activityRepository;

    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity entity) {

        String userId = entity.getUserId();
        Long activityId = entity.getActivityId();
        Date currentDate = new Date();

        //1 查询活动
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

        //2-1 验证活动有效期
        if (!ActivityStateVO.open.getCode().equals(activityEntity.getState().getCode())) {
            throw new AppException(ResponseCode.ACTIVITY_STATUS_OPEN.getCode(), ResponseCode.ACTIVITY_STATUS_OPEN.getInfo());
        }
        //2-2 校验活动时间
        Date date = new Date();
        if (activityEntity.getBeginDateTime().after(date)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        System.out.println(activityEntity.getEndDateTime());
        if (activityEntity.getEndDateTime().before(date)){
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }

        //3 查询是否有未使用的订单
        UserRaffleOrderEntity userRaffleOrder = activityRepository.queryNoUseRaffleOrder(entity);
        if (userRaffleOrder != null) {
            log.info("创建抽奖活动订单[已存在未消费的订单]");
            return userRaffleOrder;
        }

        //4 账户额度过滤(查询用户账户额度是否足够)
        CreatePartakeOrderAggregate orderAggregate = this.doFilterAccount(userId, activityId, currentDate);

        //5 构建订单实体对象
        userRaffleOrder = this.buildUserRaffleOrder(userId, activityId, currentDate);

        //6 为聚合对象设置 抽奖单
        orderAggregate.setUserRaffleOrder(userRaffleOrder);

        //7 保存聚合对象
        activityRepository.saveCreatePartakeOrderAggregate(orderAggregate);

        return userRaffleOrder;
    }

    @Override
    public UserRaffleOrderEntity createOrder(String userId, Long activityId) {
        PartakeRaffleActivityEntity entity = new PartakeRaffleActivityEntity();
        entity.setUserId(userId);
        entity.setActivityId(activityId);
        return createOrder(entity);
    }

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);

    protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate);

}
