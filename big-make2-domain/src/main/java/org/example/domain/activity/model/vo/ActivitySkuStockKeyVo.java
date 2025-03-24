package org.example.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/24 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitySkuStockKeyVo {
    private Long sku;
    private Long activityId;
}
