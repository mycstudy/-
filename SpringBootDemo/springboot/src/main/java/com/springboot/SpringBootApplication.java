package com.springboot;

import com.springboot.autoConfiguration.WebServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@ComponentScan  //这个ComponentScan扫描的是SpringBootApplication这个注解修饰的类所在的包，即user.com.myc这个包，所以能把controller扫描进去
//Import需要导入所有的AutoConfiguration类，一个一个写是不可能的，需要AutoConfigurationImportSelector.class的帮助
@Import(WebServerAutoConfiguration.class)
public @interface SpringBootApplication {

}
