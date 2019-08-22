package site.abiao.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.abiao.demo.mapper.UserMapper;
import site.abiao.demo.model.User;


@Service
public class UserServiceImpl {
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void test1(){
        userMapper.insert(new User("A",10,"北邮"));
        userMapper.insert(new User("B",10,"北邮"));
        userMapper.insert(new User("C",10,"北邮"));
        userMapper.insert(new User("D",10,"北邮"));
        userMapper.insert(new User("E",10,"北邮"));
        userMapper.insert(new User("F",10,"北邮"));
        userMapper.insert(new User("G",10,"北邮"));
        userMapper.insert(new User("A",10,"北邮"));
    }
}
