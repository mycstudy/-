package com.springboot.webServer;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class TomcatWebServer implements WebServer{

    @Override
    public void start(WebApplicationContext applicationContext) {
        System.out.println("Tomcat");
        startTomcat(applicationContext);
    }

    private static void startTomcat(WebApplicationContext applicationContext) {
        //WebServer-->SpringBoot的关键部分之一
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

        //这是SpringMVC提供的功能，可以直接用
        //DispatcherServlet-->SpringBoot的关键部分之二
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
