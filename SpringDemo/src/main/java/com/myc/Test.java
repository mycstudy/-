package com.myc;

import com.myc.service.UserService;
import com.myc.service.UserServiceInterface;
import com.myc.service.UserServiceProto;
import com.spring.AnnotationConfigurationApplicationContext;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigurationApplicationContext applicationContext=new AnnotationConfigurationApplicationContext(AppConfig.class);

        UserServiceInterface bean = (UserServiceInterface) applicationContext.getBean("userService");
        bean.test();
    }
}
