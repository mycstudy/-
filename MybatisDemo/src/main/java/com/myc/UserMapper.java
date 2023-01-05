package com.myc;

import com.mybatis.Param;
import com.mybatis.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from user where name=#{name} and age=#{age}")
    List<User> getUserByName(@Param("name") String name,@Param("age")Integer age);

    @Select("select * from user where id=#{id}")
    User getUserById(@Param("id") int id);
}
