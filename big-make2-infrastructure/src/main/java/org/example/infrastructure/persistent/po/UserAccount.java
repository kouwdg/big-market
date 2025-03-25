package org.example.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.types.common.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户表
 * @date 2025/3/24 11:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements Serializable {
    private Integer id;
    private String userId;
    private String password;
    private String userName;
    //用户角色集合
    private String roles;


    //返回RoleList
    public List<String> getRoleList(){
        if (roles==null||StringUtils.isBlank(roles)){
            return null;
        }
        String[] split = roles.split(Constants.SPLIT);

        return new ArrayList<>(Arrays.asList(split));
    }
}























