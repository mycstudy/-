package com.myc;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//按Servlet规范实现Response
public class Response extends AbstractHttpServletResponse {
    private int status = 200;
    private String message = "OK";
    private Map<String, String> headers = new HashMap<>();
    private ResponseServletOutputStream bodyOutputStream = new ResponseServletOutputStream();
    private Request request;
    private OutputStream outputStream;

    private final byte SP = ' ';
    private final byte CR = '\r';
    private final byte LF = '\n';


    public Response(Request request) {
        this.request = request;
        try {
            this.outputStream = request.getSocket().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setStatus(int i, String s) {
        status = i;
        message = s;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void addHeader(String s, String s1) {
        headers.put(s, s1);
    }

    @Override
    public ResponseServletOutputStream getOutputStream() throws IOException {
        return bodyOutputStream;
    }

    public void complete() {
        //发送响应
        try {
            SendResponseLine();
            SendResponseHeader();
            SendResponseBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void SendResponseBody() throws IOException {
        outputStream.write(getOutputStream().getBytes());
    }

    private void SendResponseHeader() throws IOException {
        //添加必要的header
        if (!headers.containsKey("Content-Length")) {
            addHeader("Content-Length", String.valueOf(getOutputStream().getPos()));
        }
        if (!headers.containsKey("Content-Type")) {
            addHeader("Content-Type", "application/json");
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            outputStream.write(key.getBytes());
            outputStream.write(":".getBytes());
            outputStream.write(value.getBytes());
            outputStream.write(CR);
            outputStream.write(LF);
        }
        outputStream.write(CR);
        outputStream.write(LF);
    }

    private void SendResponseLine() throws IOException {
        outputStream.write(request.getProtocol().getBytes());
        outputStream.write(SP);
        outputStream.write(status);
        outputStream.write(SP);
        outputStream.write(message.getBytes());
        outputStream.write(CR);
        outputStream.write(LF);
    }
}
