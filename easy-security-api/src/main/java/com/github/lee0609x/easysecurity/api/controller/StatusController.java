package com.github.lee0609x.easysecurity.api.controller;

import com.github.lee0609x.easysecurity.model.ResponseBody;
import com.github.lee0609x.easysecurity.model.ResponseBodyStatus;
import com.github.lee0609x.easysecurity.util.ResponseBodyUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Lee0609x
 * Date:2020/5/23
 */
@RestController
@RequestMapping("/easy-security/status")
public class StatusController {

    @GetMapping("/401")
    public ResponseBody<String> needLogin(HttpServletResponse response) throws IOException {
        return ResponseBodyUtil.errorResponse(ResponseBodyStatus.HTTP401, null);
    }

    @GetMapping("/403")
    public ResponseBody<String> needResource(HttpServletResponse response) throws IOException {
        return ResponseBodyUtil.errorResponse(ResponseBodyStatus.HTTP403, null);
    }

}
