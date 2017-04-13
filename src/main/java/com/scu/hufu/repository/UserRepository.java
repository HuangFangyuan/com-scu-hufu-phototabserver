package com.scu.hufu.repository;

import com.scu.hufu.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tianfei on 2017/4/6.
 */

//简单的增删查改已经通过继承默认实现
public interface UserRepository extends JpaRepository<User,Integer>{

    //若要自定义某些查询功能，按如下规则定义接口方法即可
    //public List<User> findByXXX();

    User findByEmail(String email);

    //boolean existUser(String email);

}
