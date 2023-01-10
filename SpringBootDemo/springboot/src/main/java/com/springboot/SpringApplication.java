package com.springboot;

import com.springboot.webServer.WebServer;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.Map;

public class SpringApplication {
    public static void run(Class clazz,String[] args){
        //创建一个spring容器-->往容器中注册配置类-->启动容器
        AnnotationConfigWebApplicationContext applicationContext=new AnnotationConfigWebApplicationContext();
        applicationContext.register(clazz);
        applicationContext.refresh();

        //WebServer:多态
        WebServer webServer=getWebServer(applicationContext);
        webServer.start(applicationContext);
    }

    private static WebServer getWebServer(WebApplicationContext applicationContext) {
        Map<String, WebServer> webServerMap = applicationContext.getBeansOfType(WebServer.class);
        if (webServerMap.size()==0)
            throw new NullPointerException();
        if (webServerMap.size()>1)
            throw new IllegalStateException();

        return webServerMap.values().stream().findFirst().get();
    }

}
