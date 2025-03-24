package org.example.domain.rebate.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.rebate.model.vo.BehaviorTypeVo;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 行为实体
 * @date 2024/12/30 18:41
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    private String userId;

    private BehaviorTypeVo behaviorTypeVo;

    //唯一订单号，防重
    private String outBusinessNo;
}
