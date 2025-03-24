package org.example.infrastructure.persistent.po;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户日次数表
 */
@Data
public class RaffleActivityAccountDay {

    private static SimpleDateFormat DayFormat=new SimpleDateFormat("yyyy-MM-dd");

    /** 自增ID */
    private String id;
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 日期（yyyy-mm-dd） */
    private String day;
    /** 日次数 */
    private Integer dayCount;
    /** 日次数-剩余 */
    private Integer dayCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

    public static String currentDay(){
       return DayFormat.format(new Date());
    }

}
