package org.example.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.example.api.IRaffleLogin;
import org.example.api.dto.UserAccountRequest;
import org.example.domain.login.model.Vo.UserLoginVo;
import org.example.domain.login.service.ILoginService;
import org.example.types.enums.ResponseCode;
import org.example.types.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.WebEndpoint;


/**
 * 登录接口
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/raffle/Login")
public class RaffleLogin implements IRaffleLogin {

    @Autowired
    private ILoginService loginService;

    //http://localhost:8091/api/v1/raffle/Login/login
    @PostMapping("login")
    @Override
    public Response<String> login(@RequestBody UserAccountRequest userAccountRequest) throws Exception {
        String jwt = loginService.login(UserLoginVo.builder()
                .userName(userAccountRequest.getUserName())
                .password(userAccountRequest.getPassword())
                .build());
        return Response.<String>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(jwt)
                .build();
    }

    //退出登录
    //http://localhost:8091/api/v1/raffle/Login/quitLogin
    @GetMapping("quitLogin")
    @Override
    public Response<Boolean> QuitLogin(String userName) {
        Boolean result = loginService.quitLogin(userName);
        return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(result)
                .build();
    }

    @Override
    public Response<Boolean> register() {
        return null;
    }

    //http://localhost:8091/api/v1/raffle/Login/test1
    @GetMapping("test1")
    public String test(){
        return "a";
    }
}
