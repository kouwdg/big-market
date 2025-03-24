package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 每日签到接口 请求参数
 * @date 2025/3/2 14:16
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class calendarSignRebateDTO {

    private String userId;
}
