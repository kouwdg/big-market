package com.cheng.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 获得抽奖列表接口的 请求数据对象
 * @date 2024/12/15 21:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListRequestDTO implements Serializable {
    private Long strategyId;
}
