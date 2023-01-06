package com.myc;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Tomcat {
    public static void main(String[] args) {
        Tomcat tomcat=new Tomcat();
        tomcat.start();
    }

    private void start() {
        //Tomcat连接TCP
        try {
            //异步BIO+线程池获取并处理socket连接
            ExecutorService executorService=new ThreadPoolExecutor(2,4,1000, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));
            ServerSocket socket=new ServerSocket(8080);
            while (true){
                Socket clientSocket=socket.accept();
                executorService.execute(new SocketProcessor(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
