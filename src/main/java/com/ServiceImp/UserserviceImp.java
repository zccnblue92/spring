package com.ServiceImp;

import com.Dao.UserDao;
import com.Vo.User;
import com.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zallhy on 2017/9/11.
 */
@Service
public class UserserviceImp implements Userservice {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByid(Integer id) {
        User user=userDao.get(id);
        return user;
    }

    @Override
    public List getlist() {
        List li=userDao.getlist();
        return li;
    }
}
