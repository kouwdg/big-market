package org.example.api;

import org.example.api.dto.UserAccountRequest;
import org.example.types.model.Response;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户的的登录接口
 * @date 2025/3/24 11:19
 */
public interface IRaffleLogin {

    //登录
    Response<String> login( UserAccountRequest userAccountRequest) throws Exception;

    //退出登录
    Response<Boolean>QuitLogin(String userName);

    //注册
    Response<Boolean> register();

}
