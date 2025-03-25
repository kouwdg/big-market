package org.example.domain.login.model.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/24 15:40
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVo {
    private String userName;
    private String password;
}
