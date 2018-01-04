package com.Dao;

import com.Vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

        User get(Integer id);

        List<User> getlist();

}