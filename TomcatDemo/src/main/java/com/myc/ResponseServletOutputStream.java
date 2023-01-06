package com.myc;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ResponseServletOutputStream extends ServletOutputStream {
    private byte[] bytes=new byte[1024];
    private int pos;
    @Override
    public void write(int b) throws IOException {
        //暂存响应体，等到doGet方法结束后由Tomcat决定是否要发送到socket，而不是直接写入socket
        bytes[pos]= (byte) b;
        pos++;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
