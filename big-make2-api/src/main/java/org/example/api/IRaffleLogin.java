package org.example.api;

import org.example.types.model.Response;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 用户的的登录接口
 * @date 2025/3/24 11:19
 */
public interface IRaffleLogin {

    //登录
    Response<Boolean> login();

    //注册
    Response<Boolean> register();

}
