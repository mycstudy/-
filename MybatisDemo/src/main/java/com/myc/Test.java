package com.myc;

import com.mybatis.MapperProxyFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException {
        UserMapper mapper = MapperProxyFactory.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserByName("王五",15);
        User user = mapper.getUserById(2);
        System.out.println(userList);
        System.out.println(user);
    }
}
