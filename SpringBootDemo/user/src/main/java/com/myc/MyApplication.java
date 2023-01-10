package com.myc;

import com.springboot.SpringApplication;
import com.springboot.SpringBootApplication;
import com.springboot.autoConfiguration.WebServerAutoConfiguration;
import com.springboot.webServer.JettyWebServer;
import com.springboot.webServer.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@Import(WebServerAutoConfiguration.class)
public class MyApplication {
//    @Bean
//    public TomcatWebServer getWebServer(){
//        return new TomcatWebServer();
//    }
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class,args);
    }
}