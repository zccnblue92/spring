package com.service;

import com.Vo.User;

import java.util.List;

/**
 * Created by zallhy on 2017/9/11.
 */
public interface Userservice {

    User getUserByid(Integer id);

    List<User> getlist();

}
