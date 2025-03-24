package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 判断是否完成抽奖请求接口的请求参数
 * @date 2025/3/2 14:13
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IsCalendarSignRebateDTO {
    private String userId;
}
