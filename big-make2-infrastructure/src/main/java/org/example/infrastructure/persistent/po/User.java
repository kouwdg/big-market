package org.example.infrastructure.persistent.po;

import lombok.Data;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户表
 * @date 2025/3/24 11:26
 */
@Data
public class User {
    private Integer id;
    private String userId;
    private String password;
}
