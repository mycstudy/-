package com.springboot.autoConfiguration;

import com.springboot.webServer.JettyWebServer;
import com.springboot.webServer.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

//WebServer自动配置类
@Configuration
public class WebServerAutoConfiguration {
    //生效-->提前把所有Bean都注册好，但是增加了“生效”的概念，定义了Bean生效的条件-->pom里面有哪个的依赖，就哪个Bean生效
    //依赖传递和依赖控制问 题-->因为user依赖了springboot，springboot因为要写webServer的start方法，所以依赖了Tomcat和Jetty，所以user根据
    //依赖传递，就会默认依赖Tomcat和Jetty，导致getWebServer报错，所以在pom.xml里面把<optional>选项设置为true，可以不把这个依赖传递给user

    @Bean
//    @Conditional(TomcatCondition.class)
    //条件注解优化
    @ConditionalOnClass("org.apache.catalina.startup.Tomcat") //解析这个注解并生成metaData对象
    public TomcatWebServer getTomcatWebServer(){
        return new TomcatWebServer();
    }

    @Bean
//    @Conditional(JettyCondition.class)
    @ConditionalOnClass("org.eclipse.jetty.server.Server")
    public JettyWebServer getJettyWebServer(){
        return new JettyWebServer();
    }

}
