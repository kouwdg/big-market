package org.example.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.example.api.IRaffleLogin;
import org.example.types.model.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录接口
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/raffle/login")
public class RaffleLogin implements IRaffleLogin {
    @Override
    public Response<Boolean> login() {
        return null;
    }

    @Override
    public Response<Boolean> register() {
        return null;
    }

    //http://localhost:8091/api/v1/raffle/login/test1
    @GetMapping("test1")
    public String test(){
        return "a";
    }
}
