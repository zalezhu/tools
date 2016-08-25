package com.zale.shortlink.web.controller;

import com.zale.shortlink.redis.RedisCacheDao;
import com.zale.shortlink.util.SystemUtil;
import com.zale.shortlink.web.constants.WebConstants;
import com.zale.shortlink.web.dto.LoginModel;
import com.zale.shortlink.web.dto.LoginRst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by Zale on 16/8/24.
 */
@RestController
public class LoignController extends BaseController {
    @Autowired
    @Qualifier("redisCache")
    private RedisCacheDao redisCache;
    private static final String PASSWORD = "!QAZ3edc";
    private static final String USERNAME = "admin";
    @RequestMapping(value="/user/login",method = RequestMethod.POST)
    public ResponseEntity<LoginRst> login(@RequestBody LoginModel loginModel){
        LoginRst rst = new LoginRst();

        if(!USERNAME.equals(loginModel.getUserName())||!PASSWORD.equals(loginModel.getPassword())){
            rst.setError("登录名或密码错误");
        }else{
            String uuid = UUID.randomUUID().toString();
            redisCache.save(uuid,USERNAME);
            rst.setToken(uuid);
        }
        return getJSONResp(rst, HttpStatus.OK);
    }
    @RequestMapping(value="/user/logout",method = RequestMethod.DELETE)
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token = request.getHeader(WebConstants.TOKEN);
        redisCache.delete(token,String.class);
        return getJSONResp("成功登出", HttpStatus.OK);
    }
    @RequestMapping(value="/user/passcode/{username}",method = RequestMethod.GET)
    public ResponseEntity<String> passcode(@PathVariable String username){
        if(StringUtils.isEmpty(username)){
            throw new RuntimeException("用户名不能为空");
        }
        String passcode = SystemUtil.genRandomCode(6);
        redisCache.save(username,passcode.toUpperCase(),5*60L);
       return getJSONResp(passcode.toUpperCase(), HttpStatus.OK);

    }
}
