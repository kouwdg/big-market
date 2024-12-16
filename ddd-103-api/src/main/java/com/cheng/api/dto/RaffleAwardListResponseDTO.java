package com.cheng.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 获得抽奖列表接口的 响应数据的对象
 * @date 2024/12/15 21:17
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseDTO {

    //奖品ID
    private Integer awardId;
    //奖品标题
    private String awardTitle;
    //奖品的副标题
    private String awardSubtitle;
    //排序编号
    private Integer sort;
}
