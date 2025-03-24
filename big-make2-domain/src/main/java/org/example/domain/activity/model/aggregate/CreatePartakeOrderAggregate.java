package org.example.domain.activity.model.aggregate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.RaffleActivityAccountDayEntity;
import org.example.domain.activity.model.entity.RaffleActivityAccountMonthEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.strategy.model.entity.ActivityAccountEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/25 19:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {
    private String userId;
    private Long activityId;

    private boolean isExistAccountDay=true;
    /**
     * 月额度
     */
    private RaffleActivityAccountDayEntity activityAccountDayEntity;

    private boolean isExistAccountMouth=true;
    /**
     * 日额度
     */
    private RaffleActivityAccountMonthEntity activityAccountMonthEntity;

    /**
     * 总额度
     */
    private ActivityAccountEntity activityAccountEntity;


    /**
     * 抽奖单 实体
     */
    private UserRaffleOrderEntity userRaffleOrder;
}
