package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.RechargeDraw.Model.vo.RechargeDrawVo;
import org.example.domain.RechargeDraw.Service.IRechargeDrawService;
import org.example.domain.login.model.Vo.UserRegisterVo;
import org.example.domain.login.service.ILoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/2/11 22:58
 */
@SpringBootTest
@Slf4j
public class Test1 {
    @Autowired
    @Qualifier("Recharge_draw_credit")
    private IRechargeDrawService rechargeDraw;

    @Autowired
    @Qualifier("Recharge_draw_sign_in")
    private IRechargeDrawService rechargeDrawSignIn;

    @Autowired
    private ILoginService loginService;

    @Test
    public void test1() {
        RechargeDrawVo tem = new RechargeDrawVo();
        tem.setUserId("cheng");
        tem.setCount(2);
        tem.setSku(101L);
        tem.setActivity(101L);
        tem.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
        rechargeDrawSignIn.RechargeDraw(tem);
    }
    @Test
    public void test3() {
        RechargeDrawVo tem = new RechargeDrawVo();
        tem.setUserId("cheng");
        tem.setCount(2);
        tem.setSku(101L);
        tem.setActivity(101L);
        tem.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
        rechargeDraw.RechargeDraw(tem);
    }

    @Test
    public void test2() {
        UserRegisterVo tem = new UserRegisterVo();
        tem.setPassword("12345");
        tem.setUserName("cheng");

        loginService.register(tem);
    }
}
