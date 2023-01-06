package com.myc;

import java.net.Socket;

//按Servlet规范实现Request
public class Request extends AbstractHttpServletRequest {
    private String method;
    private String url;
    private String protocol;

    private Socket socket;

    public Request(String method, String url, String protocol,Socket socket) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.socket=socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(url);
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
