package com.myc.service;

import com.spring.Autowired;
import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.InitializingBean;

@Component("userService")
public class UserService implements BeanNameAware, InitializingBean,UserServiceInterface {
    @Autowired
    private OrderService orderService;

    private String beanName;
    private String comment;

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("调用afterPropertiesSet");
    }

    public void test(){
        System.out.println(orderService);
        System.out.println(beanName);
        System.out.println(comment);
    }
}
