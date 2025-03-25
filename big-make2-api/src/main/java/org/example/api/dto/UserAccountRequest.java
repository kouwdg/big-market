package org.example.api.dto;

import lombok.Data;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/24 16:23
 */
@Data
public class UserAccountRequest {
    private String userName;
    private String password;
}
