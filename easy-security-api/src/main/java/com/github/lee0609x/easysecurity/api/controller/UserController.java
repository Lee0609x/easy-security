package com.github.lee0609x.easysecurity.api.controller;

import com.github.lee0609x.easysecurity.model.ResponseBody;
import com.github.lee0609x.easysecurity.util.ResponseBodyUtil;
import com.github.lee0609x.easysecurity.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lee0609x
 * Date:2020/5/24
 */
@RestController
@RequestMapping("/easy-security/user")
public class UserController {

    /**
     * 获取用户登录状态
     * @return
     */
    @GetMapping("/status")
    public ResponseBody<Boolean> userStatus() {
        return ResponseBodyUtil.successResponse(SecurityUtil.getUserLoginStatus());
    }

}
