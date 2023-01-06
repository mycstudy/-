package com.springboot;

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

public class SpringApplication {
    public static void run(Class clazz,String[] args){
        //创建一个spring容器-->往容器中注册配置类-->启动容器
        AnnotationConfigWebApplicationContext applicationContext=new AnnotationConfigWebApplicationContext();
        applicationContext.register(clazz);
        applicationContext.refresh();

        //启动Tomcat
        startTomcat(applicationContext);
    }

    private static void startTomcat(WebApplicationContext applicationContext) {
        Tomcat tomcat=new Tomcat();

        Server server=tomcat.getServer();
        Service service=server.findService("Tomcat");

        Connector connector=new Connector();
        connector.setPort(8081);

        Engine engine=new StandardEngine();
        engine.setDefaultHost("localhost");

        Host host=new StandardHost();
        host.setName("localhost");

        String contextPath="";
        Context context=new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        //DispatcherServlet入参接收WebApplicationContext，也就是接收一个spring容器
        //DispatcherServlet需要寻找某个controller下的某个方法是否匹配请求，而controller是个bean
        tomcat.addServlet(contextPath,"dispatcher", new DispatcherServlet(applicationContext));
        context.addServletMappingDecoded("/*","dispatcher");

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
