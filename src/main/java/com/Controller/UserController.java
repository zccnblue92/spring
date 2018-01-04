package com.Controller;

import com.Vo.User;

import com.commmon.util.RedisCache;
import com.commmon.util.SerializerUtil;
/*import com.commmon.util.TokenManager;
import com.commmon.util.TokenModel;*/
import com.service.Userservice;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zallhy on 2017/9/11.
 */
@Controller
public class UserController {

    @Resource
    private Userservice userservice;
    @Resource
    private RedisCache redisCache;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisCache tokenManager;

    @Authorization("需token")
    @RequestMapping(value = "/index",method = {RequestMethod.GET,RequestMethod.POST})
    public String index(){

        User user=userservice.getUserByid(1);
       //List li=redisCache.getCache("API_STAT");
        /*List li=userservice.getlist();
        redisTemplate.opsForList().leftPush("list",li);*/
        //TokenModel model = tokenManager.createToken(user.getId());
       /* ListOperations<String, Object> list = redisTemplate.opsForList();
        List userList=list.range("AUTHORIZATION",0,0);*/
        return "indexDome";
    }

    @Authorization("需token")
    @RequestMapping(value = "/indexsec",method = {RequestMethod.GET,RequestMethod.POST})
    public String indexsec(){
        User user=userservice.getUserByid(3);
        Map li=new HashMap();
        li.put("user",user);
       // redisCache.putCache("user",user);
        System.out.print(user);
        return "indexDome";
    }

}
