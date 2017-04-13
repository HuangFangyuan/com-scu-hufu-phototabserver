package com.scu.hufu.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.scu.hufu.bean.Response;
import com.scu.hufu.bean.Token;
import com.scu.hufu.bean.User;
import com.scu.hufu.enums.ResponseEnum;
import com.scu.hufu.exception.ServerExpection;
import com.scu.hufu.repository.UserRepository;
import com.scu.hufu.util.MD5;
import com.scu.hufu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Date;
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

    @Value("${serverSecret}")
    private String SERVER_SECRET;


    @PostMapping(value = "/user/login")
    public Object loginWithPassword (@RequestParam String email,@RequestParam String MD5pass){

        String _email=checkNotNull(email);
        String _password=checkNotNull(MD5pass);

        User user=userRepository.findByEmail(_email);

        if (user==null){
            throw new ServerExpection(ResponseEnum.USER_NOT_FOUND);
        }

        if (user.getPasswordMD5().equals(_password)){
            //成功后返回用户一个token（一组token）

            // Signature使用HS256

     /******************客户端必须在header的authorization字段中添加token**********************
     ******************authorization格式为： Authorization: Bearer 'token'*****************
     ******************                 Bearer必须存在                    *****************
     ******************     RFC 6750   OAuth 2.0 Bearer Token Usage      *****************
     */

            Algorithm algorithm = null;
            try {
                algorithm = Algorithm.HMAC256(SERVER_SECRET);
            } catch (UnsupportedEncodingException e) {
                throw new ServerExpection(ResponseEnum.UNKONW_ERROR);
            }

            //设置一天后过期
            Date exp=new Date(System.currentTimeMillis() + 24l*3600l*1000l);

            String token = JWT.create()
                    .withIssuer("tianff.net")
                    .withExpiresAt(exp)
                    .withClaim("uid",user.getId())
                    .sign(algorithm);


            //设置User的tokenValid为TRUE
            user.setTokenValid(true);
            if (userRepository.save(user) == null){
                throw  new ServerExpection(ResponseEnum.UNKONW_ERROR);
            }

            return ResponseUtil.success(new Token(token));
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
        user.setTokenValid(false);
        user.setPasswordMD5(_MD5pass);

        User select=userRepository.findByEmail(_email);
        if (select!=null){
            throw new ServerExpection(ResponseEnum.USER_EXISTED);
        }

        User save=userRepository.save(user);

        //返回的数据包括注册成功的User
        return ResponseUtil.success(save);
    }

    //更新用户信息
    @PostMapping(value = "/user/update")
    public Object updateUser(){

        return null;
    }

    //查看用户详细信息
    @PostMapping(value = "/user/info")
    public Object checkUserInfo(){


        return null;
    }

    //登出
    @PostMapping(value = "/user/logout")
    public Object logout(@RequestParam Integer uid){


        return  null;
    }

    //////////////////////////////////////////////////////////////////////////
    //////////为确保一致性，所有的token相关的操作的函数名一律为tokenXXX////////////
    //////////为确保一致性，所有的token相关的操作的映射路径一律为/token/XXX////////
    /////////////////////////////////////////////////////////////////////////

    /*
     客户端使用用户名跟密码请求登录
     服务端收到请求，去验证用户名与密码
     验证成功后，服务端会签发一个 Token，再把这个 Token 发送给客户端 （注意服务端并不保存这个token）
     客户端收到 Token 以后把它存储起来
     客户端每次向服务端请求资源的时候需要带着服务端签发的 Token

     ******************客户端必须在header的authorization字段中添加token**********************
     ******************authorization格式为： Authorization: Bearer 'token'*****************
     ******************                 Bearer必须存在                    *****************
     ******************     RFC 6750   OAuth 2.0 Bearer Token Usage      *****************

     服务端收到请求，然后去验证客户端请求里面带着的 Token，如果验证成功，就向客户端返回请求的数据
     */

    /*
    JWT token格式：

    header.payload.signature   中间用点分隔开，并且都使用 Base64 编码

    header 部分主要是两部分内容，一个是 Token 的类型，另一个是使用的算法，比如下面类型就是 JWT，使用的算法是 HS256。
    {
      "typ": "JWT",
      "alg": "HS256"
    }

    本服务器采用的payload格式为
    {
     "iss": "tianff.net",
     "exp": "1438955445",
     "uid": "123",
    }

    Signature
    先用 Base64 编码 header.payload ，再用加密算法加密一下，
    加密的时候要放进去一个 Secret ，这个密码秘密地存储在服务端。
     */

    @PostMapping(value = "/token/Login")
    public Object tokenLogin(@RequestBody String json){



        Response<User> response=new Response<>();
        //检索数据库
        //userRepository.findOne()；

        //Type type=new TypeToken<List<User>>(){}.getType();

        //Response<type> re=new Response<type>();


        return null;
    }

    @PostMapping(value = "/token/refreshToken")
    public Object tokenRefresh(){


        return null;
    }

}
