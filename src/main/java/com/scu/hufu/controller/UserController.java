package com.scu.hufu.controller;

import com.google.common.reflect.TypeToken;
import com.scu.hufu.bean.Response;
import com.scu.hufu.bean.User;
import com.scu.hufu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by tianfei on 2017/4/6.
 */
@RestController
@RequestMapping(value = "/session")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping(value = "/login")
    public Object loginWithPassword(@RequestBody String json){



        Response<User> response=new Response<>();
        //检索数据库
        //userRepository.findOne()；

        //Type type=new TypeToken<List<User>>(){}.getType();

        //Response<type> re=new Response<type>();


        return null;
    }

    @PostMapping(value = "/token")
    public Object tokenLogin(@RequestBody String json){



        Response<User> response=new Response<>();
        //检索数据库
        //userRepository.findOne()；

        //Type type=new TypeToken<List<User>>(){}.getType();

        //Response<type> re=new Response<type>();


        return null;
    }


    @PostMapping(value = "/test")
    public Object testAll(){

        Response<List> response=new Response<>();

        List<User> mList=userRepository.findAll();
        //checkNotNull(mList);

        response.setCode(200);
        response.setMessage("success");
        response.setData(mList);

        return response;
    }


}
