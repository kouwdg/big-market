package org.example.domain.login.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.types.common.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/25 11:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountEntity {
    private Integer id;
    private String userId;
    private String password;
    private String userName;
    //用户角色集合
    private String roles;


    //返回RoleList
    public List<String> getRoleList(){
        if (roles==null|| StringUtils.isBlank(roles)){
            return null;
        }
        String[] split = roles.split(Constants.SPLIT);

        return new ArrayList<>(Arrays.asList(split));
    }
}
