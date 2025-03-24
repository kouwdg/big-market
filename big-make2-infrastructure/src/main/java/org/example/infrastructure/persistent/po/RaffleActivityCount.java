package org.example.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2024/12/19 11:12
 */
@Data
public class RaffleActivityCount {
    //自增ID
private Long id;
//活动次数编号
private Long activityCountId;
//总次数
private Integer totalCount;
//日次数
private Integer dayCount;
//月次数
private Integer monthCount;
//创建时间
private Date createTime;
//更新时间
private Date updateTime;
}
