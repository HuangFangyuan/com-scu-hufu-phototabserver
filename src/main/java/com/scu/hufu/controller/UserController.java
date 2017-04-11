package com.scu.hufu.controller;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.scu.hufu.bean.Response;
import com.scu.hufu.bean.User;
import com.scu.hufu.enums.ResponseEnum;
import com.scu.hufu.exception.ServerExpection;
import com.scu.hufu.repository.UserRepository;
import com.scu.hufu.util.MD5;
import com.scu.hufu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.List;
import static com.scu.hufu.util.Predictions.checkNotNull;

/**
 * Created by tianfei on 2017/4/6.
 */
@RestController
@RequestMapping(value = "/session")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping(value = "/user/login")
    public Object loginWithPassword(@RequestParam String email,@RequestParam String MD5pass){

        String _email=checkNotNull(email);
        String _password=checkNotNull(MD5pass);

        User user=userRepository.findByEmail(_email);

        if (user==null){
            throw new ServerExpection(ResponseEnum.USER_NOT_FOUND);
        }

        if (user.getPasswordMD5().equals(_password)){
            Response<User> response =ResponseUtil.success(user);
            return response;
        }else {
            return ResponseUtil.error(ResponseEnum.PASSWORD_ERROR);
        }
    }

    @PostMapping(value = "/user/create")
    public Object register(@RequestParam String email,@RequestParam String MD5pass,@RequestParam String name){

        String _email=checkNotNull(email,ResponseEnum.REGISTER_ERR);
        String _MD5pass=checkNotNull(MD5pass,ResponseEnum.REGISTER_ERR);
        String _name=checkNotNull(name,ResponseEnum.REGISTER_ERR);

        User user=new User();
        user.setEmail(_email);
        user.setName(_name);
        user.setPasswordMD5(_MD5pass);

        User select=userRepository.findByEmail(_email);

        if (select!=null){
            throw new ServerExpection(ResponseEnum.USER_EXISTED);
        }

        User save=userRepository.save(user);

        //返回的数据包括注册成功的User
        return ResponseUtil.success(save);
    }

    @PostMapping(value = "/user/update")
    public Object updateUser(){

        return null;
    }

    @PostMapping(value = "/tokenLogin")
    public Object tokenLogin(@RequestBody String json){



        Response<User> response=new Response<>();
        //检索数据库
        //userRepository.findOne()；

        //Type type=new TypeToken<List<User>>(){}.getType();

        //Response<type> re=new Response<type>();


        return null;
    }

    @PostMapping(value = "/refreshToken")
    public Object refreshToken(){


        return null;
    }

}
