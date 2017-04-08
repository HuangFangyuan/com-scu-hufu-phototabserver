package com.scu.hufu.controller;

import com.scu.hufu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tianfei on 2017/4/6.
 */
@RestController
public class HelloController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping(value = "/sayHello")
    public Object sayHello(){

        return "Hello Spring Boot Server!";
    }




}
