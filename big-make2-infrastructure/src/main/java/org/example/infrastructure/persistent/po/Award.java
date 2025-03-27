package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 奖品实体表
 * @date 2025/3/26 16:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Award {
    private Long id;
    private Integer awardId;
    private String awardKey;
    private String awardConfig;
    private String awardDesc;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
}
