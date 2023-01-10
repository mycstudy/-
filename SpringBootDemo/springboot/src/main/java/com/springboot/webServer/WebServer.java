package com.springboot.webServer;

import org.springframework.web.context.WebApplicationContext;

//多态实现WebServer
public interface WebServer {
    void start(WebApplicationContext applicationContext);
}
