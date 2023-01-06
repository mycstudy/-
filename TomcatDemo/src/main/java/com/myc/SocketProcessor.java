package com.myc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketProcessor implements Runnable{
    private Socket socket;

    public SocketProcessor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        processSocket(socket);
    }

    private void processSocket(Socket socket) {
        //处理socket连接
        try {
            //按HTTP协议解析字节流-->构造Request对象
            InputStream inputStream = socket.getInputStream();
            byte[] bytes=new byte[1024];
            inputStream.read(bytes);

            //解析请求行
            int pos=0;
            int begin=0;
            int end=0;
            for (;end<bytes.length;end++){
                if (bytes[end]==' ')
                    break;
            }
            StringBuilder method=new StringBuilder();
            for (;begin<end;begin++){
                method.append((char) bytes[begin]);
            }

            begin++;
            end++;
            pos=end;
            for (;end<bytes.length;end++){
                if (bytes[end]==' ')
                    break;
            }
            StringBuilder url=new StringBuilder();
            for (;begin<end;begin++){
                url.append((char) bytes[begin]);
            }

            begin++;
            end++;
            pos=end;
            for (;end<bytes.length;end++){
                if (bytes[end]=='\r')
                    if (end<bytes.length-1&&bytes[end+1]=='\n')
                        break;
            }
            StringBuilder protocol=new StringBuilder();
            for (;begin<end;begin++){
                protocol.append((char) bytes[begin]);
            }

            //构造Request对象
            Request request=new Request(method.toString(),url.toString(),protocol.toString(),socket);
            Response response=new Response(request);

            //匹配servlet,doGet
            MyServlet servlet=new MyServlet();
            servlet.service(request,response);

            //按HTTP协议发送响应数据
            response.complete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
