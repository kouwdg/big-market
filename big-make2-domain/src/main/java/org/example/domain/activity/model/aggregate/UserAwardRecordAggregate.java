package org.example.domain.activity.model.aggregate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.TaskEntity;
import org.example.domain.activity.model.entity.UserAwardRecordEntity;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户中奖 聚合
 * @date 2024/12/26 14:36
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {

    private UserAwardRecordEntity userAwardRecordEntity;
    private TaskEntity taskEntity;
}
